package com.coursecatalog.service;

import com.coursecatalog.model.dto.CourseDTO;
import com.coursecatalog.model.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CourseService {
    List<Course> getAllCourses();
    Page<Course> getAllCourses(Pageable pageable);
    Course getCourseById(Long id);
    Course createCourse(CourseDTO productDTO);
    Course updateCourse(Long id, CourseDTO productDTO);
    Course patchCourse(Long id, Map<String, Object> updates);
    void deleteCourse(Long id);
    Course uploadImage(Long id, MultipartFile file);
    Course deleteImage(Long id);
}
