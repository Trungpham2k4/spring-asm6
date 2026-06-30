package fa.training.asm6.configuration;

import fa.training.asm6.entity.*;
import fa.training.asm6.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

import static fa.training.asm6.enums.LookupType.COURSE_STATUS;
import static fa.training.asm6.enums.LookupType.REVIEW_STATUS;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final LookupRepository lookupRepository;
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;

    @Value("${admin.username}")
    private String INSTRUCTOR_USERNAME;
    @Value("${admin.password}")
    private String INSTRUCTOR_PASSWORD;

    private final PasswordEncoder passwordEncoder;

    @EventListener(ContextRefreshedEvent.class)
    public void seed() {
        if(lookupRepository.count() == 0){
            List<Lookup> lookups = List.of(
                    new Lookup(null, COURSE_STATUS, 1, "Draft"),
                    new Lookup(null, COURSE_STATUS, 2, "Published"),
                    new Lookup(null, COURSE_STATUS, 3, "Archived"),

                    // Trạng thái cho Review
                    new Lookup(null, REVIEW_STATUS, 1, "Pending"),
                    new Lookup(null, REVIEW_STATUS, 2, "Approved")
            );
            lookupRepository.saveAll(lookups);
            log.info("Data seeding completed. Inserted {} lookup records.", lookups.size());
        }

        if(instructorRepository.count() == 0) {
            Instructor instructor = Instructor.builder()
                    .username(INSTRUCTOR_USERNAME)
                    .password(passwordEncoder.encode(INSTRUCTOR_PASSWORD))
                    .role("ADMIN")
                    .build();
            instructorRepository.save(instructor);
        }

        if(categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category("HTML", 1),
                    new Category("CSS", 1),
                    new Category("JavaScript", 1)
            );
            categoryRepository.saveAll(categories);
        }

        if(courseRepository.count() == 0){
            List<Course> courses = getCourses();
            courseRepository.saveAll(courses);
        }
    }

    private static List<Course> getCourses() {
        Course course = new Course(
                "HTML for Beginners",
                "Learn the basics of HTML",
                """
                        # 1.First chapter
                        # 2.Second chapter
                        # 3.Third chapter""",
                2,
                "HTML");
        List<Review> reviews = getReviews();
        reviews.forEach(review -> review.setCourse(course));
        course.setReviews(reviews);
        List<Course> courses = List.of(
                course,
                new Course(
                        "CSS for Beginners",
                        "Learn the basics of CSS",
                        """
                                # 1.First chapter
                                # 2.Second chapter
                                # 3.Third chapter""",
                        2,
                        "CSS"),
                new Course(
                        "JavaScript for Beginners",
                        "Learn the basics of JavaScript",
                        """
                                # 1.First chapter
                                # 2.Second chapter
                                # 3.Third chapter""",
                        2,
                        "JavaScript")
        );
        return courses;
    }

    private static List<Review> getReviews() {
        List<Review> reviews = List.of(
                new Review("John Doe", "johndoe@gmail.com", 5, "Great course!", 2),
                new Review("Jane Smith", "janesmith@gmail.com", 4, "Very informative.", 2),
                new Review("Alice Johnson", "alicejohnson@gmail.com",3, "Good, but could be better.", 2),
                new Review("Bob Brown", "bobbrown@gmail.com", 1, "Good, but could be better.", 1)
        );
        return reviews;
    }
}
