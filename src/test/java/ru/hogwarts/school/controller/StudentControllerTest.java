package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.util.FacultyFixed;
import ru.hogwarts.school.util.StudentFixed;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    private String host;

    @Autowired
    private StudentController studentController;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    private Student student;

    @BeforeEach
    void setUp() {
        host = "http://localhost:" + port;
        Faculty faculty =
                this.restTemplate.postForObject(host + "/faculty", FacultyFixed.getFacultyDefault(), Faculty.class);
        Student student = StudentFixed.getStudentDefault();
        student.setFaculty(faculty);
        this.student = this.restTemplate.postForObject(host + "/student", student, Student.class);
    }



    @Test
    void testCreateStudent() throws Exception {

        Assertions
                .assertThat(student)
                .isNotNull();
        Assertions
                .assertThat(this.restTemplate.getForObject(host + "/student/" + student.getId(), Student.class).equals(student))
                .isTrue();

    }

    @Test
    void testEditStudent() throws Exception {
        System.out.println(student);
        String nameStudent = "Айтишник";
        student.setName(nameStudent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> entity = new HttpEntity<>(student, headers);
        ResponseEntity<Student> response = restTemplate.exchange(
                host + "/student",
                HttpMethod.PUT,
                entity,
                Student.class);

        Assertions
                .assertThat(response.getStatusCode().is2xxSuccessful())
                .isTrue();
        Assertions
                .assertThat(student.equals(response.getBody()))
                .isTrue();
    }

    @Test
    void testGetStudentInfo() throws Exception {

        Student studentGet = this.restTemplate.getForObject(host + "/student/" + student.getId(), Student.class);

        Assertions
                .assertThat(student.equals(studentGet))
                .isTrue();
    }

    @Test
    void testFindStudents() throws Exception {

        Collection<Student> students = this.restTemplate.getForObject(host + "/student", Collection.class);
        Assertions
                .assertThat(students.stream().count() >= 1)
                .isTrue();
    }

    @Test
    void testGetFacultyStudent() throws Exception {

        Assertions
                .assertThat(this.restTemplate.getForObject(host + "/student/" + student.getId() + "/faculty", Faculty.class)
                        .equals(student.getFaculty()))
                .isTrue();
    }

    @Test
    void testDeleteStudent() throws Exception {
        restTemplate.delete(host + "/student/" + student.getId());

        Assertions
                .assertThat(this.restTemplate.getForEntity(host + "/student/" + student.getId(), Object.class).getStatusCode().is2xxSuccessful())
                .isFalse();
    }
}
