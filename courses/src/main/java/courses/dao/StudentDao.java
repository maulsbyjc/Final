package courses.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import courses.entity.Student;

public interface StudentDao extends JpaRepository<Student, Long> {
}