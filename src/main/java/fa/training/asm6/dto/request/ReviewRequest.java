package fa.training.asm6.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull
    Integer courseId;

    @NotNull
    @NotBlank
    String authorName;

    @NotNull
    @Email
    String email;

    @NotNull
    @Min(1)
    @Max(5)
    Integer rating;

    @NotNull
    @NotBlank
    String content;

    @NotNull
    @Min(1)
    @Max(2)
    Integer status;

    @Override
    public String toString() {
        return "ReviewRequest{" +
                "courseId=" + courseId +
                ", authorName='" + authorName + '\'' +
                ", email='" + email + '\'' +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                ", status=" + status +
                '}';
    }
}
