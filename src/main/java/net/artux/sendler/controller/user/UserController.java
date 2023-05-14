package net.artux.sendler.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.sendler.model.user.RegisterUserDto;
import net.artux.sendler.model.user.UserDto;
import net.artux.sendler.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RequiredArgsConstructor
@Tag(name = "Пользователь")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Регистрация")
    @PostMapping("/register")
    public UserDto registerUser(@Valid @RequestBody RegisterUserDto dto) {
        return userService.registerUser(dto);
    }

    @Operation(summary = "Информация о пользователе")
    @GetMapping("/info")
    public UserDto getUser() {
        return userService.getUser();
    }

}
