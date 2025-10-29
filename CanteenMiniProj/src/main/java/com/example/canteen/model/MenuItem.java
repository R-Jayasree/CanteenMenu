package com.example.canteen.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Document(collection = "menuitems")
public class MenuItem {
    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private String imageUrl;

    @NotBlank
    private String date; // Format: YYYY-MM-DD
    
    @NotNull
    private Double price;
    private List<Rating> ratings;

    public MenuItem() {}

    public MenuItem(String name, String description, String imageUrl, String date, List<Rating> ratings) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
        this.ratings = ratings;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public List<Rating> getRatings() { return ratings; }
    public void setRatings(List<Rating> ratings) { this.ratings = ratings; }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
}
