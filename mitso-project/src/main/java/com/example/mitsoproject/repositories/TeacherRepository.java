package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.people.Teacher;
import com.example.mitsoproject.models.people.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUser(User user);
}
