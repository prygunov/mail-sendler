package net.artux.mupse.controller.contact;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import net.artux.mupse.service.sender.SenderServiceImpl;
import net.artux.mupse.service.statistic.StatisticService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RequiredArgsConstructor
@Tag(name = "Ведение статистики")
@RestController
@RequestMapping("/statistic")
public class StatisticRegistrationController {

    private final StatisticService service;
    private final Log logger = LogFactory.getLog(SenderServiceImpl.class);

    @Operation(summary = "Чек об открытии письма")
    @GetMapping(value = "/opened/{mailingUuid}/{contactUuid}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] check(@PathVariable UUID mailingUuid, @PathVariable UUID contactUuid) throws IOException {
        try {
            service.redirect(mailingUuid, contactUuid);
        }
        catch (Throwable throwable) {
            logger.error(throwable.getMessage());
        }
        service.opened(mailingUuid, contactUuid);
        Resource resource = new ClassPathResource("/static/pixel.jpg");
        InputStream in = resource.getInputStream();
        return IOUtils.toByteArray(in);
    }

    @Operation(summary = "Редирект на страницу")
    @GetMapping(value = "/redirect/{mailingUuid}/{contactUuid}")
    public ModelAndView check(@PathVariable UUID mailingUuid, @PathVariable UUID contactUuid, @RequestParam String to) {
        try {
            service.redirect(mailingUuid, contactUuid);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage());
        }
        String redirectUrl = URLDecoder.decode(to, StandardCharsets.UTF_8);
        return new ModelAndView("redirect:" + redirectUrl);
    }

}
