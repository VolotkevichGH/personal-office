package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.data.Role;
import com.example.mitsoproject.models.people.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    List<User> findAllByRoles(Role roles);

    Optional<User> findByName(String name);
    Optional<User> findBySurname(String surname);
}