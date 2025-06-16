package courses.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import courses.entity.Courses;
import courses.entity.Enrollment;
import courses.entity.Instructor;
import courses.entity.Student;
import courses.controller.model.CoursesData;
import courses.controller.model.CoursesData.CoursesInstructor;
import courses.controller.model.CoursesData.CoursesStudent;
import courses.controller.model.CoursesData.EnrollmentData;
import courses.dao.CoursesDao;
import courses.dao.EnrollmentDao;
import courses.dao.InstructorDao;
import courses.dao.StudentDao;

@Service
public class CoursesService {

    @Autowired
    private CoursesDao coursesDao;

    @Autowired
    private InstructorDao instructorDao;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private EnrollmentDao enrollmentDao;

    @Transactional
    public CoursesData saveCourses(CoursesData coursesData) {
        Long courseId = coursesData.getCourseId();
        Courses courses = findOrCreateCourses(courseId);

        copyCoursesFields(courses, coursesData);

        if (coursesData.getInstructor() != null && coursesData.getInstructor().getInstructorId() != null) {
            Instructor instructor = findInstructorById(coursesData.getInstructor().getInstructorId());
            courses.setInstructor(instructor);
        } else if (coursesData.getInstructor() == null) {
            courses.setInstructor(null);
        } else {
            throw new IllegalArgumentException("Instructor ID is required to assign an instructor to a course.");
        }

        if (courseId != null) {
            Set<Enrollment> existingEnrollments = courses.getEnrollments();
            if (existingEnrollments != null) {
                existingEnrollments.clear();
            }
        } else {
            courses.setEnrollments(new HashSet<>());
        }


        if (coursesData.getEnrollments() != null) {
            for (EnrollmentData studentData : coursesData.getEnrollments()) {
                if (studentData.getStudentId() != null) {
                    Student student = findStudentById(studentData.getStudentId());

                    Enrollment enrollment = new Enrollment();
                    enrollment.setCourse(courses);
                    enrollment.setStudent(student);
                    enrollment.setEnrollmentDate(LocalDate.now());
                    courses.getEnrollments().add(enrollment);

                    if (student.getEnrollments() == null) {
                        student.setEnrollments(new HashSet<>());
                    }
                    student.getEnrollments().add(enrollment);

                } else {
                    throw new IllegalArgumentException("Student ID is required to enroll a student in a course.");
                }
            }
        }

        Courses dbCourse = coursesDao.save(courses);
        return new CoursesData(dbCourse);
    }

    private Courses findOrCreateCourses(Long courseId) {
        if (Objects.isNull(courseId)) {
            return new Courses();
        } else {
            return findCourseById(courseId);
        }
    }

    private Courses findCourseById(Long courseId) {
        return coursesDao.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course with ID=" + courseId + " does not exist."));
    }

    private void copyCoursesFields(Courses courses, CoursesData coursesData) {
        courses.setCourseCode(coursesData.getCourseCode());
        courses.setTitle(coursesData.getTitle());
        courses.setDescription(coursesData.getDescription());
        courses.setCourseName(coursesData.getCourseName());
        courses.setCredits(coursesData.getCredits());
        
    }

    @Transactional
    public CoursesInstructor saveInstructor(CoursesInstructor coursesInstructor) {
    	
        Long instructorId = coursesInstructor.getInstructorId();
        Instructor instructor = findOrCreateInstructor(instructorId);

        copyInstructorFields(instructor, coursesInstructor);

        Instructor dbInstructor = instructorDao.save(instructor);

        return new CoursesInstructor(dbInstructor);
    }
    
    private Instructor findOrCreateInstructor(Long instructorId) {

        if (Objects.isNull(instructorId)) {
            return new Instructor();
        } else {
            return findInstructorById(instructorId);
        }
    }

    private Instructor findInstructorById(Long instructorId) {
        return instructorDao.findById(instructorId)
                .orElseThrow(() -> new NoSuchElementException("Instructor with ID=" + instructorId + " does not exist."));
    }

    private void copyInstructorFields(Instructor instructor, CoursesInstructor coursesInstructor) {
        instructor.setFirstName(coursesInstructor.getFirstName());
        instructor.setLastName(coursesInstructor.getLastName());
        instructor.setEmail(coursesInstructor.getEmail());
        instructor.setDepartment(coursesInstructor.getDepartment());
        instructor.setEmployeeId(coursesInstructor.getEmployeeId());
    }

    @Transactional
    public CoursesStudent saveStudent(Long courseId, CoursesStudent coursesStudent) {
        Courses courses = findCourseById(courseId);
        Long studentId = coursesStudent.getStudentId();
        Student student = findOrCreateStudent(studentId);

        copyStudentFields(student, coursesStudent);

        boolean alreadyEnrolled = false;
        if (courses.getEnrollments() != null) {
            for (Enrollment existingEnrollment : courses.getEnrollments()) {
                if (existingEnrollment.getStudent() != null && existingEnrollment.getStudent().getStudentId().equals(student.getStudentId())) {
                    alreadyEnrolled = true;
                    break;
                }
            }
        }

        if (!alreadyEnrolled) {
            Enrollment enrollment = new Enrollment();
            enrollment.setCourse(courses);
            enrollment.setStudent(student);
            enrollment.setEnrollmentDate(LocalDate.now());

            if (courses.getEnrollments() == null) {
                courses.setEnrollments(new HashSet<>());
            }
            courses.getEnrollments().add(enrollment);

            if (student.getEnrollments() == null) {
                student.setEnrollments(new HashSet<>());
            }
            student.getEnrollments().add(enrollment);

            enrollmentDao.save(enrollment);
        }

        Student dbStudent = studentDao.save(student);

        return new CoursesStudent(dbStudent);
    }

    private Student findStudentById(Long studentId) {
        return studentDao.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student with ID=" + studentId + " does not exist."));
    }

    private Student findOrCreateStudent(Long studentId) {
        if (Objects.isNull(studentId)) {
            return new Student();
        } else {
            return findStudentById(studentId);
        }
    }

    private void copyStudentFields(Student student, CoursesStudent coursesStudent) {
        student.setFirstName(coursesStudent.getFirstName());
        student.setLastName(coursesStudent.getLastName());
        student.setEmail(coursesStudent.getEmail());
    }

    @Transactional
    public List<CoursesData> retrieveAllCourses() {
        List<Courses> courses = coursesDao.findAll();
        List<CoursesData> results = new LinkedList<>();

        for (Courses course : courses) {
            CoursesData coursesData = new CoursesData(course);
            results.add(coursesData);
        }

        return results;
    }

    public CoursesData retrieveCoursesById(Long coursesId) {
        Courses courses = findCourseById(coursesId);
        return new CoursesData(courses);
    }

    @Transactional
    public void deleteCoursesById(Long coursesId) {
        Courses courses = findCourseById(coursesId);
        coursesDao.delete(courses);
    }

	public CoursesInstructor findOrCreateInstructor(CoursesInstructor coursesInstructor) {
		// TODO Auto-generated method stub
		return null;
	}

    @Transactional
    public void updateEnrollmentGrade(Long courseId, Long studentId, String grade) {
        Courses course = findCourseById(courseId);

        Student student = findStudentById(studentId);

        Enrollment enrollment = enrollmentDao.findByCourseAndStudent(course, student)
            .orElseThrow(() -> new NoSuchElementException("Enrollment not found for course ID: " + courseId + " and student ID: " + studentId));

        enrollment.setGrade(grade);

        enrollmentDao.save(enrollment);
    }
	
}