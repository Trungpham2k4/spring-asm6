package fa.training.asm6.dto.response;

import fa.training.asm6.entity.Category;
import fa.training.asm6.entity.Course;
import org.springframework.data.domain.Page;

import java.util.List;

public record CategoryPageResponse(List<Category> categories, Page<Course> courses) {
}
