package br.com.compasso.catalogodeprodutos.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.compasso.catalogodeprodutos.model.Product;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long>{

    @Query(value = "select * from Product p where p.price >= :min_price and p.price <= :max_price", nativeQuery = true)
    List<Product> findbyMinAndMaxValues(@Param ("min_price") BigDecimal min_price, @Param ("max_price") BigDecimal max_price);

    @Query(value = "select * from Product p where p.price >= :min_price and p.price <= :max_price and (name = :q or description = :q)", nativeQuery = true)
    List<Product> findbyNameDescriptionMinAndMaxValues(@Param ("min_price") BigDecimal min_price, @Param ("max_price") BigDecimal max_price, @Param("q") String q);
    
}
