package com.example.mitsoproject.services;

import com.example.mitsoproject.models.data.Group;
import com.example.mitsoproject.models.data.Role;
import com.example.mitsoproject.models.data.Specialization;
import com.example.mitsoproject.models.people.Curator;
import com.example.mitsoproject.models.people.Student;
import com.example.mitsoproject.models.people.User;
import com.example.mitsoproject.repositories.*;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DecanatService {

    SpecializationRepository specializationRepository;
    AdminService adminService;
    UserRepository userRepository;
    StudentsRepository studentsRepository;
    CuratorRepository curatorRepository;
    GroupRepository groupRepository;

    public void addGroup(String name, String course, String specName, String curatorName, String curatorSurname, String students) {

        Group group = new Group();
        group.setName(name);
        group.setCourse(course);
        Specialization specialization = specializationRepository.findByName(specName);
        if (specialization != null) {
            group.setSpecialization(specialization);
        }
        String[] studentsMassive = students.split(", ");
        for (String studentWithoutMassive : studentsMassive) {
            User user = new User();
            Student student = new Student();
            String[] massiveByOneStudent = studentWithoutMassive.split(" ");
            String studentName = massiveByOneStudent[0];
            String studentSurname = massiveByOneStudent[1];
            adminService.addAccount(studentName,studentSurname, user, student);
            student.getUser().setRoles(Set.of(Role.ROLE_STUDENT));
            userRepository.save(user);
            group.getStudents().add(student);
            studentsRepository.save(student);
        }
         User user1 = userRepository.findByName(curatorName).get();
        User user2 = userRepository.findBySurname(curatorSurname).get();
        if (user1.equals(user2)){
            Curator curator = curatorRepository.findByUser(user1).get();
            group.setCurator(curator);
            curator.setGroup(group);
            curatorRepository.save(curator);
        }
        groupRepository.save(group);
    }

}
