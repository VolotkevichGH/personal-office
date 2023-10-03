package com.example.mitsoproject.models.people;


import com.example.mitsoproject.models.data.Faculty;
import com.example.mitsoproject.models.data.Group;
import com.example.mitsoproject.models.data.Lesson;
import com.example.mitsoproject.models.data.Specialization;
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
    @OneToOne
    private Group group;
    @OneToOne
    private User user;
}
