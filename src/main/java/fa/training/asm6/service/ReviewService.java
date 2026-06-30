package fa.training.asm6.service;

import fa.training.asm6.dto.request.ReviewRequest;
import fa.training.asm6.dto.response.ManageReviewsResponse;
import fa.training.asm6.entity.Course;
import fa.training.asm6.entity.Review;
import fa.training.asm6.repository.CourseRepository;
import fa.training.asm6.repository.ReviewRepository;
import fa.training.asm6.service.base.GenericServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService extends GenericServiceImpl<Review, Integer> {

    private final CourseRepository courseRepository;

    public ReviewService(ReviewRepository repository, CourseRepository courseRepository) {
        super(repository);
        this.courseRepository = courseRepository;
    }

    public Review saveReview(ReviewRequest reviewRequest){
        Course course = courseRepository.findById(reviewRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + reviewRequest.getCourseId()));
        Review review = Review.builder()
                .authorName(reviewRequest.getAuthorName())
                .email(reviewRequest.getEmail())
                .content(reviewRequest.getContent())
                .rating(reviewRequest.getRating())
                .status(reviewRequest.getStatus())
                .course(course)
                .build();
        return super.save(review);
    }

    public List<Review> findMostRecentReviews(int offset, int limit){
        PageRequest pageable = PageRequest.of(offset, limit);
        List<Review> reviews = ((ReviewRepository) repository).findMostRecentReviews(pageable).getContent();
        return reviews;
    }

    public ManageReviewsResponse findReviewsWithStatus(){
        List<Review> pending = ((ReviewRepository) repository).findReviewByStatusWithCourse(1);
        List<Review> approved = ((ReviewRepository) repository).findReviewByStatusWithCourse(2);
        return new ManageReviewsResponse(pending, approved);
    }

    public void approveReview(Integer reviewId){
        Review review = repository.findById(reviewId).orElseThrow(
                () -> new RuntimeException("Review not found with id: " + reviewId)
        );
        review.setStatus(2);
        repository.save(review);
    }

    public void rejectReview(Integer reviewId){
        repository.deleteById(reviewId);
    }
}
