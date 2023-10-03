package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.data.Faculty;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {
    Optional<Faculty> findByName(String facultyName);
}
