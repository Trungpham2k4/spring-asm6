package fa.training.asm6.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");
        String errorType = "bad_credentials";

        if (username == null || username.trim().isEmpty() || request.getParameter("password").isEmpty()) {
            errorType = "blank_fields";
        }

        String encodedUsername = (username != null) ? java.net.URLEncoder.encode(username, "UTF-8") : "";
        setDefaultFailureUrl("/instructor/login?error=" + errorType + "&user=" + encodedUsername);

        super.onAuthenticationFailure(request, response, exception);
    }
}