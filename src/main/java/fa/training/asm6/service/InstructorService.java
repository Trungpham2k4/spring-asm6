package fa.training.asm6.service;

import fa.training.asm6.dto.response.InstructorDashboardResponse;
import fa.training.asm6.entity.Instructor;
import fa.training.asm6.repository.CategoryRepository;
import fa.training.asm6.repository.CourseRepository;
import fa.training.asm6.repository.InstructorRepository;
import fa.training.asm6.repository.ReviewRepository;
import fa.training.asm6.service.base.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class InstructorService extends GenericServiceImpl<Instructor, Integer> {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;

    public InstructorService(InstructorRepository repository, CourseRepository courseRepository, CategoryRepository categoryRepository, ReviewRepository reviewRepository) {
        super(repository);
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.reviewRepository = reviewRepository;
    }

    public InstructorDashboardResponse getInstructorDashboardInfo() {
        int totalCourses = (int) courseRepository.count();
        int totalPublishedCourses = courseRepository.findPublishedCourses().size();
        int totalCategories = (int) categoryRepository.count();
        int totalPendingReviews = reviewRepository.countReviewByStatus(1);
        return new InstructorDashboardResponse(totalCourses, totalPublishedCourses, totalPendingReviews, totalCategories);
    }
}
