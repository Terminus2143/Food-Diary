package com.example.fooddiary.service;

import com.example.fooddiary.model.FoodDiaryEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class FoodDiaryService {
    private final List<FoodDiaryEntry> entries = new ArrayList<>();

    public List<FoodDiaryEntry> getAllEntries() {
        return new ArrayList<>(entries);
    }

    public Optional<FoodDiaryEntry> getEntryByName(String foodName) {
        return entries.stream()
                .filter(entry -> entry.getFoodName().equalsIgnoreCase(foodName))
                .findFirst();
    }

    public void addFood(FoodDiaryEntry food) {
        entries.add(food);
    }
}
