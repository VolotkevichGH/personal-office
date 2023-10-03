package com.example.mitsoproject.models.data;

import com.example.mitsoproject.models.people.Decanat;
import com.example.mitsoproject.models.people.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "faculty")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany
    private Set<Teacher> teachers;
    @OneToMany
    private Set<Specialization> specializations;
    @OneToOne
    private Decanat decanat;
}
