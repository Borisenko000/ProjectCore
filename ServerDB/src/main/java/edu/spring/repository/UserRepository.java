package edu.spring.repository;

import edu.spring.module.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> getUserById(Long id);

    public Optional<User> getUserByEmail(String email);

    public void deleteUserById(Long id);

    @Query("SELECT u.password FROM User u WHERE u.id = :id")
    Optional<String> findHashPasswordById(@Param("id") Long id);

   // @Query("SELECT u.passwordPlain FROM User u WHERE u.id = :id")
    //Optional<String> findPlainPasswordById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :value WHERE u.id = :id")
    public void updateHashPassword(@Param("id") Long id, @Param("value") String value);
}
