package br.com.compasso.catalogodeprodutos.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.compasso.catalogodeprodutos.config.RabbitMessageConfig;
import br.com.compasso.catalogodeprodutos.dto.ProductDto;
import br.com.compasso.catalogodeprodutos.form.ProductForm;
import br.com.compasso.catalogodeprodutos.model.Product;
import br.com.compasso.catalogodeprodutos.model.ProductStatus;
import br.com.compasso.catalogodeprodutos.model.Status;
import br.com.compasso.catalogodeprodutos.repository.ProductRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/products")
@Api(value = "API REST Catalogo de Produtos")
@CrossOrigin(origins = "*")
public class ProductsController {
    
    private static final String DEFAULT_MIN_PRICE = "0.00";
    private static final String DEFAULT_MAX_PRICE = "999999999.99";

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private ProductRepository repository;

    @PostMapping
    @ApiOperation(value = "Cria um novo produto no catálogo e salva no banco não relacional")
    public ResponseEntity<Object> newProduct(@RequestBody @Valid ProductForm productForm,
            UriComponentsBuilder uriBuilder) {
       
        Product product = productForm.toProduct();
        repository.save(product);
        sendMessage(product, Status.SAVED);
    
        URI uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(new ProductDto(product));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Atualiza um produto do catálogo através da busca pelo ID")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String id,
            @RequestBody @Valid ProductForm productForm) {

        Optional<Product> productOptional = repository.findById(id);

        if (productOptional.isPresent()) {
            Product updatatedProduct = productForm.update(productOptional.get());
            repository.save(updatatedProduct);

            sendMessage(updatatedProduct, Status.UPDATED);
            
            return ResponseEntity.ok(new ProductDto(updatatedProduct));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Retorna um único produto do catálogo através da busca pelo ID")
    public ResponseEntity<ProductDto> findProduct(@PathVariable String id) {
        Optional<Product> productOptional = repository.findById(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(new ProductDto(productOptional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @ApiOperation(value = "Retorna todos os produtos do catálogo")
    public List<ProductDto> getAllProducts() {
        List<Product> products = repository.findAll();
        return ProductDto.convertListToDto(products);
    }

    @GetMapping("/search")
    @ApiOperation(value = "Busca por um produto a partir dos seu intervalo de preço, nome ou descrição")
    public List<ProductDto> searchProduct(@RequestParam(defaultValue = DEFAULT_MIN_PRICE) Double min_price,
            @RequestParam(defaultValue = DEFAULT_MAX_PRICE) Double max_price, @RequestParam(required = false) String q) {
        
        List<Product> products;
        if (q == null) {
            products = repository.findbyMinAndMaxValues(min_price, max_price);
        } else {
            products = repository.findbyNameDescriptionMinAndMaxValues(min_price, max_price, q);
        }
        return ProductDto.convertListToDto(products);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deleta um produto do catálogo a partir do seu ID")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Optional<Product> productOptional = repository.findById(id);
        if (productOptional.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    public void sendMessage(Product product, Status status) {
        ProductStatus productStatus = new ProductStatus(product, status);
        template.convertAndSend(RabbitMessageConfig.EXCHANGE_NAME, RabbitMessageConfig.ROUTING_KEY, productStatus);
    }

}
