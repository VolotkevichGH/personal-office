package com.example.mitsoproject.services;

import com.example.mitsoproject.config.SpringSecurityConfig;
import com.example.mitsoproject.models.data.*;
import com.example.mitsoproject.models.people.*;
import com.example.mitsoproject.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final CuratorRepository curatorRepository;
    private final StudentsRepository studentsRepository;
    private final AdminRepository adminRepository;
    private final DataStudentsRepository builder;
    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final DecanatRepository decanatRepository;
    private final FacultyRepository facultyRepository;


    public String alphaNumericString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public void addStudent(Student student, String course, Faculty faculty, Specialization specialization, Group group) {
        student.getGroup().setCourse(course);
        student.getGroup().getSpecialization().setFaculty(faculty);
        student.getGroup().setSpecialization(specialization);
        student.setGroup(group);
        student.getUser().getRoles().clear();
        student.getUser().getRoles().add(Role.ROLE_STUDENT);
        studentsRepository.save(student);
    }

    public void addAdmin(User user) {
        Admin admin = new Admin();
        admin.setUser(user);
        if (user.getRoles().contains(Role.ROLE_STUDENT)) {
            Student student = studentsRepository.findByUser(user).get();
            user.getRoles().remove(Role.ROLE_STUDENT);
            studentsRepository.delete(student);
        } else if (user.getRoles().contains(Role.ROLE_CURATOR)) {
            Curator curator = curatorRepository.findByUser(user).get();
            user.getRoles().remove(Role.ROLE_CURATOR);
            curatorRepository.delete(curator);
        } else if (user.getRoles().contains(Role.ROLE_TEACHER)) {
            Teacher teacher = teacherRepository.findByUser(user).get();
            user.getRoles().remove(Role.ROLE_TEACHER);
            teacherRepository.delete(teacher);
        }
        user.getRoles().clear();
        user.getRoles().add(Role.ROLE_ADMIN);

        adminRepository.save(admin);
        userRepository.save(user);
    }

    public void addCurator(User user, Group nameGroup) {
        Curator curator = new Curator();
        curator.setUser(user);
        if (user.getRoles().contains(Role.ROLE_TEACHER)) {
            user.getRoles().add(Role.ROLE_CURATOR);
        } else {
            curator.getUser().getRoles().clear();
            curator.getUser().getRoles().add(Role.ROLE_CURATOR);
        }
        Student student = studentsRepository.findByUser(user).get();
        if (student != null) {
            studentsRepository.delete(student);
        }
        curator.setGroup(nameGroup);
        userRepository.save(curator.getUser());
        curatorRepository.save(curator);
    }

    public void addTeacher(User user, Faculty faculty, Lesson lesson) {
        Teacher teacher = new Teacher();
        teacher.setUser(user);
        if (teacher.getUser().getRoles().contains(Role.ROLE_CURATOR)) {
            teacher.getUser().getRoles().add(Role.ROLE_TEACHER);
        } else {
            teacher.getUser().getRoles().clear();
            teacher.getUser().getRoles().add(Role.ROLE_TEACHER);
        }
        Student student = studentsRepository.findByUser(user).get();
        if (student != null) {
            studentsRepository.delete(student);
        }
        teacher.getFaculty().add(faculty);
        teacher.getLesson().add(lesson);
        userRepository.save(teacher.getUser());
        teacherRepository.save(teacher);
    }

    public void addAccount(String name, String surname, User user, Student student) {
        student.setUser(user);
        student.getUser().setName(name);
        student.getUser().setSurname(surname);

        String password = alphaNumericString(10);
        student.getUser().setUsername("student-" + alphaNumericString(4));
        student.getUser().setPassword(SpringSecurityConfig.passwordEncoder().encode(password));

        student.getUser().setEmail(alphaNumericString(10) + "@gmail.com");

        DataStudents data = new DataStudents();
        data.setData(student.getUser().getName() + " " + student.getUser().getSurname() + " Данные:  login: \"" + student.getUser().getUsername() + "\". Password: \"" + password + "\".");
        builder.save(data);
    }

    public void deleteAccount(User user) {
        if (user.getRoles().contains(Role.ROLE_TEACHER)) {
            Teacher teacher = teacherRepository.findByUser(user).get();
            teacher.getUser().getRoles().remove(Role.ROLE_TEACHER);
            teacherRepository.delete(teacher);
            userRepository.save(teacher.getUser());
            userRepository.delete(user);
        } else if (user.getRoles().contains(Role.ROLE_CURATOR)) {
            Curator curator = curatorRepository.findByUser(user).get();
            curator.getUser().getRoles().remove(Role.ROLE_CURATOR);
            if (!curator.getGroup().getStudents().isEmpty()) {
                Set<Student> students = curator.getGroup().getStudents().stream().collect(Collectors.toSet());
                curator.getGroup().getStudents().removeAll(students);
                for (Student student : students) {
                    student.getGroup().setCurator(null);
                    studentsRepository.save(student);
                }
            }
            userRepository.save(curator.getUser());
            curatorRepository.delete(curator);
            userRepository.delete(user);
        } else if (user.getRoles().contains(Role.ROLE_STUDENT)) {
            Student student = studentsRepository.findByUser(user).get();
            student.getUser().getRoles().remove(Role.ROLE_STUDENT);
            userRepository.save(student.getUser());
            if (student.getGroup().getCurator() != null) {
                Curator curator = student.getGroup().getCurator();
                student.getGroup().setCurator(null);
                studentsRepository.save(student);
                curator.getGroup().getStudents().remove(student);
                curatorRepository.save(curator);
            }
            studentsRepository.delete(student);
            userRepository.delete(user);
        } else if (user.getRoles().contains(Role.ROLE_ADMIN)) {
            Admin admin = adminRepository.findByUser(user).get();
            admin.getUser().getRoles().remove(Role.ROLE_ADMIN);
            userRepository.save(admin.getUser());
            adminRepository.delete(admin);
            userRepository.delete(user);
        } else if (user.getRoles().contains(Role.ROLE_DECAN)) {
            user.getRoles().clear();
            userRepository.save(user);
            boolean hasDecanat = decanatRepository.findByUser(user).isPresent();
            if (hasDecanat) {
                Decanat decanat = decanatRepository.findByUser(user).get();
                if (decanat.getFaculty() != null) {
                    decanat.getFaculty().setDecanat(null);
                    facultyRepository.save(decanat.getFaculty());
                    decanat.setFaculty(null);
                    decanatRepository.delete(decanat);
                }
            }
            userRepository.delete(user);
        } else {
            userRepository.delete(user);
        }

    }


    public void addDecan(User user, Faculty faculty) {

        if (user.getRoles().contains(Role.ROLE_STUDENT)) {
            user.getRoles().clear();
            user.getRoles().add(Role.ROLE_DECAN);
            userRepository.save(user);
            Student student = studentsRepository.findByUser(user).get();
            if (student != null) {
                Group group = student.getGroup();
                if (group != null) {
                    group.getStudents().remove(student);
                    groupRepository.save(group);
                }
                studentsRepository.delete(student);
            }
        } else if (user.getRoles().contains(Role.ROLE_TEACHER)) {
            user.getRoles().clear();
            user.getRoles().add(Role.ROLE_DECAN);
            userRepository.save(user);
            Teacher teacher = teacherRepository.findByUser(user).get();
            if (teacher != null) {
                teacher.getLesson().clear();
                teacherRepository.delete(teacher);
                Set<Lesson> lessons = teacher.getLesson().stream().collect(Collectors.toSet());
                for (Lesson lesson : lessons) {
                    lesson.getTeachers().remove(teacher);
                    lessonRepository.save(lesson);
                }

            }
        } else if (user.getRoles().contains(Role.ROLE_ADMIN)) {
            user.getRoles().clear();
            user.getRoles().add(Role.ROLE_DECAN);
            userRepository.save(user);
            Admin admin = adminRepository.findByUser(user).get();
            if (admin != null) {
                adminRepository.delete(admin);
            }
        } else if (user.getRoles().contains(Role.ROLE_CURATOR)) {
            user.getRoles().clear();
            user.getRoles().add(Role.ROLE_DECAN);
            userRepository.save(user);
            Curator curator = curatorRepository.findByUser(user).get();
            if (curator != null) {
                curatorRepository.delete(curator);
                Group group = curator.getGroup();
                group.setCurator(null);
                groupRepository.save(group);
            }
        }

        Decanat decanat = new Decanat();
        decanat.setFaculty(faculty);
        decanat.setUser(user);
        faculty.setDecanat(decanat);
        facultyRepository.save(faculty);
        decanatRepository.save(decanat);
    }
}
