package fa.training.asm6.exception;

import fa.training.asm6.exception.custom.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j // Tận dụng SLF4J đã setup để ghi log
public class GlobalExceptionHandler {

    // 1. Xử lý nhóm lỗi 404 (Không tìm thấy tài nguyên)
    @ExceptionHandler({NotFoundException.class, UsernameNotFoundException.class})
    public ModelAndView handleNotFound(Exception e, HttpServletRequest request) {
        log.warn("Resource not found at URI: {}. Message: {}", request.getRequestURI(), e.getMessage());

        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("errorMessage", e.getMessage()); // Truyền câu lỗi chi tiết xuống view
        mav.addObject("url", request.getRequestURI());
        return mav;
    }

    // 2. Xử lý lỗi 403 (Không có quyền truy cập)
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ModelAndView handleUnAuthorized(Exception e, HttpServletRequest request) {
        log.warn("Unauthorized access attempt at URI: {}", request.getRequestURI());

        ModelAndView mav = new ModelAndView("error/403");
        mav.addObject("errorMessage", "You are not authorized to access this resource.");
        return mav;
    }

    // 3. Xử lý lỗi hệ thống 500 (BẮT BUỘC PHẢI CÓ)
    // Hứng toàn bộ các lỗi Runtime (NullPointer, Database Error...)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(Exception e, HttpServletRequest request) {
        // Log lại toàn bộ Stack Trace để Dev debug (Tuyệt đối quan trọng)
        log.error("System has critical error at URI: {}", request.getRequestURI(), e);

        ModelAndView mav = new ModelAndView("error/500"); // Em cần tạo thêm file 500.html
        // Thông điệp thân thiện cho user, KHÔNG ĐƯA CÂU LỖI CỦA CODE RA ĐÂY
        mav.addObject("errorMessage", "Oops! Something went wrong on our end. Please try again later.");
        return mav;
    }
}