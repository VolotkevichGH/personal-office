package com.example.mitsoproject.controllers;

import com.example.mitsoproject.models.data.Role;
import com.example.mitsoproject.models.people.Student;
import com.example.mitsoproject.models.people.User;
import com.example.mitsoproject.repositories.StudentsRepository;
import com.example.mitsoproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TeacherController {
    private final UserRepository userRepository;
    private final StudentsRepository studentsRepository;


    @GetMapping("/teacher")
    public String office(Model model) {
        List<User> allStudentsTst = userRepository.findAllByRoles(Role.ROLE_STUDENT);
        List<Student> allStudents = new ArrayList<>();
        for (User student : allStudentsTst) {
            Student student1 = studentsRepository.findByUser(student).get();
            allStudents.add(student1);
        }
        model.addAttribute("students", allStudents);
        return "teacher";
    }

}
