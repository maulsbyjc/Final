package courses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import courses.controller.model.CoursesData;
import courses.controller.model.CoursesData.CoursesInstructor;
import courses.controller.model.CoursesData.CoursesStudent;
import courses.service.CoursesService;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/courses")
@Slf4j
public class CoursesController {

    @Autowired
    private CoursesService coursesService;
    
    @PostMapping("/instructor")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CoursesInstructor createInstructor(@RequestBody CoursesInstructor coursesInstructor) {
        log.info("Creating Instructor {}", coursesInstructor);
        return coursesService.saveInstructor(coursesInstructor);
    }

    @PutMapping("/instructor/{instructorId}")
    public CoursesInstructor updateInstructor(@PathVariable Long instructorId, @RequestBody CoursesInstructor coursesInstructor) {
    	coursesInstructor.setInstructorId(instructorId);
        log.info("Updating Instructor {}", coursesInstructor);
        return coursesService.saveInstructor(coursesInstructor);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CoursesData createCourses(@RequestBody CoursesData coursesData) {
        log.info("Creating courses {}", coursesData);
        return coursesService.saveCourses(coursesData);
    }

    @PutMapping("/{courseId}")
    public CoursesData updateCourses(@PathVariable Long courseId, @RequestBody CoursesData coursesData) {
        coursesData.setCourseId(courseId);
        log.info("Updating courses {}", coursesData);
        return coursesService.saveCourses(coursesData);
    }

    @PostMapping("/{courseId}/student")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CoursesStudent addStudentToCourse(@PathVariable Long courseId,
                                             @RequestBody CoursesStudent coursesStudent) {
        log.info("Adding student {} to course with ID={}", coursesStudent, courseId);
        return coursesService.saveStudent(courseId, coursesStudent);
    }
    
    @PatchMapping("/{courseId}/student/{studentId}/grade")
    public Map<String, String> updateStudentGrade(@PathVariable Long courseId,
                                                @PathVariable Long studentId,
                                                @RequestBody Map<String, String> gradeRequest) {
        String grade = gradeRequest.get("grade");
        log.info("Patching grade for student ID {} in course ID {} with grade: {}", studentId, courseId, grade);
        coursesService.updateEnrollmentGrade(courseId, studentId, grade);
        return Map.of("message", "Grade for student ID=" + studentId + " in course ID=" + courseId + " updated successfully to " + grade + ".");
    }

    @GetMapping
    public List<CoursesData> listAllCourses() {
        log.info("Retrieving all courses summary.");
        return coursesService.retrieveAllCourses();
    }

    @GetMapping("/{courseId}")
    public CoursesData retrieveCourseById(@PathVariable Long courseId) {
        log.info("Retrieving course with ID={}", courseId);
        return coursesService.retrieveCoursesById(courseId);
    }

    @DeleteMapping("/{courseId}")
    public Map<String, String> deleteCourseById(@PathVariable Long courseId) {
        log.info("Deleting course with ID={}", courseId);
        coursesService.deleteCoursesById(courseId);
        return Map.of("message", "Deletion of course with ID=" + courseId + " was successful.");
    }
}