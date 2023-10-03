package com.example.mitsoproject.repositories;

import ch.qos.logback.core.joran.spi.ElementPath;
import com.example.mitsoproject.models.people.Admin;
import com.example.mitsoproject.models.people.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByUser(User user);
}

