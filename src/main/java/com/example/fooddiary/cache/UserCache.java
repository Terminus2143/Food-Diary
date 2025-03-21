package com.example.fooddiary.cache;

import com.example.fooddiary.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserCache extends LfuCache<User> {
    public UserCache() {
        super(2);
    }
}
