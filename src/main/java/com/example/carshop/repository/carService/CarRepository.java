package com.example.carshop.repository.carService;

import com.example.carshop.model.Car;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class CarRepository implements ICarRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Car> findAll() {
        TypedQuery<Car> query = entityManager.createQuery("select c from Car c", Car.class);
        return query.getResultList();
    }

    @Override
    public Car findById(Long id) {
        TypedQuery<Car> query = entityManager.createQuery("select c from Car c where  c.id=:id", Car.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Car beverage) {
        if (beverage.getId() != null) {
            entityManager.merge(beverage);
        } else {
            entityManager.persist(beverage);
        }
    }

    @Override
    public void remove(Long id) {
        Car car = findById(id);
        if (car != null) {
            entityManager.remove(car);
        }
    }

    @Override
    public List<Car> searchByName(String keyword) {
        String query = "SELECT b FROM Car b WHERE LOWER(b.name) LIKE LOWER(:name)";
        TypedQuery<Car> sql = entityManager.createQuery(query, Car.class);
        sql.setParameter("name", "%" + keyword + "%");
        return sql.getResultList();
    }

}