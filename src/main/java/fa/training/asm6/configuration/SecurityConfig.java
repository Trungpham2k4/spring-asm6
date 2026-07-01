package fa.training.asm6.configuration;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] whitelist = new String[] {
            "/css/**", "/static/**", "/js/**", "/images/**", "/fonts/**",
            "/categories/**", "/instructor/login", "/reviews/**", "/courses/**", "/webjars/**"
    };

    @Autowired
    private CustomLoginFailureHandler customLoginFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(whitelist).permitAll()
                .anyRequest().authenticated()
        );
        http.formLogin(form -> form
                .loginPage("/instructor/login")
                .failureHandler(customLoginFailureHandler)
                .defaultSuccessUrl("/instructor/dashboard", true)
                .permitAll()
        );
        http.logout(logout -> logout
                .logoutUrl("/instructor/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/courses")
                .permitAll()
        );
        http.headers(header ->
                header.cacheControl(HeadersConfigurer.CacheControlConfig::disable));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
