package courses.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import courses.entity.Instructor;

public interface InstructorDao extends JpaRepository<Instructor, Long> {
}