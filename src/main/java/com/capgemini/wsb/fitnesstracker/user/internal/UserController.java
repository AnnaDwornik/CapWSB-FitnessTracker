package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
class UserController {

    @Autowired
    private final UserServiceImpl userService;
    @Autowired
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

    @GetMapping("/email")
    public ResponseEntity<List<User>> getUserByEmail(@RequestParam String email) {
        System.out.println("email");
        return userService.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(Collections.singletonList(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{imie}/{nazwisko}")
    public UserDto getUser(@PathVariable String imie, @PathVariable String nazwisko) {
        System.out.println("Imie");
        return userService.getUserByNameAndLastName(imie, nazwisko)
                .map(userMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    @GetMapping("/older/{date}")
    public List<UserDto> getAllUsersOlderThan(@PathVariable LocalDate date) {
        return userService.findAllUsersOlderThan(date)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) throws InterruptedException {
        System.out.println("User with e-mail: " + userDto.email() + " passed to the request");

        User createdUser = userService.createUser(this.userMapper.toEntity(userDto));
        UserDto createdUserDto = userMapper.toDto(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {

        User updatedUser = userService.updateUser(userId, userMapper.toEntity(userDto));

        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}