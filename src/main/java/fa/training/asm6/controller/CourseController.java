package fa.training.asm6.controller;

import fa.training.asm6.dto.request.CourseRequest;
import fa.training.asm6.dto.request.ReviewRequest;
import fa.training.asm6.dto.response.CourseDetailResponse;
import fa.training.asm6.dto.response.CourseWithStatus;
import fa.training.asm6.dto.response.HomepageResponse;
import fa.training.asm6.entity.Category;
import fa.training.asm6.entity.Course;
import fa.training.asm6.entity.Review;
import fa.training.asm6.service.CategoryService;
import fa.training.asm6.service.CourseService;
import fa.training.asm6.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

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

    @GetMapping("/manage-course")
    public String manageCourse(@RequestParam(value = "page", defaultValue = "1") Integer page, Model model){
        Page<CourseWithStatus> courses = courseService.findCoursesPagination(page);
        model.addAttribute("courses", courses);
        model.addAttribute("currentPage", page);
        return "instructor-pages/manage-courses";
    }

    @GetMapping("/add")
    public String addCoursePage(Model model){
        model.addAttribute("course", new CourseRequest());
        return "instructor-pages/create-edit";
    }

    @PostMapping("/add")
    public String addCourse(@Valid @ModelAttribute CourseRequest courseRequest){
        courseService.saveCourse(courseRequest);
        return "redirect:/course/manage-course";
    }

    @GetMapping("/edit/{id}")
    public String editCoursePage(@PathVariable Integer id, Model model){
        CourseRequest courseRequest = courseService.findUpdateCourse(id);
        model.addAttribute("course", courseRequest);
        return "instructor-pages/create-edit";
    }

    @PostMapping("/edit")
    public String editCourse(@Valid @ModelAttribute CourseRequest courseRequest){
        courseService.updateCourse(courseRequest);
        return "redirect:/course/manage-course";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Integer id){
        courseService.deleteCourse(id);
        return "redirect:/course/manage-course";
    }

    @PatchMapping("/{id}/status/{courseStatus}")
    public String updateCourseStatus(@PathVariable Integer id, @PathVariable Integer courseStatus){
        courseService.updateStatus(id, courseStatus);
        return "redirect:/course/manage-course";
    }
}
