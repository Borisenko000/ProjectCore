package edu.database.hibernate.sessionFactoryUtil;

import edu.database.hibernate.usersEntity.UserEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class HibernateSessionFactoryUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private  HibernateSessionFactoryUtil() {}

    public static SessionFactory buildSessionFactory() {
            try{
                return new Configuration().configure().buildSessionFactory();
            } catch (Exception e) {
                throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + e);
            }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
