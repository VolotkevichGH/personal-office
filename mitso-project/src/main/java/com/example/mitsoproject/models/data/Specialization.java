package com.example.mitsoproject.models.data;

import com.example.mitsoproject.models.people.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "specialization")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private Faculty faculty;
    @OneToMany
    private Set<Group> groups;
}
