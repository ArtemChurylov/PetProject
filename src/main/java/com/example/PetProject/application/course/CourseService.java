package com.example.PetProject.application.course;

import com.example.PetProject.security.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void saveCourse(MultipartFile file, Course course) {

        String fileName = file.getOriginalFilename();

        if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
            try {
                course.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            course.setOwner(user);
            courseRepository.save(course);
        } else {
            throw new IllegalStateException("Invalid type of file");
        }
    }


    public void editCourse(MultipartFile file, Course course, Long id) {

        Course tempCourse = courseRepository.findById(id).orElseThrow();

        tempCourse.setTitle(course.getTitle());
        tempCourse.setDescription(course.getDescription());
        tempCourse.setPrice(course.getPrice());

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Course course1 = user.getCreatedCourses().stream().filter(course2 -> course2.getId().equals(tempCourse.getId())).findFirst().orElseThrow();
        course1.setTitle(tempCourse.getTitle());
        course1.setDescription(tempCourse.getDescription());
        course1.setPrice(tempCourse.getPrice());

        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();

            if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {

                try {
                    tempCourse.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
                    course1.setImage(tempCourse.getImage());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                throw new IllegalStateException("Invalid type of file");
            }
        }

        courseRepository.save(tempCourse);
    }
}
