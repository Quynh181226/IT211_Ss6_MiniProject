package com.coursecatalog.service.impl;


import com.coursecatalog.model.dto.CourseDTO;
import com.coursecatalog.model.entity.Course;
import com.coursecatalog.repository.CourseRepository;
import com.coursecatalog.service.CourseService;
import com.coursecatalog.service.FileStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final FileStorageService fileStorageService;

    public CourseServiceImpl(CourseRepository courseRepository, FileStorageService fileStorageService) {
        this.courseRepository = courseRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khoá học có id: " + id));
    }

    @Override
    public Course createCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setPrice(courseDTO.getPrice());
        course.setImageUrl(courseDTO.getImageUrl());
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Long id, CourseDTO courseDTO) {
        Course course = getCourseById(id);
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setPrice(courseDTO.getPrice());
        course.setImageUrl(courseDTO.getImageUrl());
        return courseRepository.save(course);
    }

    @Override
    public Course patchCourse(Long id, Map<String, Object> updates) {
        Course course = getCourseById(id);

        if (updates.containsKey("name")) {
            course.setName((String) updates.get("name"));
        }

        if (updates.containsKey("price")) {
            Object priceObj = updates.get("price");
            if (priceObj instanceof Number) {
                course.setPrice(((Number) priceObj).doubleValue());
            }
        }

        if (updates.containsKey("description")) {
            course.setDescription((String) updates.get("description"));
        }

        if (updates.containsKey("imageUrl")) {
            course.setImageUrl((String) updates.get("imageUrl"));
        }

        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        // Xóa file vật lý trước nếu khóa học có ảnh
        if (course.getImageUrl() != null) {
            fileStorageService.deleteFile(course.getImageUrl());
        }
        courseRepository.delete(course);
    }

    @Override
    public Course uploadImage(Long id, MultipartFile file) {
        Course course = getCourseById(id);

        if (course.getImageUrl() != null) {
            fileStorageService.deleteFile(course.getImageUrl());
        }

        String fileName = fileStorageService.storeFile(file);
        course.setImageUrl(fileName);

        return courseRepository.save(course);
    }

    @Override
    public Course deleteImage(Long id) {
        Course course = getCourseById(id);

        if (course.getImageUrl() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khóa học này chưa có ảnh để xóa!");
        }

        fileStorageService.deleteFile(course.getImageUrl());
        course.setImageUrl(null);

        return courseRepository.save(course);
    }

}