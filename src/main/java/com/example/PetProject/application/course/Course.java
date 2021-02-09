package com.example.PetProject.application.course;

import com.example.PetProject.application.course.validation.Price;
import com.example.PetProject.security.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "curse_sequence")
    @SequenceGenerator(name = "curse_sequence", sequenceName = "curse_sequence", allocationSize = 1)
    @Column(name = "course_id")
    private Long id;

    @NotEmpty(message = "Can`t be empty")
    @Size(min = 5, max = 33, message = "Size must be between 5 and 33")
    @Column(name = "title")
    private String title;

    @Size(max = 100, message = "Max size is 100 characters")
    @Column(name = "description")
    private String description;

    @Price
    @NotEmpty(message = "Can`t be empty")
    @Column(name = "price")
    private String price;

    @Lob
    @Column(name = "image")
    private String image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "course_user",
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    )
    private List<User> users;

    @ManyToOne
    @JoinTable(
            name = "course_owner",
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    )
    private User owner;

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        List<Course> courses = owner.getCreatedCourses();
        courses.add(this);
        owner.setCreatedCourses(courses);
        this.owner = owner;
    }

}
