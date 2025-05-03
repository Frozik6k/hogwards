package ru.hogwarts.school.util;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

public class StudentFixed {

    public static Student getStudentDefault() {
        Student student = new Student();
        student.setName("Александр");
        student.setAge(19);
        return student;
    }
}
