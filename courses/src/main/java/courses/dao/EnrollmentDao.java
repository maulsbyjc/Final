package courses.dao;

    import java.util.Optional;

    import org.springframework.data.jpa.repository.JpaRepository;
    import courses.entity.Enrollment;
    import courses.entity.Courses;
    import courses.entity.Student;

    public interface EnrollmentDao extends JpaRepository<Enrollment, Long> {
        Optional<Enrollment> findByCourseAndStudent(Courses course, Student student);
    }