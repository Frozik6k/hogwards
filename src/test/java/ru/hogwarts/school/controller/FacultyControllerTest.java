package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.util.FacultyFixed;
import ru.hogwarts.school.util.StudentFixed;

import org.springframework.http.*;
import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    @LocalServerPort
    private int port;

    private String host;

    @Autowired
    private StudentController studentController;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    private Faculty faculty;

    @BeforeEach
    void setUp() {
        host = "http://localhost:" + port;
        this.faculty =
                this.restTemplate.postForObject(host + "/faculty", FacultyFixed.getFacultyDefault(), Faculty.class);
        Student student = StudentFixed.getStudentDefault();
        student.setFaculty(this.faculty);
        this.restTemplate.postForObject(host + "/student", student, Student.class);

    }

    @Test
    void testGetFacultyInfo() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject(host + "/faculty/" + faculty.getId(), Faculty.class).equals(faculty))
                .isTrue();
    }

    @Test
    void testGetFacultyStudents() throws  Exception {

        Collection<Student> students = this.restTemplate.getForObject(host + "/faculty/" + faculty.getId() + "/students", Collection.class);

        Assertions
                .assertThat(students.stream().count() >= 1)
                .isTrue();
    }

    @Test
    void testFindFaculties() throws Exception {
        Collection<Faculty> faculties = this.restTemplate.getForObject(host + "/faculty", Collection.class);

        Assertions
                .assertThat(faculties.stream().count() >= 1)
                .isTrue();
    }

    @Test
    void testCreateFaculty() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject(host + "/faculty/" + faculty.getId(), Faculty.class).equals(faculty));
    }

    @Test
    void testEditFaculty() {
        faculty.setName("Шарашкина контора");
        faculty.setColor("Black");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Faculty> entity = new HttpEntity<>(faculty, headers);
        ResponseEntity<Faculty> response = restTemplate.exchange(
                host + "/faculty",
                HttpMethod.PUT,
                entity,
                Faculty.class);

        Assertions
                .assertThat(response.getStatusCode().is2xxSuccessful())
                .isTrue();
        Assertions
                .assertThat(faculty.equals(response.getBody()));


    }

    @Test
    void testDeleteFaculty() {
        restTemplate.delete(host + "/faculty/" + faculty.getId());
        Assertions
                .assertThat(this.restTemplate.getForEntity(host + "/faculty/" + faculty.getId(), Object.class).getStatusCode().is2xxSuccessful())
                .isFalse();
    }


}
