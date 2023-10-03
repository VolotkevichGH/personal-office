package com.example.mitsoproject.models.data;

import com.example.mitsoproject.models.people.Curator;
import com.example.mitsoproject.models.people.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "table_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String course;
    @ManyToOne
    private Specialization specialization;
    @OneToMany
    private Set<Student> students;
    @OneToOne
    private Curator curator;
}
