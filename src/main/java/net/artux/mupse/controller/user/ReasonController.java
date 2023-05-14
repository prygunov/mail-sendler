package net.artux.mupse.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.model.reason.ReasonDto;
import net.artux.mupse.service.reason.ReasonService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Tag(name = "Отписки")
@RestController
@RequestMapping("/contacts/unsubs")
public class ReasonController {

    private final ReasonService service;

    @Operation(summary = "Причины отписок страницами")
    @GetMapping
    public ResponsePage<ReasonDto> getReasons(@Valid @ParameterObject QueryPage queryPage) {
        return service.getReasons(queryPage);
    }

    @Operation(summary = "Скачать причины")
    @GetMapping("/export")
    public void downloadAll(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=\"reasons.xlsx\"");

        ByteArrayInputStream stream = service.exportAll();
        IOUtils.copy(stream, response.getOutputStream());
    }


    @Operation(summary = "Очистить причины")
    @PostMapping("/clear")
    public boolean downloadAll(@Parameter(description = "Удалить соответствуюещие контакты") boolean clearContacts) {
        service.clear(clearContacts);
        return true;
    }
}
