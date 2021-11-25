package ru.grishkova.kursovaya.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.grishkova.kursovaya.entity.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}
