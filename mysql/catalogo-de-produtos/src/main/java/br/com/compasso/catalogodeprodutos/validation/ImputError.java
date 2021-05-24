package br.com.compasso.catalogodeprodutos.validation;

public class ImputError {
    
    private String status_code;
    private String message;

    public ImputError(String status_code, String message) {
        this.status_code = status_code;
        this.message = message;
    }

    public String getCode() {
        return status_code;
    }

    public String getMessage() {
        return message;
    }
        
}
