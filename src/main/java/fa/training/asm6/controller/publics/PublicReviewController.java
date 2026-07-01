package fa.training.asm6.controller.publics;

import fa.training.asm6.dto.request.ReviewRequest;
import fa.training.asm6.dto.response.CourseDetailResponse;
import fa.training.asm6.entity.Course;
import fa.training.asm6.entity.Review;
import fa.training.asm6.service.CourseService;
import fa.training.asm6.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews") // Chuẩn hóa URL số nhiều
@RequiredArgsConstructor
public class PublicReviewController {

    private final ReviewService reviewService;
    private final CourseService courseService; // Bổ sung CourseService để xử lý load lại view khi form lỗi

    @GetMapping
    public String recentReviews(Model model, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int limit) {
        List<Review> reviews = reviewService.findMostRecentReviews(offset, limit);
        model.addAttribute("reviews", reviews);
        return "public/recent-review";
    }

    @PostMapping
    public String addReview(@Valid @ModelAttribute("reviewRequest") ReviewRequest reviewRequest,
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // LỖI: Dữ liệu user nhập sai -> Nạp lại thông tin khóa học để hiển thị cùng form lỗi màu đỏ
            Course course = courseService.findCourseWithReviews(reviewRequest.getCourseId());
            CourseDetailResponse courseDetailResponse = new CourseDetailResponse(course, course.getReviews());
            model.addAttribute("courseDetailResponse", courseDetailResponse);
            // Không redirect, trả về trực tiếp view
            return "public/course-detail";
        }

        reviewService.saveReview(reviewRequest);
        return "redirect:/courses/" + reviewRequest.getCourseId();
    }
}