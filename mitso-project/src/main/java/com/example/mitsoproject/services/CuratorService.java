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


    public void deleteFromGroup(Student student, Curator curator){
        student.getGroup().setCurator(null);
        student.getGroup().getSpecialization().setFaculty(null);
        student.getGroup().setSpecialization(null);
        student.getGroup().setCourse("");
        student.getGroup().setCurator(null);
        studentsRepository.save(student);
        curator.getGroup().getStudents().remove(student);
        curatorRepository.save(curator);
    }
}
