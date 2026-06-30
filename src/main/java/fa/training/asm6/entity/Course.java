package fa.training.asm6.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_course")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    String content;

    @Column(nullable = false)
    Integer status;

    String category;

    @CreationTimestamp
    LocalDateTime createdTime;

    @UpdateTimestamp
    LocalDateTime modifiedTime;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> reviews;

    public Course(String title, String description, String content, Integer status, String category) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.status = status;
        this.category = category;
    }
}
