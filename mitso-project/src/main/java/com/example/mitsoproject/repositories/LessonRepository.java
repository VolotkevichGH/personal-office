package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.data.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
}
