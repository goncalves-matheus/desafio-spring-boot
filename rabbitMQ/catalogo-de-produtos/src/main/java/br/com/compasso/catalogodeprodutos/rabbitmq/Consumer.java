package br.com.compasso.catalogodeprodutos.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.compasso.catalogodeprodutos.config.RabbitMessageConfig;
import br.com.compasso.catalogodeprodutos.model.ProductStatus;

@Component
public class Consumer {

    @RabbitListener(queues = RabbitMessageConfig.QUEUE_NAME)
    public void consumeMessage(ProductStatus productStatus){
        try {
            System.out.println("\nMessage recieved from the queue: "+RabbitMessageConfig.QUEUE_NAME+":\n" 
        + productStatus.toString());
        } catch (Exception e) {
            System.out.println("\n\nException:" +e.getMessage());
        }
        
    }
}
