package com.example.mitsoproject.models.people;


import com.example.mitsoproject.models.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "curators")
public class Curator{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String course;
    private String faculty;
    private String specialization;
    private String nameGroupe;
    private String lesson;
    @OneToOne
    private User user;
    @OneToMany
    @ElementCollection(targetClass = Student.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "curator_students", joinColumns = @JoinColumn(name = "curator_id"))
    private Set<Student> student;

}
