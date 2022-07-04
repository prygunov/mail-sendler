package net.artux.mupse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.mupse.configuration.JwtUtil;
import net.artux.mupse.model.security.AuthenticationRequest;
import net.artux.mupse.model.security.AuthenticationResponse;
import net.artux.mupse.service.util.UserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final UserDetailService userDetailService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Получение токена безопасности")
    @PostMapping
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Неправильный логин или пароль.");
        }
        final UserDetails userDetails = userDetailService.loadUserByUsername(request.getLogin());
        final String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

}
