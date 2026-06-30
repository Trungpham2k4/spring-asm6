package fa.training.asm6.repository;

import fa.training.asm6.dto.response.CourseWithStatus;
import fa.training.asm6.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT c FROM Course c JOIN Lookup l ON c.status = l.key " +
            "WHERE l.lookupType = 'COURSE_STATUS' AND l.key = 2 " +
            "ORDER BY c.modifiedTime DESC")
    Page<Course> findRecentPublishedCourses(Pageable pageable);

    @Query("SELECT c FROM Course c JOIN Lookup l ON c.status = l.key " +
            "WHERE l.lookupType = 'COURSE_STATUS' AND l.key = 2 ")
    List<Course> findPublishedCourses();

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.reviews WHERE c.id = :courseId")
    Optional<Course> findCourseWithReviews(@Param("courseId") Integer courseId);

    Page<Course> findCourseByCategoryContainingIgnoreCase(String categoryName, Pageable pageable);

    @Query("SELECT new fa.training.asm6.dto.response.CourseWithStatus(c.id, c.title, c.description, c.content, l.value, c.category) " +
            "FROM Course c JOIN Lookup l ON c.status = l.key " +
            "WHERE l.lookupType = 'COURSE_STATUS'")
    Page<CourseWithStatus> findCourseWithStatus(Pageable pageable);
}
