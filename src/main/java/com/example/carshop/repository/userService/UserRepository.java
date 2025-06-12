package com.example.carshop.repository.userService;

import com.example.carshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class UserRepository implements IUserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery("select c from User c", User.class);
        return query.getResultList();
    }

    @Override
    public User findById(Long id) {
        TypedQuery<User> query = entityManager.createQuery("select c from User c where  c.id=:id", User.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(User user) {
        if (user.getId() != null) {
            entityManager.merge(user);
        } else {
            entityManager.persist(user);
        }
    }

    @Override
    public void remove(Long id) {
        User user = findById(id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public List<User> searchByName(String keyword) {
        TypedQuery<User> sql = entityManager.createQuery("select b from User b where b.name like :name", User.class);
        sql.setParameter("name","%"+keyword+"%");
        return sql.getResultList();
    }

    @Override
    public User findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}