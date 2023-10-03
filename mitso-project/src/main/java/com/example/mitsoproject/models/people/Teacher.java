package com.example.mitsoproject.models.people;

import com.example.mitsoproject.models.data.Faculty;
import com.example.mitsoproject.models.data.Lesson;
import com.example.mitsoproject.models.data.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teachers")
public class Teacher{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    private Set<Faculty> faculty;
    @ManyToMany
    private Set<Lesson> lesson;
    @OneToOne
    private User user;
}
