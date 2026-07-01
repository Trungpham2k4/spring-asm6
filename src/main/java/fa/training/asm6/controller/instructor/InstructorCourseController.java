package fa.training.asm6.controller.instructor;

import fa.training.asm6.dto.request.CourseRequest;
import fa.training.asm6.dto.response.CourseWithStatus;
import fa.training.asm6.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/instructor/courses") // Đường dẫn đã được bảo vệ bởi SecurityConfig
@RequiredArgsConstructor
public class InstructorCourseController {

    private final CourseService courseService;

    @GetMapping
    public String manageCourse(@RequestParam(value = "page", defaultValue = "1") Integer page, Model model){
        Page<CourseWithStatus> courses = courseService.findCoursesPagination(page);
        model.addAttribute("courses", courses);
        model.addAttribute("currentPage", page);
        return "instructor-pages/manage-courses";
    }

    @GetMapping("/new") // Sửa /add thành /new chuẩn quy ước
    public String addCoursePage(Model model){
        model.addAttribute("course", new CourseRequest());
        return "instructor-pages/create-edit";
    }

    @PostMapping
    public String addCourse(@Valid @ModelAttribute("course") CourseRequest courseRequest,
                            BindingResult bindingResult, Model model) {
        // NẾU CÓ LỖI VALIDATION: Phải return lại view hiện tại, TUYỆT ĐỐI KHÔNG REDIRECT
        if (bindingResult.hasErrors()) {
            return "instructor-pages/create-edit";
        }
        courseService.saveCourse(courseRequest);
        return "redirect:/instructor/courses";
    }

    @GetMapping("/{id}/edit") // URL chuẩn: /instructor/courses/1/edit
    public String editCoursePage(@PathVariable Integer id, Model model){
        CourseRequest courseRequest = courseService.findUpdateCourse(id);
        model.addAttribute("course", courseRequest);
        return "instructor-pages/create-edit";
    }

    @PutMapping("/{id}") // Dùng đúng PutMapping thay vì Post
    public String editCourse(@PathVariable Integer id,
                             @Valid @ModelAttribute("course") CourseRequest courseRequest,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "instructor-pages/create-edit";
        }
        courseRequest.setId(id); // Đảm bảo gán ID từ path vào
        courseService.updateCourse(courseRequest);
        return "redirect:/instructor/courses";
    }

    @DeleteMapping("/{id}") // URL chuẩn: /instructor/courses/1 (Xóa bỏ động từ delete khỏi url)
    public String deleteCourse(@PathVariable Integer id){
        courseService.deleteCourse(id);
        return "redirect:/instructor/courses";
    }

    @PatchMapping("/{id}/status")
    public String updateCourseStatus(@PathVariable Integer id, @RequestParam(name = "status") Integer status){
        courseService.updateStatus(id, status);
        return "redirect:/instructor/courses";
    }
}