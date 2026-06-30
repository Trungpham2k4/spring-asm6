package fa.training.asm6.repository;

import fa.training.asm6.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r JOIN FETCH Course c ON r.course.id = c.id ORDER BY r.createdTime DESC")
    Page<Review> findMostRecentReviews(Pageable pageable);

    @Query("SELECT r FROM Review r JOIN FETCH Course c ON r.course.id = c.id " +
            "WHERE r.status = :status")
    List<Review> findReviewByStatusWithCourse(@Param("status") Integer status);

    int countReviewByStatus(int status);
}
