package com.example.mitsoproject.services;

import com.example.mitsoproject.config.SpringSecurityConfig;
import com.example.mitsoproject.models.DataStudents;
import com.example.mitsoproject.models.Role;
import com.example.mitsoproject.models.people.Admin;
import com.example.mitsoproject.models.people.Curator;
import com.example.mitsoproject.models.people.Student;
import com.example.mitsoproject.models.people.User;
import com.example.mitsoproject.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CuratorRepository curatorRepository;
    private final StudentsRepository studentsRepository;
    private final AdminRepository adminRepository;
    private final DataStudentsRepository builder;

    public String alphaNumericString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public void addStudent(Student student, String course, String faculty, String specialization, String nameGroup){
        student.setCourse(course);
        student.setFaculty(faculty);
        student.setSpecialization(specialization);
        student.setNameGroupe(nameGroup);
        student.getUser().getRoles().clear();
        student.getUser().getRoles().add(Role.ROLE_STUDENT);
        studentsRepository.save(student);
    }

    public void addAdmin(Admin user){
        user.getUser().getRoles().clear();
        user.getUser().getRoles().add(Role.ROLE_ADMIN);
        adminRepository.save(user);
    }

    public void addCurator(User user, String faculty, String nameGroup, String course, String lesson, String specialization){
        Curator curator = new Curator();
        curator.setUser(user);
        curator.getUser().getRoles().clear();
        curator.getUser().getRoles().add(Role.ROLE_CURATOR);
        curator.setFaculty(faculty);
        curator.setNameGroupe(nameGroup);
        curator.setCourse(course);
        curator.setLesson(lesson);
        curator.setSpecialization(specialization);
        userRepository.save(curator.getUser());
        curatorRepository.save(curator);
    }

    public void addAccount(String name, String surname){
        User user = new User();
        Student student = new Student();
        student.setUser(user);
        student.getUser().setName(name);
        student.getUser().setSurname(surname);

        String password = alphaNumericString(10);
        student.getUser().setUsername("student-" + alphaNumericString(4));
        student.getUser().setPassword(SpringSecurityConfig.passwordEncoder().encode(password));

        student.getUser().setEmail(alphaNumericString(10) + "@gmail.com");
        student.getUser().setRoles(Set.of(Role.ROLE_STUDENT));
        userRepository.save(user);
        studentsRepository.save(student);

        DataStudents data = new DataStudents();
        data.setData(student.getUser().getName() + " " + student.getUser().getSurname() + " Данные:  login: \"" + student.getUser().getUsername() + "\". Password: \"" + password + "\".");
        builder.save(data);
    }

}
