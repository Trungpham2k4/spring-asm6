package fa.training.asm6.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InstructorRequest {

    @NotBlank(message = "Username cannot be empty")
    @NotNull(message = "Username cannot be null")
    String username;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be empty")
    String password;
}
