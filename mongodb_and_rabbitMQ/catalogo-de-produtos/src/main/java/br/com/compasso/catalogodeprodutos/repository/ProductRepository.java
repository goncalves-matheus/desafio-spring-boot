package br.com.compasso.catalogodeprodutos.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.compasso.catalogodeprodutos.model.Product;

@Repository
@Transactional
public interface ProductRepository extends MongoRepository<Product, String>{

    @Query("{ $and: [ {'price':{$gte: ?0} }, {'price':{$lte: ?1} } ] }")
    List<Product> findbyMinAndMaxValues(Double min_price, Double max_price);

    @Query("{  $and: [{$and: [ {'price':{$gte: ?0} }, {'price':{$lte: ?1} } ]},{$or: [ {'description': ?2}, {'name': ?2} ]}] }")
    List<Product> findbyNameDescriptionMinAndMaxValues(Double min_price, Double max_price, String q);
    
}
