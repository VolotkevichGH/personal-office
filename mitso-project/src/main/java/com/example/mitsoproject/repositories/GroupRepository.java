package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.data.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group,Long> {
}
