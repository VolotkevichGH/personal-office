package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.data.Group;
import com.example.mitsoproject.models.people.Student;
import com.example.mitsoproject.models.people.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.Optional;
import java.util.Set;

public interface StudentsRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
    Set<Student> findByGroup(Group group);
    Set<Student> findByUser_Name(String name);
    Set<Student> findByUser_Surname(String surname);
}
