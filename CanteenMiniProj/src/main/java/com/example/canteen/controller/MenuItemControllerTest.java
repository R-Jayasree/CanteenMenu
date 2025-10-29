package com.example.canteen.controller;

import com.example.canteen.model.MenuItem;
import com.example.canteen.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @BeforeEach
    void setup() {
        // Insert sample menu item for test
        menuItemRepository.deleteAll();
        MenuItem item = new MenuItem();
        item.setName("Test Burger");
        item.setDescription("Test Description");
        item.setImageUrl("http://example.com/img.jpg");
        item.setDate("2025-10-29");
        item.setPrice(99.0);
        menuItemRepository.save(item);
    }

    @Test
    void shouldReturnDailyMenuItems() throws Exception {
        mockMvc.perform(get("/api/menu/date/2025-10-29")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Burger"));
    }
}
