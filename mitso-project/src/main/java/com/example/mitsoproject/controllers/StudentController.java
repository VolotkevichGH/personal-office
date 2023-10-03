package com.example.mitsoproject.controllers;

import com.example.mitsoproject.config.SpringSecurityConfig;
import com.example.mitsoproject.models.people.User;
import com.example.mitsoproject.repositories.StudentsRepository;
import com.example.mitsoproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class StudentController {

    private final UserRepository userRepository;
    private final StudentsRepository studentsRepository;

    @GetMapping("/student/edit")
    public String studentEdit(Model model) {
        String studentName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(studentName).get();
        model.addAttribute("user", user);
        return "edit-profile-student";
    }

    @PostMapping("/student/edit")
    public String studentOfficePost(Model model, @RequestParam String username, @RequestParam String email, @RequestParam String oldpassword, @RequestParam String password) {
        String studentName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(studentName).get();
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


    @GetMapping("/student/profile")
    public String studentOffice(Model model) {
        String studentName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(studentName).get();
        model.addAttribute("user", user);
        String dayOfWeek = LocalDate.now().getDayOfWeek().name();
        int day = LocalDate.now().getDayOfMonth();
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        model.addAttribute("dayOfWeek", dayOfWeek);
        model.addAttribute("day", day);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        return "student";
    }


}
