package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.people.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentsRepository extends JpaRepository<Student, Long> {
}
