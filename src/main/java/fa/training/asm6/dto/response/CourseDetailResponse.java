package fa.training.asm6.dto.response;

import fa.training.asm6.entity.Course;
import fa.training.asm6.entity.Review;

import java.util.List;

public record CourseDetailResponse(Course course, List<Review> reviews) {
}
