package com.example.canteen.repository;

import com.example.canteen.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
	
    List<MenuItem> findByDate(String date);
 
}
