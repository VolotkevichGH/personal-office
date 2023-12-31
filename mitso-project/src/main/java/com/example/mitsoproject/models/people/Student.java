package com.example.mitsoproject.models.people;

import com.example.mitsoproject.models.data.Faculty;
import com.example.mitsoproject.models.data.Group;
import com.example.mitsoproject.models.data.Specialization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Group group;
    @OneToOne
    private User user;
}
