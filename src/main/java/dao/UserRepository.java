package dao;

import jakarta.persistence.*;
import model.User;

public class UserRepository {

    private final EntityManagerFactory emf;

    public UserRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public User findByUsername(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "select u from User u where u.username = :u", User  .class)
                    .setParameter("u", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void create(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
    }
}
