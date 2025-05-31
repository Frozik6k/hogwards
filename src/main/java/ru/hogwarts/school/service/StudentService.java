package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");

        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for find student");

        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");

        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");

        studentRepository.deleteById(id);
    }

    public Collection<Student> getAll() {
        logger.info("Was invoked method for get all students");

        return studentRepository.findAll();
    }

    public Collection<Student> findByAge(int age) {
        logger.info("Was invoked method for find by age");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find by age between");

        return studentRepository.findByAgeBetween(min, max);
    }

    public Integer getCountStudents() {
        logger.info("Was invoked method for get count students");

        return studentRepository.getCountStudents();
    }

    public Integer getAvgAgeStudents() {
        logger.info("Was invoked method for get middle age students");
        return studentRepository.getAvgAgeStudents();
    }

    public Collection<Student> findByLastFiveStudents() {
        logger.info("Was invoked method for find last five students");

        return studentRepository.findByLastFiveStudents();
    }

    public Collection<String> findNameStudentFirstLetterA() {
        return studentRepository.findAll()
                .stream()
                .parallel()
                .filter(student -> student.getName().charAt(0) == 'А' || student.getName().charAt(0) == 'а')
                .map(student -> student.getName().toUpperCase())
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getMiddleAge() {
        return studentRepository.findAll()
                .stream()
                .parallel()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(0);
    }

    public int getSumIterator() {
        LocalDateTime startDateTime = LocalDateTime.now();
        int sum = (int) LongStream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .sum();


        LocalDateTime endDateTime = LocalDateTime.now();

        long differenceInMillis = ChronoUnit.MILLIS.between(startDateTime, endDateTime);

        logger.info("lead time: " + differenceInMillis + " ms");

        return sum;
    }

    public void getAllStudentPrintParallel() {
        PageRequest pageRequest = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "id"));
        List<String> nameStudents = studentRepository.findAll(pageRequest)
                .map(Student::getName)
                .toList();
        logger.info(nameStudents.get(0));
        logger.info(nameStudents.get(1));

        new Thread(() -> {
            logger.info(nameStudents.get(2));
            logger.info(nameStudents.get(3));
        }).start();

        new Thread(() -> {
            logger.info(nameStudents.get(4));
            logger.info(nameStudents.get(5));
        }).start();
    }



}
