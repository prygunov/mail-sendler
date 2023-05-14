package net.artux.sendler.controller.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.sendler.model.exception.exceptions.NotFoundException;
import net.artux.sendler.model.security.AuthenticationRequest;
import net.artux.sendler.model.security.AuthenticationResponse;
import net.artux.sendler.model.user.SecurityUser;
import net.artux.sendler.service.util.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Безопасность")
@RequiredArgsConstructor
@RestController
@RequestMapping("/authenticate")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Operation(summary = "Получение токена безопасности")
    @PostMapping
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        try {
            SecurityUser securityUser = (SecurityUser) authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()))
                    .getPrincipal();
            final String token = jwtService.generateToken(securityUser);
            return new AuthenticationResponse(token);
        } catch (BadCredentialsException e) {
            throw new NotFoundException("Неправильный логин или пароль.");
        }
    }

}
