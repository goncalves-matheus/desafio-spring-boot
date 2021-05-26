package br.com.compasso.catalogodeprodutos.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import br.com.compasso.catalogodeprodutos.model.Product;

public class ProductForm {
    
    @NotBlank(message = "O nome precisa ser preenchido")
    private String name;
    @NotBlank(message = "A descrição precisa ser preenchida")
    private String description;
    @PositiveOrZero(message = "O preço precisa ser maior que zero") @NotNull(message = "O preço precisa ser preenchido")
    private Double price;

    public Product toProduct(){
        return new Product(name, description, price);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Product update(Product product) {
        product.setName(this.name);
        product.setDescription(this.description);
        product.setPrice(this.price);
        return product;
    }


    
}
