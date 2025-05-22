SELECT student.name, student.age, faculty.name as faculty FROM student
INNER JOIN faculty ON student.faculty_id = faculty.id

SELECT student.name, student.age, avatar.data, avatar.file_path FROM avatar
INNER JOIN student ON avatar.student_id = student.id