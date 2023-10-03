package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.data.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecializationRepository extends JpaRepository<Specialization,Long> {

    Specialization findByName(String name);
}
