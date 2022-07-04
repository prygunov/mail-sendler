package net.artux.mupse.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        paramName = "Authorization",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER

)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("mupse-public")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MUPSE")
                        .description("Сервисы REST API. Для использования необходимо получить токен безопасности в разделе \"Безопасность\"," +
                                " затем нажать кнопку \"Authorize\" и ввести полученный ранее токен. Регистрация проходит в разделе \"Пользователь\"." +
                                " Если клиент не имеет токен, либо он устарел будет выдана 403 ошибка.")
                        .version("0.1")
                        .license(new License().name("Privacy policy").url("https://artux.net/privacy")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));

    }

}
