package br.com.compasso.catalogodeprodutos.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.compasso.catalogodeprodutos.model.Product;

public class ProductDto {
    
    private String id;
    private String name;
    private String description;
    private Double price;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public static List<ProductDto> convertListToDto(List<Product> products) {
        return products.stream().map(ProductDto::new).collect(Collectors.toList());
    }

}
