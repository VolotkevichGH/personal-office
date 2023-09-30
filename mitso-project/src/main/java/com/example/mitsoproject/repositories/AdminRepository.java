package com.example.mitsoproject.repositories;

import com.example.mitsoproject.models.people.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
}
