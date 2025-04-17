package com.example.fooddiary.service;

import com.example.fooddiary.cache.UserCache;
import com.example.fooddiary.dto.UserDto;
import com.example.fooddiary.exception.AlreadyExistsException;
import com.example.fooddiary.exception.NotFoundException;
import com.example.fooddiary.mapper.UserMapper;
import com.example.fooddiary.model.Dish;
import com.example.fooddiary.model.User;
import com.example.fooddiary.repository.DishRepository;
import com.example.fooddiary.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private DishRepository dishRepository;
    @Mock private UserCache userCache;
    @InjectMocks private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setAge(25);
        userDto.setHeight(170.0);
        userDto.setWeight(65.0);
        userDto.setGender("male");
    }

    @Test
    void addUser_success() {
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.addUser(userDto);

        assertEquals("testuser", result.getUsername());
        verify(userCache).put(1L, user);
    }

    @Test
    void addUser_alreadyExists_exceptionThrown() {
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(AlreadyExistsException.class, () -> userService.addUser(userDto));
    }

    @Test
    void getAllUsers_success() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        ResponseEntity<List<User>> response = userService.getAllUsers();

        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("testuser", response.getBody().get(0).getUsername());
    }

    @Test
    void getUserById_fromCache() {
        when(userCache.get(1L)).thenReturn(user);

        var result = userService.getUserById(1);
        assertEquals("testuser", Objects.requireNonNull(result.getBody()).getUsername());
        verify(userRepository, never()).findById(any());
    }

    @Test
    void getUserById_fromDatabase() {
        when(userCache.get(1L)).thenReturn(null);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        var result = userService.getUserById(1);
        assertEquals("testuser", Objects.requireNonNull(result.getBody()).getUsername());
        verify(userCache).put(1L, user);
    }

    @Test
    void getUserById_notFound_exceptionThrown() {
        when(userCache.get(1L)).thenReturn(null);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1));
    }

    @Test
    void updateUser_success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        User result = userService.updateUser(1, userDto);

        assertEquals("testuser", result.getUsername());
        verify(userCache).put(1L, user);
    }

    @Test
    void updateUser_userNotFound_exceptionThrown() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(1, userDto));
    }

    @Test
    void updateUser_duplicate_exceptionThrown() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(AlreadyExistsException.class, () -> userService.updateUser(1, userDto));
    }

    @Test
    void deleteUser_success() {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.deleteUser(1);

        verify(userRepository).deleteById(1);
        verify(userCache).remove(1L);
    }

    @Test
    void deleteUser_notFound_exceptionThrown() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteUser(1));
    }

    @Test
    void getUserDishes_success() {
        Dish dish = new Dish();
        dish.setId(1);
        when(dishRepository.findByUserId(1)).thenReturn(List.of(dish));

        var result = userService.getUserDishes(1);
        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
        assertEquals(1, result.getBody().get(0).getId());
    }

    @Test
    void getUserDishById_success() {
        Dish dish = new Dish();
        dish.setId(2);

        when(dishRepository.findByIdAndUserId(2, 1)).thenReturn(Optional.of(dish));

        var result = userService.getUserDishById(1, 2);
        assertEquals(2, Objects.requireNonNull(result.getBody()).getId());
    }

    @Test
    void getUserDishById_notFound_exceptionThrown() {
        when(dishRepository.findByIdAndUserId(2, 1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserDishById(1, 2));
    }
}
