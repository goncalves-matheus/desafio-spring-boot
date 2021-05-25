package br.com.compasso.catalogodeprodutos.model;

public class ProductStatus {

    private Product product;
    private Status status;
    private String message;

    public ProductStatus() {
    }

    public ProductStatus(Product product, Status status) {
        this.product = product;
        this.status = status;
        setMessage(status);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(Status status) {
        switch (status) {
            case SAVED:
                this.message = "Product saved successfully";
                break;
            case UPDATED:
                this.message = "Product updated successfully";
                break;
            case UPDATE_ERROR:
                this.message = "Fail to update the product";
                break;
            case SAVE_ERROR:
                this.message = "Fail to save the product";
                break;
            default:
                this.message = "";
                break;
        }
    }

    @Override
    public String toString() {
        return "[Status: " + this.status + " | Message: " + this.message + "]\nProduct: " + this.product.toString();
    }


}
