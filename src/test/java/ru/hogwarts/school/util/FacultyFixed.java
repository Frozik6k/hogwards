package ru.hogwarts.school.util;

import org.json.JSONException;
import org.json.JSONObject;
import ru.hogwarts.school.model.Faculty;

public class FacultyFixed {

    public final static Long ID_FACULTY = 1L;
    public final static String NAME_FACULTY = "факультет домашних эльфов";
    public final static String COLOR_FACULTY = "Green";

    public static Faculty getFacultyDefault() {
        Faculty faculty = new Faculty();
        faculty.setId(ID_FACULTY);
        faculty.setName(NAME_FACULTY);
        faculty.setColor(COLOR_FACULTY);
        return faculty;
    }

    public static final String KEY_FACILTY = "faculty";

    public static JSONObject getFacultyObject() throws JSONException {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", ID_FACULTY);
        facultyObject.put("name", NAME_FACULTY);
        facultyObject.put("color", COLOR_FACULTY);
        return facultyObject;
    }
}
