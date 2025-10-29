package com.example.canteen.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class Rating {
    @Min(1)
    @Max(5)
    private int stars;

    @NotBlank
    private String userId;

    private String feedback;

    private LocalDateTime timestamp;

    public Rating() {}

    public Rating(int stars, String userId, String feedback, LocalDateTime timestamp) {
        this.stars = stars;
        this.userId = userId;
        this.feedback = feedback;
        this.timestamp = timestamp;
    }

    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
