package courses.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import courses.entity.Courses;

public interface CoursesDao extends JpaRepository<Courses, Long> {
}