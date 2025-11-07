package org.example.service;

import org.example.dto.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CourseServiceTest {

    private CourseService service;

    @BeforeEach
    void setUp() {
        service = new CourseService();
    }

    @Test
    void addAndGetCourseById() {
        Course c = new Course(1, "Java 21 Mastery", 199.0);
        service.addCourse(c);

        Optional<Course> found = service.getCourseById(1);
        assertTrue(found.isPresent(), "Course should be present");
        assertEquals("Java 21 Mastery", found.get().getName());
        assertEquals(199.0, found.get().getPrice());
    }

    @Test
    void getCourseById_missing_returnsEmpty() {
        assertTrue(service.getCourseById(123).isEmpty());
    }

    @Test
    void updateCourse_success() {
        service.addCourse(new Course(1, "Old", 10.0));
        boolean updated = service.updateCourse(1, new Course(1, "New", 20.0));
        assertTrue(updated, "Update should return true");

        Optional<Course> found = service.getCourseById(1);
        assertTrue(found.isPresent());
        assertEquals("New", found.get().getName());
        assertEquals(20.0, found.get().getPrice());
    }

    @Test
    void updateCourse_notFound_returnsFalse() {
        boolean updated = service.updateCourse(999, new Course(999, "X", 1.0));
        assertFalse(updated);
    }

    @Test
    void deleteCourse_successAndIdempotent() {
        service.addCourse(new Course(1, "Delete Me", 10.0));
        assertTrue(service.deleteCourse(1));
        // second time should be false
        assertFalse(service.deleteCourse(1));
    }
}
