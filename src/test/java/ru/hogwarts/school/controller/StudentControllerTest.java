package ru.hogwarts.school.controller;

import org.hibernate.annotations.Synchronize;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;
import ru.hogwarts.school.util.FacultyFixed;
import ru.hogwarts.school.util.StudentFixed;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private FacultyService facultyService;

    @SpyBean
    private AvatarService avatarService;

    @InjectMocks
    private StudentController studentController;

    private JSONObject studentObject;

    private Student student;

    @BeforeEach
    public void setUp() throws JSONException {
        studentObject = StudentFixed.getStudentObject();
        studentObject.put(FacultyFixed.KEY_FACILTY, FacultyFixed.getFacultyObject());

        student = StudentFixed.getStudentDefault();
        student.setFaculty(FacultyFixed.getFacultyDefault());
    }

    @Test
    public void testGetStudentInfo() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + student.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(StudentFixed.ID_STUDENT))
                .andExpect(jsonPath("$.name").value(StudentFixed.NAME_STUDENT))
                .andExpect(jsonPath("$.age").value(StudentFixed.AGE_STUDENT));
    }

    @Test
    public void testGetFacultyStudent() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + student.getId() + "/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FacultyFixed.ID_FACULTY))
                .andExpect(jsonPath("$.name").value(FacultyFixed.NAME_FACULTY))
                .andExpect(jsonPath("$.color").value(FacultyFixed.COLOR_FACULTY));
    }

    @Test
    public void testFindStudents() throws Exception {

        List<Student> students = new ArrayList<>();
        students.add(student);

        when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(StudentFixed.ID_STUDENT))
                .andExpect(jsonPath("$[0].name").value(StudentFixed.NAME_STUDENT))
                .andExpect(jsonPath("$[0].age").value(StudentFixed.AGE_STUDENT));

    }

    @Test
    public void testCreateStudent() throws Exception {

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(StudentFixed.ID_STUDENT))
                .andExpect(jsonPath("$.name").value(StudentFixed.NAME_STUDENT))
                .andExpect(jsonPath("$.age").value(StudentFixed.AGE_STUDENT));
    }

    @Test
    public void testEditStudent() throws Exception {

        int age = 158;
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(StudentFixed.ID_STUDENT))
                .andExpect(jsonPath("$.name").value(StudentFixed.NAME_STUDENT))
                .andExpect(jsonPath("$.age").value(age));

    }

    @Test
    public void testDeleteStudent() throws Exception {
        Long id = student.getId();
        student = null;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


}
