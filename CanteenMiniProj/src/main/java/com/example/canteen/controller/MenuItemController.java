package com.example.canteen.controller;

import com.example.canteen.model.MenuItem;
import com.example.canteen.model.Rating;
import com.example.canteen.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @GetMapping
    public List<MenuItem> getAll() {
        return menuItemService.getAllMenuItems();
    }
    
    @GetMapping("/date/{date}")
    public List<MenuItem> getByDate(@PathVariable String date) {
    
        return menuItemService.getMenuItemsByDate(date);
    }

    @GetMapping("/{id}")
    public Optional<MenuItem> getMenuItemById(@PathVariable String id) {
        return menuItemService.getMenuItemById(id);
    }

    @PostMapping
    public MenuItem add(@Valid @RequestBody MenuItem menuItem) {
        return menuItemService.addMenuItem(menuItem);
    }

    @PutMapping("/{id}")
    public MenuItem update(@PathVariable String id, @Valid @RequestBody MenuItem menuItem) {
        return menuItemService.updateMenuItem(id, menuItem);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        menuItemService.deleteMenuItem(id);
    }
    @PostMapping("/{id}/rate")
    public ResponseEntity<?> addRating(@PathVariable String id, @Valid @RequestBody Rating rating) {
        Optional<MenuItem> optionalMenuItem = menuItemService.getMenuItemById(id);
        if (optionalMenuItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MenuItem menuItem = optionalMenuItem.get();

        // Set rating timestamp if not set
        if (rating.getTimestamp() == null) {
            rating.setTimestamp(LocalDateTime.now());
        }

        // Append rating to menuItem's ratings list
        List<Rating> ratings = menuItem.getRatings();
        if (ratings == null) {
            ratings = new java.util.ArrayList<>();
        }
        ratings.add(rating);
        menuItem.setRatings(ratings);

        // Save updated menuItem
        menuItemService.addMenuItem(menuItem);

        return ResponseEntity.ok("Rating added successfully");
    }
}
