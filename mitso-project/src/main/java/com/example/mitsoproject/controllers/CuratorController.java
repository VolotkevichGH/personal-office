package com.example.mitsoproject.controllers;

import com.example.mitsoproject.config.SpringSecurityConfig;
import com.example.mitsoproject.models.Role;
import com.example.mitsoproject.models.people.Student;
import com.example.mitsoproject.models.people.User;
import com.example.mitsoproject.repositories.CuratorRepository;
import com.example.mitsoproject.repositories.StudentsRepository;
import com.example.mitsoproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CuratorController {

    private final UserRepository userRepository;
    private final StudentsRepository studentsRepository;
    private final CuratorRepository curatorRepository;

    @GetMapping("/curator/profile")
    public String curatorOffice(Model model) {
        String studentName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(studentName).get();
        model.addAttribute("user", user);
        List<Student> userList = studentsRepository.findAll();
        List<Student> userListForCurator = new ArrayList<>();
        for (Student user1 : userList) {
            if (user1.getCurator().equals(user)) {
                userListForCurator.add(user1);
            }
        }
        model.addAttribute("userList", userListForCurator);
        return "curator";
    }


    @GetMapping("/curator/edit")
    public String curatorEdit(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user1 = curatorRepository.findByUserByUsername(name).get().getUser();
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


}
