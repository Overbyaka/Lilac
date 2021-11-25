package ru.grishkova.kursovaya.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.grishkova.kursovaya.entity.Basket;
import ru.grishkova.kursovaya.entity.Product;

import java.util.List;

@Repository
public interface BasketRepository extends CrudRepository<Basket, Long> {
    public Basket findByProduct(Product product);
}
