package ru.hogwarts.school.util;

import ru.hogwarts.school.model.Faculty;

public class FacultyFixed {
    public static Faculty getFacultyDefault() {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Blue");
        return faculty;
    }
}
