package fa.training.asm6.dto.response;

import fa.training.asm6.entity.Category;
import fa.training.asm6.entity.Course;
import fa.training.asm6.entity.Review;
import org.springframework.data.domain.Page;

import java.util.List;

public record HomepageResponse(Page<Course> courses, List<Review> reviews, List<Category> categories) {
}
