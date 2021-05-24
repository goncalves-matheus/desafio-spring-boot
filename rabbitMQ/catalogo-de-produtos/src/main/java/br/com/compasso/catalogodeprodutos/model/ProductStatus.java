package br.com.compasso.catalogodeprodutos.model;

public class ProductStatus {

    private Product product;
    private String status;
    private String message;

    public ProductStatus() {
    }

    public ProductStatus(Product product, String status, String message) {
        this.product = product;
        this.status = status;
        this.message = message;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
}
