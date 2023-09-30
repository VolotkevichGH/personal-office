package com.example.mitsoproject.controllers;

import com.example.mitsoproject.models.people.Admin;
import com.example.mitsoproject.models.people.Student;
import com.example.mitsoproject.models.people.User;
import com.example.mitsoproject.repositories.*;
import com.example.mitsoproject.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final AdminService adminService;
    private final DataStudentsRepository builder;
    private final StudentsRepository studentsRepository;
    private final AdminRepository adminRepository;
    private final CuratorRepository curatorRepository;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin";
    }

    @PostMapping("/admin/giveadmin/{user}")
    public String adminPost(@PathVariable Admin user) {
        adminService.addAdmin(user);
        return "redirect:/admin";
    }


    @GetMapping("/admin/givestudent/{user}")
    public String setStudent(Model model, @PathVariable Student user) {
        model.addAttribute("studentName", user.getUser().getName());
        model.addAttribute("studentSurname", user.getUser().getSurname());
        return "set-student";
    }

    @PostMapping("/admin/givestudent/{student}")
    public String setStudentPost(@PathVariable Student student, @RequestParam String course,
                                 @RequestParam String faculty, @RequestParam String specialization,
                                 @RequestParam String nameGroup) {
        adminService.addStudent(student,course,faculty,specialization,nameGroup);

        return "redirect:/admin";
    }


    @GetMapping("/admin/givecur/{user}")
    public String addCurator(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        return "set-curator";
    }

    @PostMapping("/admin/givecur/{user}")
    public String addCuratorPost(@PathVariable User user, @RequestParam String nameGroup, @RequestParam String faculty, @RequestParam String lesson, @RequestParam String course, @RequestParam String specialization) {
       adminService.addCurator(user, faculty,nameGroup,course,lesson,specialization);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete/{user}")
    public String userPost(@PathVariable User user) {
        userRepository.delete(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/addstudents")
    public String addStudents(Model model) {
        return "add-students";
    }

    @PostMapping("/admin/addstudents")
    public String addStudentsPost(Model model, @RequestParam String students) {
        String[] studentsMassive = students.split(", ");
        for (String studentWithoutMassive : studentsMassive) {
            String[] massiveByOneStudent = studentWithoutMassive.split(" ");
            String name = massiveByOneStudent[0];
            String surname = massiveByOneStudent[1];
            adminService.addAccount(name,surname);
        }
        return "redirect:/admin/students/data";
    }

    @PostMapping("/admin/students/data")
    public String postData() {
        builder.deleteAll();
        return "redirect:/admin";
    }

    @GetMapping("/admin/students/data")
    public String getData(Model model) {
        model.addAttribute("data", builder.findAll());
        return "data-students";
    }

}
