package fa.training.asm6.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CourseRequest {
    @Min(1)
    Integer id;

    @NotBlank
    @NotNull
    String title;

    @NotBlank
    @NotNull
    String description;

    @NotBlank
    @NotNull
    String content;

    @Min(1)
    @Max(3)
    @NotNull
    Integer status;

    @Pattern(regexp = "^([a-zA-Z0-9\\s]+(,[a-zA-Z0-9\\s]+)*)?$", message = "Category must be a comma-separated list of alphanumeric values.")
    String category;

    String oldCategory;
}
