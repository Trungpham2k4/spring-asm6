package fa.training.asm6.service;

import fa.training.asm6.entity.Instructor;
import fa.training.asm6.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final InstructorRepository instructorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Instructor instructor = instructorRepository.findByUsername(username);
        if (instructor == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(
                instructor.getUsername(),
                instructor.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(instructor.getRole()))
        );
    }
}
