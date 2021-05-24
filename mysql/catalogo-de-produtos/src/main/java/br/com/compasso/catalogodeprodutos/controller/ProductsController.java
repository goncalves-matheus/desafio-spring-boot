package br.com.compasso.catalogodeprodutos.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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
import br.com.compasso.catalogodeprodutos.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository repository;

    @PostMapping
    public ResponseEntity<ProductDto> newProduct(@RequestBody @Valid ProductForm productForm,
            UriComponentsBuilder uriBuilder) {

        Product product = productForm.toProduct();
        repository.save(product);
        URI uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(new ProductDto(product));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
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
    public ResponseEntity<ProductDto> findProduct(@PathVariable Long id) {
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
    public List<ProductDto> searchProduct(@RequestParam(defaultValue = "0.00") String min_price,
            @RequestParam(defaultValue = "999999999.99") String max_price, @RequestParam(required = false) String q) {

        if (q == null) {
            List<Product> products = repository.findbyMinAndMaxValues(new BigDecimal(min_price),
                    new BigDecimal(max_price));
            return ProductDto.convertListToDto(products);
        } else {
            List<Product> products = repository.findbyNameDescriptionMinAndMaxValues(new BigDecimal(min_price),
                    new BigDecimal(max_price), q);
            return ProductDto.convertListToDto(products);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Product> productOptional = repository.findById(id);
        if (productOptional.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
