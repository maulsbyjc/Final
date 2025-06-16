package courses.controller.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import courses.entity.Courses;
import courses.entity.Enrollment;
import courses.entity.Instructor;
import courses.entity.Student; 

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoursesData {
    private Long courseId;
    private String courseName;
    private String courseCode;
    private String title;
    private String description;
    private int credits;

    private CoursesInstructor instructor;

    private Set<EnrollmentData> enrollments = new HashSet<>();

    public CoursesData(Courses courses) {
        this.courseId = courses.getCourseId();
        this.courseName = courses.getCourseName();
        this.courseCode = courses.getCourseCode();
        this.title = courses.getTitle();
        this.description = courses.getDescription();
        this.credits = courses.getCredits();

        if (courses.getInstructor() != null) {
            this.instructor = new CoursesInstructor(courses.getInstructor());
        }

        if (courses.getEnrollments() != null) {
            for (Enrollment enrollment : courses.getEnrollments()) {
                this.enrollments.add(new EnrollmentData(enrollment));
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class CoursesInstructor {
        private Long instructorId;
        private int employeeId;
        private String department;
        private String firstName;
        private String lastName;
        private String email;

        public CoursesInstructor(Instructor instructor) {
            this.instructorId = instructor.getInstructorId();
            this.firstName = instructor.getFirstName();
            this.lastName = instructor.getLastName();
            this.email = instructor.getEmail();
            this.employeeId = instructor.getEmployeeId();
            this.department = instructor.getDepartment();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            CoursesInstructor that = (CoursesInstructor) obj;
            return Objects.equals(instructorId, that.instructorId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(instructorId);
        }

        public void clear() {
            return;
        }
    }

    @Data
    @NoArgsConstructor
    public static class CoursesStudent {
        private Long studentId;
        private String firstName;
        private String lastName;
        private String email;

        public CoursesStudent(Student student) {
            this.studentId = student.getStudentId();
            this.firstName = student.getFirstName();
            this.lastName = student.getLastName();
            this.email = student.getEmail();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            CoursesStudent that = (CoursesStudent) obj;
            return Objects.equals(studentId, that.studentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(studentId);
        }
    }

    @Data
    @NoArgsConstructor
    public static class EnrollmentData {
        private Long enrollmentId;
        private Long studentId;
        private Long courseId;
        private LocalDate enrollmentDate;
        private String grade;

        public EnrollmentData(Enrollment enrollment) {
            this.enrollmentId = enrollment.getEnrollmentPkId();
            this.enrollmentDate = enrollment.getEnrollmentDate();
            this.grade = enrollment.getGrade();

            if (enrollment.getStudent() != null) {
                this.studentId = enrollment.getStudent().getStudentId();
            }
            if (enrollment.getCourse() != null) {
                this.courseId = enrollment.getCourse().getCourseId();
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            EnrollmentData that = (EnrollmentData) obj;
            return Objects.equals(enrollmentId, that.enrollmentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(enrollmentId);
        }
    }
}