package com.example.canteen.service;

import com.example.canteen.model.MenuItem;
import com.example.canteen.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItem> getAllMenuItems() { return menuItemRepository.findAll(); }

    public List<MenuItem> getMenuItemsByDate(String date) { return menuItemRepository.findByDate(date); }

    public MenuItem addMenuItem(MenuItem menuItem) { return menuItemRepository.save(menuItem); }

    public MenuItem updateMenuItem(String id, MenuItem menuItem) {
        menuItem.setId(id);
        return menuItemRepository.save(menuItem);
    }

    public void deleteMenuItem(String id) { menuItemRepository.deleteById(id); }

    public Optional<MenuItem> getMenuItemById(String id) { return menuItemRepository.findById(id); }
}
