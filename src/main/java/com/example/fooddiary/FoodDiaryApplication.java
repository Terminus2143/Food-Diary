package com.example.fooddiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Food Diary application.
 */
@SpringBootApplication
public class FoodDiaryApplication {

  /**
   * Entry point of the Food Diary application.
   *
   * @param args Command-line arguments passed to the application.
   */
  public static void main(String[] args) {
    SpringApplication.run(FoodDiaryApplication.class, args);
  }
}