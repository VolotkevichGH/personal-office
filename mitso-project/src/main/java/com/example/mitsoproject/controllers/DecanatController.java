package com.example.mitsoproject.controllers;

import com.example.mitsoproject.models.people.User;
import com.example.mitsoproject.repositories.DataStudentsRepository;
import com.example.mitsoproject.repositories.UserRepository;
import com.example.mitsoproject.services.AdminService;
import com.example.mitsoproject.services.DecanatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class DecanatController {

    private final AdminService adminService;
    private final DecanatService decanatService;
    private final DataStudentsRepository builder;
    private final UserRepository userRepository;


    @GetMapping("/decanat")
    public String office(Model model){
        String studentName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(studentName).get();
        model.addAttribute("user", user);
        return "decanat";
    }

    @PostMapping("/decanat/add/group")
    public String addGroup(Model model, String name, String course, String specName, String curatorName, String curatorSurname, String students){
        decanatService.addGroup(name,course,specName,curatorName,curatorSurname,students);
        return "redirect:/decanat/students/data";
    }

    @GetMapping("/decanat/students/data")
    public String getData(Model model) {
        model.addAttribute("data", builder.findAll());
        return "data-students";
    }

}
