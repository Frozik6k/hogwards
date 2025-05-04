package ru.hogwarts.school.util;


import org.json.JSONException;
import org.json.JSONObject;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

public class StudentFixed {

    public final static Long ID_STUDENT = 1L;
    public static final String NAME_STUDENT = "Добби";
    public static final int AGE_STUDENT = 156;

    public static Student getStudentDefault() {
        Student student = new Student();
        student.setId(ID_STUDENT);
        student.setName(NAME_STUDENT);
        student.setAge(AGE_STUDENT);
        return student;
    }

    public static JSONObject getStudentObject() throws JSONException {
        JSONObject objectStudent = new JSONObject();
        objectStudent.put("id", ID_STUDENT);
        objectStudent.put("name", NAME_STUDENT);
        objectStudent.put("age", AGE_STUDENT);
        return objectStudent;
    }
}
