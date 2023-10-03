package com.example.mitsoproject.models.people;

import com.example.mitsoproject.models.data.Faculty;
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
@Table(name = "decanat")
public class Decanat{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Faculty faculty;
    @OneToOne
    private User user;

}
