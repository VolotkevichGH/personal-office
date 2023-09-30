package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.people.Curator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CuratorRepository extends JpaRepository<Curator, Long> {
    Optional<Curator> findByUserByUsername (String username);
}
