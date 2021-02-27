package com.example.PetProject.application.course;

import org.springframework.data.jpa.repository.JpaRepository;

// Course repository for communicating with database
public interface CourseRepository extends JpaRepository<Course, Long> {
}
