package com.example.mitsoproject.controllers;

import com.example.mitsoproject.config.SpringSecurityConfig;
import com.example.mitsoproject.models.Role;
import com.example.mitsoproject.models.people.Curator;
import com.example.mitsoproject.models.people.Student;
import com.example.mitsoproject.models.people.User;
import com.example.mitsoproject.repositories.CuratorRepository;
import com.example.mitsoproject.repositories.StudentsRepository;
import com.example.mitsoproject.repositories.UserRepository;
import com.example.mitsoproject.services.CuratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CuratorController {

    private final UserRepository userRepository;
    private final StudentsRepository studentsRepository;
    private final CuratorRepository curatorRepository;
    private final CuratorService curatorService;

    @GetMapping("/curator/profile")
    public String profile(Model model){
        String curatorName = SecurityContextHolder.getContext().getAuthentication().getName();
        User curatorUser = userRepository.findByUsername(curatorName).get();
        model.addAttribute("curator", curatorUser);
        return "curator";
    }

    @GetMapping("/curator/mystudents")
    public String curatorStudents(Model model) {
        String curatorName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(curatorName).get();
        model.addAttribute("user", user);
       Curator curator = curatorRepository.findByUser(user).get();
       List<Student> curatorStudent = curator.getStudent().stream().sorted(Comparator.comparing(Student :: getId)).toList();
       model.addAttribute("curatorStudents", curatorStudent);
        return "curator-mystudents";
    }

    @GetMapping("/curator/allstudents")
    public String getAllStudents(Model model){
        List<User> allStudentsTst = userRepository.findAllByRoles(Role.ROLE_STUDENT);
        List<User> allStudents = new ArrayList<>();
        for (User student : allStudentsTst){
            Student student1 = studentsRepository.findByUser(student).get();
            if (student1.getCurator() == null) {
                allStudents.add(student);
            }
        }
        model.addAttribute("userList", allStudents);
        return "curator-add-students";
    }

    @PostMapping("/curator/allstudents")
    public String allStudents(Model model){
        return "redirect:/curator/profile";
    }

    @PostMapping("/curator/addGroupStudent/{user}")
    public String addGroupStudent(@PathVariable User user){
        String curatorName = SecurityContextHolder.getContext().getAuthentication().getName();
        User curatorUser = userRepository.findByUsername(curatorName).get();
        Curator curator = curatorRepository.findByUser(curatorUser).get();
        Student student = studentsRepository.findByUser(user).get();
        curatorService.addStudentInGroup(student,curator);
        return "redirect:/curator/allstudents";
    }

    @GetMapping("/curator/edit")
    public String curatorEdit(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user1 = curatorRepository.findByUser_Username(name).get().getUser();
        model.addAttribute("user", user1);
        return "edit-profile-curator";
    }

    @PostMapping("/curator/edit")
    public String curatorOfficePost(Model model, @RequestParam String username, @RequestParam String email, @RequestParam String oldpassword, @RequestParam String password) {
        String curatorName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(curatorName).get();
        boolean hasUsername = userRepository.existsByUsername(username) && !username.equals(user.getUsername());
        boolean hasEmail = userRepository.existsByEmail(email) && !user.getEmail().equals(email);
        if (SpringSecurityConfig.passwordEncoder().matches(oldpassword, user.getPassword())) {
            user.setPassword(SpringSecurityConfig.passwordEncoder().encode(password));
            if (!hasEmail) {
                user.setEmail(email);
            }
            if (!hasUsername) {
                user.setUsername(username);
            }
        }
        userRepository.save(user);
        return "redirect:/logout";
    }

    @PostMapping("/curator/delete/{user}")
    public String deleteFromGroup(@PathVariable User user){
        Student student = studentsRepository.findByUser(user).get();
        Curator curator = student.getCurator();
        curatorService.deleteFromGroup(student,curator);
        return "redirect:/curator/mystudents";
    }


}
