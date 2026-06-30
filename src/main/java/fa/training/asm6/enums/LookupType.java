package fa.training.asm6.enums;

import lombok.Getter;

@Getter
public enum LookupType {
    COURSE_STATUS("course_status"),
    REVIEW_STATUS("review_status");

    private final String value;
    LookupType(String value) {
        this.value = value;
    }
}
