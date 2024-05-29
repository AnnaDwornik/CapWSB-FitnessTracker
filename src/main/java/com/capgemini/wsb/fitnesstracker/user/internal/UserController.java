package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    UserController(UserServiceImpl userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/simple")
    public List<UserSimpleDto> getAllSimpleUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toSimpleDto)
                .toList();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        return userService.getUser(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    @GetMapping("/{email}")
    public UserDto getUserByEmail(@PathVariable String email) {
        System.out.println("email");
        return userService.getUserByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    @GetMapping("/{imie}/{nazwisko}")
    public UserDto getUser(@PathVariable String imie, @PathVariable String nazwisko) {
//        System.out.println("Imie");
        return userService.getUserByNameAndLastName(imie, nazwisko)
                .map(userMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    @PostMapping
    public User addUser(@RequestBody UserDto userDto) throws InterruptedException {

        // Demonstracja how to use @RequestBody
        System.out.println("User with e-mail: " + userDto.email() + "passed to the request");

        // TODO: saveUser with Service and return User
        User savedUser = userService.saveUser(userDto);
        return savedUser;
//        userService.createUser(this.userMapper.toEntity(userDto));
    }

//    @DeleteMapping("/{userId}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
//
//        userService.deleteUser(userId);
//
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
}