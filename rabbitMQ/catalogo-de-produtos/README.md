# Catálogo de Produtos API REST 

## Documentação Swagger
http://localhost:9999/swagger-ui/index.html

## Dependencias:
* Java: 16
* RabbitMQ 3.8.9
* Erlang 22.3
* Banco NoSQL: MongoDB
* Spring-boot: 2.4.5

## Configurações:
* Porta da API REST: 9999
* Porta do servidor RabbitMQ: 15672
* Nome do Mongo Database: catalogo-de-produtos

---
## RabbitMQ

### Rota das mensagens:

| Exchange         | Queue         | Routing Key        |
|------------------|:-------------:|-------------------:|
| catalog_exchange | catalog_queue | catalog_routingKey |
### Funcionamento:

Ao criar um novo produto, ou atualizar um produto existente, é postado uma mensagem na fila **"catalog_queue"**, contendo o status da operação ***"status"***, uma mensagem descritiva ***"message"*** e o objeto **"product"**. A classe **"Consumer"** é a responsável por consumir as mensagens da fila, e imprimi-las no console.

## Endpoints 

| Requisição  |  Endpoint    | Descrição                                                                   |
|-------------|:-----------------:|----------------------------------------------------------------------------:|
| GET         |  /products/{id}   | Retorna um único produto do catálogo através da busca pelo ID               |
| GET         |  /products        | Retorna todos os produtos do catálogo                                       |
| GET         |  /products/search | Busca por um produto a partir dos seu intervalo de preço, nome ou descrição |
| POST        |  /products        | Cria um novo produto no catálogo e salva no banco de dados                  |
| PUT         |  /products/{id}   | Atualiza um produto do catálogo através da busca pelo ID                    |
| DELETE      |  /products/{id}   | Deleta um produto do catálogo a partir do seu ID                            |




