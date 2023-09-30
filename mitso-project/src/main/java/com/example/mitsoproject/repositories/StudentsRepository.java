package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.people.Student;
import com.example.mitsoproject.models.people.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentsRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
}
