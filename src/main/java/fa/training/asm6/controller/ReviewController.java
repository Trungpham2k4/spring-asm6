package fa.training.asm6.controller;

import fa.training.asm6.dto.request.ReviewRequest;
import fa.training.asm6.entity.Review;
import fa.training.asm6.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public String addReview(@Valid @ModelAttribute ReviewRequest reviewRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/course/" + reviewRequest.getCourseId();
        }
        reviewService.saveReview(reviewRequest);
//        model.addAttribute("success", "Review added successfully");
        return "redirect:/course/" + reviewRequest.getCourseId();
    }

    @GetMapping
    public String recentReviews(Model model, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int limit) {
        List<Review> reviews = reviewService.findMostRecentReviews(offset, limit);
        model.addAttribute("reviews", reviews);
        return "public/recent-review";
    }

    @GetMapping("/management")
    public String manageReviews(Model model) {
        model.addAttribute("manageReviewsResponse", reviewService.findReviewsWithStatus());
        return "instructor-pages/reviews";
    }

    @PostMapping("/approve")
    public String approveReview(@RequestParam Integer reviewId, Model model) {
        reviewService.approveReview(reviewId);
        return "redirect:/review/management";
    }

    @PostMapping("/reject")
    public String rejectReview(@RequestParam Integer reviewId, Model model) {
        reviewService.rejectReview(reviewId);
        return "redirect:/review/management";
    }
}
