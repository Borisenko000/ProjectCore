package edu.database.hibernate.usersDAO;

import edu.database.hibernate.sessionFactoryUtil.HibernateSessionFactoryUtil;
import edu.database.hibernate.usersEntity.UserEntity;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class UsersDAO {

    public Optional<UserEntity> get(Long id) {
        return Optional.of(HibernateSessionFactoryUtil.getSessionFactory().openSession().get(UserEntity.class, id));
    }


    public Optional<UserEntity> getUserByLogin(String login) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<UserEntity> cq = cb.createQuery(UserEntity.class);
            Root<UserEntity> root = cq.from(UserEntity.class);
            cq.select(root).where(cb.equal(root.get("login"), login));
            return Optional.of(session.createQuery(cq).getSingleResultOrNull());
        }
    }

    public void insertUser(String login, String password) {
        Transaction tx = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(new UserEntity(login, password));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                throw e;
            }
        }
    }

    public List<UserEntity> findAll() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("from UserEntity", UserEntity.class).list();
        }


    }
    public void delete(long id) {
        Transaction tx = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            UserEntity user = session.get(UserEntity.class, id);
            if (user != null) {
                session.remove(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                throw e;
            }
        }
    }


    public void dropTable() {
        Transaction tx = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            try {
                tx = session.beginTransaction();
                int amount = session.createQuery("DELETE FROM UserEntity").executeUpdate();
                tx.commit();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                    throw e;
                }
            }

        }
    }
}
