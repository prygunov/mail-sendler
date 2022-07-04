package net.artux.mupse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.mupse.configuration.JwtUtil;
import net.artux.mupse.model.security.AuthenticationRequest;
import net.artux.mupse.model.security.AuthenticationResponse;
import net.artux.mupse.model.user.RegisterUserDto;
import net.artux.mupse.model.user.UserDto;
import net.artux.mupse.service.user.UserService;
import net.artux.mupse.service.util.UserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
