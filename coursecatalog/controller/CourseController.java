package com.coursecatalog.controller;

import com.coursecatalog.model.dto.ApiDataResponse;
import com.coursecatalog.model.dto.CourseDTO;
import com.coursecatalog.model.entity.Course;
import com.coursecatalog.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<ApiDataResponse<Page<Course>>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        String sortField = sort[0];
        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<Course> courses = courseService.getAllCourses(pageable);

        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Lấy danh sách khoá học thành công!",
                courses,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Course>> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return new ResponseEntity<>(new ApiDataResponse<>(
                    true,
                    "Lấy khoá học với id thành công!",
                    course,
                    HttpStatus.OK
            ), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ApiDataResponse<>(
                    false,
                    e.getMessage(),
                    null,
                    HttpStatus.BAD_REQUEST
            ), HttpStatus.BAD_REQUEST);

        }

    }

    @PostMapping
    public ResponseEntity<ApiDataResponse<Course>> createCourse(@Valid @RequestBody CourseDTO courseDTO, BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return new ResponseEntity<>(new ApiDataResponse<>(
                    false,
                    errorMsg,
                    null,
                    HttpStatus.BAD_REQUEST
            ), HttpStatus.BAD_REQUEST);
        }

        Course createdCourse = courseService.createCourse(courseDTO);
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Tạo mới khoá học thành công!",
                createdCourse,
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Course>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseDTO courseDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return new ResponseEntity<>(new ApiDataResponse<>(
                    false,
                    errorMsg,
                    null,
                    HttpStatus.BAD_REQUEST
            ), HttpStatus.BAD_REQUEST);
        }
        try {
            Course updatedCourse = courseService.updateCourse(id, courseDTO);
            return new ResponseEntity<>(new ApiDataResponse<>(
                    true,
                    "Cập nhật thông tin khoá học thành công!",
                    updatedCourse,
                    HttpStatus.OK
            ), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ApiDataResponse<>(
                    true,
                    e.getMessage(),
                    null,
                    HttpStatus.NOT_FOUND
            ), HttpStatus.NOT_FOUND);
        }

    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Course>> patchCourse(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        try {
            Course patchedCourse = courseService.patchCourse(id, updates);
            return new ResponseEntity<>(new ApiDataResponse<>(
                    true,
                    "Cập nhật một phần khoá học thành công!",
                    patchedCourse,
                    HttpStatus.OK
            ), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ApiDataResponse<>(
                    true,
                    e.getMessage(),
                    null,
                    HttpStatus.NOT_FOUND
            ), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Void>> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return new ResponseEntity<>(new ApiDataResponse<>(
                    true,
                    "Xoá khoá học thành công!",
                    null,
                    HttpStatus.NO_CONTENT
            ), HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ApiDataResponse<>(
                    true,
                    e.getMessage(),
                    null,
                    HttpStatus.NOT_FOUND
            ), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<ApiDataResponse<Course>> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            Course course = courseService.uploadImage(id, file);
            return new ResponseEntity<>(new ApiDataResponse<>(
                    true,
                    "Upload ảnh khóa học thành công!",
                    course,
                    HttpStatus.OK
            ), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ApiDataResponse<>(
                    false,
                    e.getReason(),
                    null,
                    HttpStatus.valueOf(e.getStatusCode().value())
            ), e.getStatusCode());
        }
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<ApiDataResponse<Course>> deleteImage(@PathVariable Long id) {
        try {
            Course course = courseService.deleteImage(id);
            return new ResponseEntity<>(new ApiDataResponse<>(
                    true,
                    "Xóa ảnh khóa học thành công!",
                    course,
                    HttpStatus.OK
            ), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ApiDataResponse<>(
                    false,
                    e.getReason(),
                    null,
                    HttpStatus.valueOf(e.getStatusCode().value())
            ), e.getStatusCode());
        }
    }
}