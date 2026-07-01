package fa.training.asm6.controller.instructor;

import fa.training.asm6.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/instructor/reviews") // Không gian bảo mật
@RequiredArgsConstructor
public class InstructorReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public String manageReviews(Model model) {
        model.addAttribute("manageReviewsResponse", reviewService.findReviewsWithStatus());
        return "instructor-pages/reviews";
    }

    // Đổi từ POST "/approve" thành PATCH "/{id}/approve"
    @PatchMapping("/{id}/approve")
    public String approveReview(@PathVariable("id") Integer reviewId) {
        reviewService.approveReview(reviewId);
        return "redirect:/instructor/reviews";
    }

    // Đổi từ POST "/reject" thành DELETE "/{id}"
    @DeleteMapping("/{id}")
    public String rejectReview(@PathVariable("id") Integer reviewId) {
        reviewService.rejectReview(reviewId);
        return "redirect:/instructor/reviews";
    }
}