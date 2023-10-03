package com.example.mitsoproject.controllers;

import com.example.mitsoproject.models.data.*;
import com.example.mitsoproject.models.people.*;
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
    private final CuratorRepository curatorRepository;
    private final TeacherRepository teacherRepository;
    private final FacultyRepository facultyRepository;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin";
    }

    @PostMapping("/admin/giveadmin/{user}")
    public String adminPost(@PathVariable User user) {
        adminService.addAdmin(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/givedecan/{user}")
    public String decan(Model model, @PathVariable User user) {
        model.addAttribute("user", user);
        return "add-decan";
    }

    @GetMapping("/admin/addfaculty")
    public String addFaculty(Model model) {
        return "add-faculty";
    }

    @PostMapping("/admin/addfaculty")
    public String addFacultyPost(Model model, String facultyName) {
        Faculty faculty = new Faculty();
        faculty.setName(facultyName);
        facultyRepository.save(faculty);
        return "redirect:/admin";
    }


    @PostMapping("/admin/givedecan/{user}")
    public String decanPost(@PathVariable User user, @RequestParam String facultyName) {
        boolean hasfaculty = facultyRepository.findByName(facultyName).isPresent();
        Faculty faculty = facultyRepository.findByName(facultyName).get();
        if (hasfaculty) {
            adminService.addDecan(user, faculty);
        }
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
                                 @RequestParam Faculty faculty, @RequestParam Specialization specialization,
                                 @RequestParam Group nameGroup) {
        adminService.addStudent(student, course, faculty, specialization, nameGroup);

        return "redirect:/admin";
    }


    @GetMapping("/admin/givecur/{user}")
    public String addCurator(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        return "set-curator";
    }

    @PostMapping("/admin/givecur/{user}")
    public String addCuratorPost(@PathVariable User user, @RequestParam Group nameGroup, @RequestParam Specialization specialization) {
        adminService.addCurator(user, nameGroup);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete/{user}")
    public String deleteUserPost(@PathVariable User user) {
        adminService.deleteAccount(user);
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
            User user = new User();
            Student student = new Student();
            String[] massiveByOneStudent = studentWithoutMassive.split(" ");
            String name = massiveByOneStudent[0];
            String surname = massiveByOneStudent[1];
            adminService.addAccount(name, surname, user, student);
            student.getUser().setRoles(Set.of(Role.ROLE_STUDENT));
            userRepository.save(user);
            studentsRepository.save(student);
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

    @GetMapping("/admin/giveteacher/{user}")
    public String addTeacher(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        return "set-teacher";
    }

    @PostMapping("/admin/giveteacher/{user}")
    public String addTeacherPost(@PathVariable User user, @RequestParam Faculty faculty, @RequestParam Lesson lesson) {
        adminService.addTeacher(user, faculty, lesson);
        return "redirect:/admin";
    }

}
