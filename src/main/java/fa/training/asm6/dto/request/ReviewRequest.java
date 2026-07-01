package fa.training.asm6.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull(message = "Course ID cannot be null")
    Integer courseId;

    @NotBlank(message = "Author name cannot be blank")
    String authorName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    String email;

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    Integer rating;

    @NotBlank(message = "Content cannot be blank")
    String content;
}
