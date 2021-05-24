package br.com.compasso.catalogodeprodutos.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import br.com.compasso.catalogodeprodutos.dto.ProductDto;
import br.com.compasso.catalogodeprodutos.form.ProductForm;
import br.com.compasso.catalogodeprodutos.model.Product;
import br.com.compasso.catalogodeprodutos.model.ProductStatus;
import br.com.compasso.catalogodeprodutos.rabbitmq.MenssagingConfig;
import br.com.compasso.catalogodeprodutos.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductsController {
    
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private ProductRepository repository;

    @PostMapping
    public ResponseEntity<ProductDto> newProduct(@RequestBody @Valid ProductForm productForm,
            UriComponentsBuilder uriBuilder) {

        Product product = productForm.toProduct();
        repository.save(product);
        URI uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();

        ProductStatus productStatus = new ProductStatus(product, "PROCESS", "Product saved succesfully");
        template.convertAndSend(MenssagingConfig.EXCHANGE_NAME, MenssagingConfig.ROUTING_KEY, productStatus);
        
        return ResponseEntity.created(uri).body(new ProductDto(product));
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String id,
            @RequestBody @Valid ProductForm productForm) {
        Optional<Product> productOptional = repository.findById(id);
        if (productOptional.isPresent()) {
            Product updatatedProduct = productForm.update(productOptional.get());
            repository.save(updatatedProduct);
            return ResponseEntity.ok(new ProductDto(updatatedProduct));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findProduct(@PathVariable String id) {
        Optional<Product> productOptional = repository.findById(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(new ProductDto(productOptional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        List<Product> products = repository.findAll();
        return ProductDto.convertListToDto(products);
    }

    @GetMapping("/search")
    public List<ProductDto> searchProduct(@RequestParam(defaultValue = "0.00") Double min_price,
            @RequestParam(defaultValue = "999999999.99") Double max_price, @RequestParam(required = false) String q) {
        
        List<Product> products;
        if (q == null) {
            products = repository.findbyMinAndMaxValues(min_price, max_price);
        } else {
            products = repository.findbyNameDescriptionMinAndMaxValues(min_price, max_price, q);
        }
        return ProductDto.convertListToDto(products);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Optional<Product> productOptional = repository.findById(id);
        if (productOptional.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
