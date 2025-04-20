package ru.hogwarts.school.model;

import java.util.Objects;

public class Student {
    private long id;
    private String name;
    private int age;

    public Student(String name, int age) {
        this.id = 0;
        this.name = name;
        this.age = age;
    }

    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge(){
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int hashCode(){
        return Objects.hash(getId(), getName(), getAge());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Student student = (Student) object;
        return getId() == student.getId() && getAge() == student.getAge() && Objects.equals(getName(), student.getName());
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
