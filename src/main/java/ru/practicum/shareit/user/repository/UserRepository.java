package ru.practicum.shareit.user.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = COALESCE(:email, u.email), u.name = COALESCE(:name, u.name) WHERE u.id = :id")
    void patchUser(@Param("email") String email,
                   @Param("name") String name,
                   @Param("id") Long id);
}
