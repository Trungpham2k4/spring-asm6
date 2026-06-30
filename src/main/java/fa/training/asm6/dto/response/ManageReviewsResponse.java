package fa.training.asm6.dto.response;

import fa.training.asm6.entity.Review;

import java.util.List;

public record ManageReviewsResponse(List<Review> pending, List<Review> approved) {
}
