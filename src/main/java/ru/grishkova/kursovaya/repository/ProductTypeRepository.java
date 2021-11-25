package ru.grishkova.kursovaya.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.grishkova.kursovaya.entity.Product;
import ru.grishkova.kursovaya.entity.ProductType;

import java.util.List;

@Repository
public interface ProductTypeRepository extends CrudRepository<ProductType, Long> {

}
