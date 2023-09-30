package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.people.Curator;
import com.example.mitsoproject.models.people.Student;
import com.example.mitsoproject.models.people.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CuratorRepository extends JpaRepository<Curator, Long> {
    Optional<Curator> findByUser_Username(String username);
    Optional<Curator> findByUser(User user);
}
