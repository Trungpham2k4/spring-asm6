package fa.training.asm6.controller;

import fa.training.asm6.dto.response.CategoryPageResponse;
import fa.training.asm6.entity.Category;
import fa.training.asm6.entity.Course;
import fa.training.asm6.service.CategoryService;
import fa.training.asm6.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CourseService courseService;

    @GetMapping
    public String category(@RequestParam(name = "name", defaultValue = "") String name,
                           @RequestParam(name = "page", defaultValue = "1") Integer page,
                           Model model) {
        List<Category> categories = categoryService.findAll();
        Page<Course> courses = courseService.findCourseByCategoryName(name, page);
        CategoryPageResponse categoryPageResponse = new CategoryPageResponse(categories, courses);
        model.addAttribute("categoryPageResponse", categoryPageResponse);
        model.addAttribute("categoryName", name);
        model.addAttribute("currentPage", page);
        return "public/category";
    }
}
