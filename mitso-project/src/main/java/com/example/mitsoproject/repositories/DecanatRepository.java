package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.people.Decanat;
import com.example.mitsoproject.models.people.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DecanatRepository extends JpaRepository<Decanat, Long> {

    Optional<Decanat> findByUser(User user);
}
