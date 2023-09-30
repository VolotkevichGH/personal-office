package com.example.mitsoproject.models.people;


import jakarta.persistence.*;
import lombok.*;

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

}
