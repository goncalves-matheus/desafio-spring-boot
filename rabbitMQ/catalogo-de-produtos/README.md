# Catálogo de Produtos API REST 

## Documentação Swagger
http://localhost:9999/swagger-ui/index.html

## Versões e Dependencias:
* Java: 16
* RabbitMQ: 3.8.9
* Erlang 22.3
* Spring-boot: 2.4.5
* Banco de Dados NoSQL: MongoDB

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

Ao criar um novo produto, ou atualizar um produto existente, é postado uma mensagem na fila **"catalog_queue"**, contendo o status da operação ***"status"***, uma mensagem descritiva ***"message"*** e o objeto **"product"**. 

A classe **"Consumer"** é a responsável por consumir as mensagens da fila, e imprimi-las no console.
### Saída da Mensagem em Json:
```json
{
    "product":{
            "id":"60ad4a4019052f0bbfd35fe0",
            "name":"nome",
            "description":"descrição",
            "price":1800.0
        },
        "status":"SAVED",
        "message":"Product saved successfully"
}
```
### Saída da Mensagem no Terminal:
```
Message recieved from the queue: catalog_queue:
[Status: SAVED | Message: Product saved successfully]
Product: [Id: 60ad4a5a19052f0bbfd35fe3 | Name: nome | Description: descrição | Price: 1800.0]
```

## Endpoints 

| Requisição  |  Endpoint    | Descrição                                                                   |
|-------------|:-----------------:|----------------------------------------------------------------------------:|
| GET         |  /products/{id}   | Retorna um único produto do catálogo através da busca pelo ID               |
| GET         |  /products        | Retorna todos os produtos do catálogo                                       |
| GET         |  /products/search | Busca por um produto a partir dos seu intervalo de preço, nome ou descrição |
| POST        |  /products        | Cria um novo produto no catálogo e salva no banco de dados                  |
| PUT         |  /products/{id}   | Atualiza um produto do catálogo através da busca pelo ID                    |
| DELETE      |  /products/{id}   | Deleta um produto do catálogo a partir do seu ID                            |

## Busca Personalizada Pelo Endpoint Search
O endpoint ***/products/search*** recebe três parâmetros opcionais:

| Parametro | Função                        |
|-----------|:-----------------------------:|
| min_price | Filtra pelo preço mínimo      |
| max_price | Filtra pelo preço máximo      |
| q         | Filtra pelo nome ou descrição |

Caso o preço máximo não seja informado, o seu valor padrão é 1 trilhão.

## Entrada de Dados:
Os inputs de valores nos endpoits PUT e POST são em Json e seguem o seguinte padrão:
```json
 {
    "name": "nome",
    "description": "descrição",
    "price": <preco>
}
```
E como resposta recebe o seguinte json:
```json
 {
    "id": "id em hexadecimal",
    "name": "nome",
    "description": "descrição",
    "price": <preco>
}
```
Ou um json indicando erro de sintaxe ou falta de algum parâmetro:
```json
{
    "message": "Origem do erro",
    "code": "Código do erro"
}
```
## Saída de Dados:
Os outputs de valores nos endpoits GET seguem o padrão:
```json
 {
    "id": "id em hexadecimal",
    "name": "nome",
    "description": "descrição",
    "price": <preco>
}
```




