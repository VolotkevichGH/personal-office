package com.example.mitsoproject.controllers;

import com.example.mitsoproject.config.SpringSecurityConfig;
import com.example.mitsoproject.models.Role;
import com.example.mitsoproject.models.people.User;
import com.example.mitsoproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


@Controller
public class MainController {
    PasswordEncoder passwordEncoder;
    List<String> builder = new ArrayList<>();

    public String alphaNumericString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }


//    ===========================================================================================

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Личный кабинет");
        return "home";
    }
    // =========================================================================
    // ==========================---ABITURIENT---===============================
    // =========================================================================

    @GetMapping("/abit")
    public String abitOffice(Model model) {
        return "abit";
    }

    // =========================================================================
    // ============================---CURATOR---================================
    // =========================================================================

    @GetMapping("/curator/profile")
    public String curatorOffice(Model model) {
        String studentName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(studentName).get();
        model.addAttribute("user", user);
        List<User> userList = userRepository.findByRoles(Role.ROLE_STUDENT);
        List<User> userListForCurator = new ArrayList<>();
        for (User user1 : userList) {
            if (user1.getCuratorName().replaceAll("\\s+", "").equals(user.getName().replaceAll("\\s+", "")) &&
                    user1.getCuratorSurname().replaceAll("\\s+", "").equals(user.getSurname().replaceAll("\\s+", ""))) {
                userListForCurator.add(user1);
            }
        }
        model.addAttribute("userList", userListForCurator);
        return "curator";
    }


    @GetMapping("/curator/edit")
    public String curatorEdit(Model model) {
        String studentName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user1 = userRepository.findByUsername(studentName).get();
        model.addAttribute("user", user1);
        return "edit-profile-curator";
    }

    @PostMapping("/curator/edit")
    public String curatorOfficePost(Model model, @RequestParam String username, @RequestParam String email, @RequestParam String oldpassword, @RequestParam String password) {
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


    // =========================================================================
    // ===========================---AUTH/REG---================================
    // =========================================================================


    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }


    // =========================================================================
    // ============================---STUDENT---================================
    // =========================================================================

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
        return "student";
    }


    // =========================================================================
    // ========================---ADMINISTRATION---=============================
    // =========================================================================


    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin";
    }

    @PostMapping("/admin/giveadmin/{user}")
    public String adminPost(@PathVariable User user) {
        user.getRoles().clear();
        user.getRoles().add(Role.ROLE_ADMIN);
        userRepository.save(user);
        return "redirect:/admin";
    }


    @GetMapping("/admin/givestudent/{user}")
    public String setStudent(Model model, @PathVariable User user) {
        model.addAttribute("studentName", user.getName());
        model.addAttribute("studentSurname", user.getSurname());
        return "set-student";
    }

    @PostMapping("/admin/givestudent/{user}")
    public String setStudentPost(@PathVariable User user, @RequestParam String course, @RequestParam String faculty, @RequestParam String specialization, @RequestParam String curatorName,
                                 @RequestParam String curatorSurname, @RequestParam String nameGroup) {
        user.setCourse(course);
        user.setCuratorName(curatorName);
        user.setCuratorSurname(curatorSurname);
        user.setFaculty(faculty);
        user.setSpecialization(specialization);
        user.setNameGroupe(nameGroup);
        user.getRoles().clear();
        user.getRoles().add(Role.ROLE_STUDENT);
        userRepository.save(user);
        return "redirect:/admin";
    }


    @GetMapping("/admin/givecur/{user}")
    public String addCurator(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        return "set-curator";
    }

    @PostMapping("/admin/givecur/{user}")
    public String addCuratorPost(@PathVariable User user, @RequestParam String nameGroup, @RequestParam String faculty, @RequestParam String lesson) {
        user.getRoles().clear();
        user.getRoles().add(Role.ROLE_CURATOR);
        user.setFaculty(faculty);
        user.setNameGroupe(nameGroup);
        user.setLesson(lesson);
        userRepository.save(user);
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
            User student = new User();
            student.setName(massiveByOneStudent[0]);
            student.setSurname(massiveByOneStudent[1]);

            String password = alphaNumericString(10);
            student.setUsername("student-" + alphaNumericString(4));
            student.setPassword(SpringSecurityConfig.passwordEncoder().encode(password));

            student.setEmail(alphaNumericString(10) + "@gmail.com");
            student.setRoles(Set.of(Role.ROLE_STUDENT));

            userRepository.save(student);

            String data = student.getName() + " " + student.getSurname() + " Данные:  login: \"" + student.getUsername() + "\". Password: \"" + password + "\".";
            builder.add(data);
        }

        return "redirect:/admin/students/data";
    }

    @PostMapping("/admin/students/data")
    public String postData() {
        builder.clear();
        return "redirect:/admin";
    }

    @GetMapping("/admin/students/data")
    public String getData(Model model) {
        model.addAttribute("data", builder);
        return "data-students";
    }

}
