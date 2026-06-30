package fa.training.asm6.entity;

import fa.training.asm6.enums.LookupType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.processing.Exclude;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_lookup")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lookup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "lookup_type", nullable = false)
    @Enumerated(EnumType.STRING)
    LookupType lookupType;

    @Column(name = "lookup_key", nullable = false)
    Integer key;

    @Column(name = "lookup_value", nullable = false)
    String value;

    @CreationTimestamp
    LocalDateTime createdTime;

    @UpdateTimestamp
    LocalDateTime modifiedTime;

    public Lookup(Integer id, LookupType lookupType, Integer key, String value) {
        this.id = id;
        this.lookupType = lookupType;
        this.key = key;
        this.value = value;
    }
}
