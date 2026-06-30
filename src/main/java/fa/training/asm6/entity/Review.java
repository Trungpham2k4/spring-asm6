package fa.training.asm6.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_review")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String authorName;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false, check = {@CheckConstraint(name = "rating_check", constraint = "rating >= 1 AND rating <= 5")})
    Integer rating;

    @Column(nullable = false)
    String content;

    @Column(nullable = false)
    Integer status;

    @CreationTimestamp
    LocalDateTime createdTime;

    @UpdateTimestamp
    LocalDateTime modifiedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    public Review(String authorName, String email, Integer rating, String content, Integer status) {
        this.authorName = authorName;
        this.email = email;
        this.rating = rating;
        this.content = content;
        this.status = status;
    }
}
