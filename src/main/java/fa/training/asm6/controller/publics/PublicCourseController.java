package fa.training.asm6.controller.publics;


import fa.training.asm6.dto.request.ReviewRequest;
import fa.training.asm6.dto.response.CourseDetailResponse;
import fa.training.asm6.dto.response.HomepageResponse;
import fa.training.asm6.entity.Category;
import fa.training.asm6.entity.Course;
import fa.training.asm6.entity.Review;
import fa.training.asm6.service.CategoryService;
import fa.training.asm6.service.CourseService;
import fa.training.asm6.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/courses") // Chuẩn hóa URL số nhiều
@RequiredArgsConstructor
public class PublicCourseController {

    private final CourseService courseService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    @GetMapping
    public String home(@RequestParam(value = "page", defaultValue = "1") Integer page, Model model) {
        Page<Course> recentPublishedCourses = courseService.findRecentPublishedCourses(page);
        List<Category> categories = categoryService.findAll();
        List<Review> recentReviews = reviewService.findAll();
        HomepageResponse homepageResponse = new HomepageResponse(recentPublishedCourses, recentReviews, categories);
        model.addAttribute("homepage", homepageResponse);
        model.addAttribute("currentPage", page);
        return "public/home";
    }


    @GetMapping("/{id}")
    public String courseDetail(@PathVariable Integer id, Model model){
        Course course = courseService.findCourseWithReviews(id);
        CourseDetailResponse courseDetailResponse = new CourseDetailResponse(course, course.getReviews());
        model.addAttribute("courseDetailResponse", courseDetailResponse);
        model.addAttribute("reviewRequest", new ReviewRequest());
        return "public/course-detail";
    }
}