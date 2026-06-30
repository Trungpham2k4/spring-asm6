package fa.training.asm6.controller;

import fa.training.asm6.dto.request.InstructorRequest;
import fa.training.asm6.dto.response.InstructorDashboardResponse;
import fa.training.asm6.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    /**
     * Note: the name of folders in templates should not be the same as url in request mapping,
     * otherwise it will cause a conflict and the page will not be displayed correctly.
     * @param model
     * @return
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("instructor", new InstructorRequest());
        return "instructor-pages/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        InstructorDashboardResponse instructorDashboardResponse = instructorService.getInstructorDashboardInfo();
        model.addAttribute("dashboardInfo", instructorDashboardResponse);
        return "instructor-pages/dashboard";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/course";
    }
}
