package br.com.compasso.catalogodeprodutos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import br.com.compasso.catalogodeprodutos.model.Product;

public class ProductDto {
    
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public static List<ProductDto> convertListToDto(List<Product> products) {
        return products.stream().map(ProductDto::new).collect(Collectors.toList());
    }

}
