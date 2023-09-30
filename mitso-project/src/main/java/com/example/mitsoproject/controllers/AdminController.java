package com.example.mitsoproject.controllers;

import com.example.mitsoproject.config.SpringSecurityConfig;
import com.example.mitsoproject.models.DataStudents;
import com.example.mitsoproject.models.Role;
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

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final AdminService adminService;
    private final DataStudentsRepository builder;
    private final StudentsRepository studentsRepository;
    private final AdminRepository adminRepository;
//    private final CuratorRepository curatorRepository;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin";
    }

    @PostMapping("/admin/giveadmin/{user}")
    public String adminPost(@PathVariable Admin user) {
        user.getUser().getRoles().clear();
        user.getUser().getRoles().add(Role.ROLE_ADMIN);
        adminRepository.save(user);
        return "redirect:/admin";
    }


    @GetMapping("/admin/givestudent/{user}")
    public String setStudent(Model model, @PathVariable Student user) {
        model.addAttribute("studentName", user.getName());
        model.addAttribute("studentSurname", user.getSurname());
        return "set-student";
    }

    @PostMapping("/admin/givestudent/{student}")
    public String setStudentPost(@PathVariable Student student, @RequestParam String course,
                                 @RequestParam String faculty, @RequestParam String specialization,
                                 @RequestParam String nameGroup) {
        student.setCourse(course);
        student.setFaculty(faculty);
        student.setSpecialization(specialization);
        student.setNameGroupe(nameGroup);
        student.getRoles().clear();
        student.getRoles().add(Role.ROLE_STUDENT);
        studentsRepository.save(student);
        return "redirect:/admin";
    }


    @GetMapping("/admin/givecur/{user}")
    public String addCurator(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        return "set-curator";
    }

//    @PostMapping("/admin/givecur/{user}")
//    public String addCuratorPost(@PathVariable Curator user, @RequestParam String nameGroup, @RequestParam String faculty, @RequestParam String lesson) {
//        user.getRoles().clear();
//        user.getRoles().add(Role.ROLE_CURATOR);
//        user.setFaculty(faculty);
//        user.setNameGroupe(nameGroup);
//        user.setLesson(lesson);
//        curatorRepository.save(user);
//        return "redirect:/admin";
//    }

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
            Student student = new Student();
            student.setName(massiveByOneStudent[0]);
            student.setSurname(massiveByOneStudent[1]);

            String password = adminService.alphaNumericString(10);
            student.setUsername("student-" + adminService.alphaNumericString(4));
            student.setPassword(SpringSecurityConfig.passwordEncoder().encode(password));

            student.setEmail(adminService.alphaNumericString(10) + "@gmail.com");
            student.setRoles(Set.of(Role.ROLE_STUDENT));

            studentsRepository.save(student);

            DataStudents data = new DataStudents();
            data.setData(student.getName() + " " + student.getSurname() + " Данные:  login: \"" + student.getUsername() + "\". Password: \"" + password + "\".");
            builder.save(data);
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
