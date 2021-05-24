package br.com.compasso.catalogodeprodutos.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.compasso.catalogodeprodutos.model.ProductStatus;

@Component
public class Consumer {

    @RabbitListener(queues = MenssagingConfig.QUEUE_NAME)
    public void consumeMessageFromQueue(ProductStatus productStatus){
        System.out.println("Message recieve from queue:" + productStatus.toString());
    }
}
