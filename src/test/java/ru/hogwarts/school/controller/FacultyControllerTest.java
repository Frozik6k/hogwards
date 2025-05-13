package ru.hogwarts.school.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
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
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController studentController;

    private JSONObject facultyObject;

    private Faculty faculty;

    private Student student;

    @BeforeEach
    public void setUp() throws JSONException {

        facultyObject = FacultyFixed.getFacultyObject();
        faculty = FacultyFixed.getFacultyDefault();

        student = StudentFixed.getStudentDefault();
        student.setFaculty(faculty);
    }

    @Test
    public void testGetFacultyInfo() throws Exception {

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FacultyFixed.ID_FACULTY))
                .andExpect(jsonPath("$.name").value(FacultyFixed.NAME_FACULTY))
                .andExpect(jsonPath("$.color").value(FacultyFixed.COLOR_FACULTY));
    }

    @Test
    public void testGetFacultyStudents() throws Exception {

        faculty.addStudent(student);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + faculty.getId() + "/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(StudentFixed.ID_STUDENT))
                .andExpect(jsonPath("$[0].name").value(StudentFixed.NAME_STUDENT))
                .andExpect(jsonPath("$[0].age").value(StudentFixed.AGE_STUDENT));
    }

    @Test
    public void testFindFaculties() throws Exception {

        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);

        when(facultyRepository.findAll()).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(FacultyFixed.ID_FACULTY))
                .andExpect(jsonPath("$[0].name").value(FacultyFixed.NAME_FACULTY))
                .andExpect(jsonPath("$[0].color").value(FacultyFixed.COLOR_FACULTY));
    }

    @Test
    public void testCreateFaculty() throws Exception {

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FacultyFixed.ID_FACULTY))
                .andExpect(jsonPath("$.name").value(FacultyFixed.NAME_FACULTY))
                .andExpect(jsonPath("$.color").value(FacultyFixed.COLOR_FACULTY));

    }

    @Test
    public void testEditFaculty() throws Exception {

        String color = "White";
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FacultyFixed.ID_FACULTY))
                .andExpect(jsonPath("$.name").value(FacultyFixed.NAME_FACULTY))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        Long id = faculty.getId();
        faculty = null;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
