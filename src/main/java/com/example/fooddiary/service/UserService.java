package com.example.fooddiary.service;

import com.example.fooddiary.dto.UserDto;
import com.example.fooddiary.exception.AlreadyExistsException;
import com.example.fooddiary.exception.NotFoundException;
import com.example.fooddiary.mapper.UserMapper;
import com.example.fooddiary.model.User;
import com.example.fooddiary.repository.UserRepository;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    private static final String
            USER_NOT_FOUND_MESSAGE_ID = "Пользователь с ID %d не найден";
    private static final String
            USER_EXISTENCE_MESSAGE = "Пользователь с таким именем или почтой уже существует";
    private static final String
            USERS_NOT_FOUND = "Пользователей не обнаружено";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User addUser(@RequestBody UserDto userDto) {
        try {
            User user = userMapper.toEntity(userDto);
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistsException(USER_EXISTENCE_MESSAGE);
        }
    }

    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new NotFoundException(USERS_NOT_FOUND);
        }
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<User> getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(USER_NOT_FOUND_MESSAGE_ID, id)
                ));
        return ResponseEntity.ok(user);
    }

    public User updateUser(Integer id, @RequestBody UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(USER_NOT_FOUND_MESSAGE_ID, id)
                ));

        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getAge() > 0) {
            user.setAge(userDto.getAge());
        }
        if (userDto.getHeight() > 0) {
            user.setHeight(userDto.getHeight());
        }
        if (userDto.getWeight() > 0) {
            user.setWeight(userDto.getWeight());
        }
        if (userDto.getGender() != null) {
            user.setGender(userDto.getGender());
        }

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new AlreadyExistsException(USER_EXISTENCE_MESSAGE);
        }
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(
                    String.format(USER_NOT_FOUND_MESSAGE_ID, id)
            );
        }
        userRepository.deleteById(id);
    }
}
