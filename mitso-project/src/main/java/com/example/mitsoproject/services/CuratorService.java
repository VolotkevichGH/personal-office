package com.example.mitsoproject.services;

import com.example.mitsoproject.models.people.Curator;
import com.example.mitsoproject.models.people.Student;
import com.example.mitsoproject.repositories.AdminRepository;
import com.example.mitsoproject.repositories.CuratorRepository;
import com.example.mitsoproject.repositories.StudentsRepository;
import com.example.mitsoproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CuratorService {

    private final UserRepository userRepository;
    private final CuratorRepository curatorRepository;
    private final StudentsRepository studentsRepository;
    private final AdminRepository adminRepository;

    public void addStudentInGroup(Student student, Curator curator) {
        student.setCurator(curator);
        student.setCourse(curator.getCourse());
        student.setNameGroupe(curator.getNameGroupe());
        student.setFaculty(curator.getFaculty());
        student.setSpecialization(curator.getSpecialization());
        curator.getStudent().add(student);
        curatorRepository.save(curator);
        studentsRepository.save(student);
    }

    public void deleteFromGroup(Student student, Curator curator){
        student.setCurator(null);
        student.setFaculty("");
        student.setSpecialization("");
        student.setCourse("");
        student.setNameGroupe("");
        studentsRepository.save(student);
        curator.getStudent().remove(student);
        curatorRepository.save(curator);
    }
}
