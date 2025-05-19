package ru.hogwarts.school.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {

    private StudentService studentService;

    private AvatarService avatarService;

    public StudentController(StudentService studentService, AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @GetMapping("{id}") // http://localhost:8080/student/43
    public ResponseEntity<Student> getStudentInfo(@PathVariable long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("{id}/faculty")
    public ResponseEntity getFacultyStudent(@PathVariable long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.getFaculty());
    }

    @GetMapping // http://localhost:8080/student
    public ResponseEntity<Collection<Student>> findStudents(@RequestParam(required = false) Integer age,
                                                            @RequestParam(required = false) Integer min,
                                                            @RequestParam(required = false) Integer max) {
        if (age != null) {
            return ResponseEntity.ok(studentService.findByAge(age));
        }

        if (min != null || max != null) {
            return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
        }

        return ResponseEntity.ok(studentService.getAll());
    }


    @PostMapping // http://localhost:8080/student
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @PutMapping // http://localhost:8080/student
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}") // Delete http://localhost:8080/student/43
    public ResponseEntity deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() >= 1024 * 300) {
            return ResponseEntity.badRequest().body("File is too big");
        }

        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar)")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws  IOException {
        Avatar avatar = avatarService.findAvatar(id);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping("count") // http://localhost:8080/student/count

    public ResponseEntity<Integer> getCountStudent() {
        return ResponseEntity.ok(studentService.getCountStudents());
    }

    @GetMapping("age") // http://localhost:8080/student/count
    public ResponseEntity<Integer> getAvgAgeStudents() {
        return ResponseEntity.ok(studentService.getAvgAgeStudents());
    }

    @GetMapping("end5students") // http://localhost:8080/student/end5students
    public ResponseEntity<Collection<Student>> getEndFiveStudents() { return ResponseEntity.ok(studentService.findByEndFiveStudents()); }

}
