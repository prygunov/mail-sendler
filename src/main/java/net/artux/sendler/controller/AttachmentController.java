package net.artux.sendler.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.artux.sendler.model.file.AttachmentDto;
import net.artux.sendler.service.file.AttachmentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "Файлы")
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @Operation(summary = "Загрузка файла")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AttachmentDto uploadContacts(@RequestPart(value = "file") final MultipartFile file) throws IOException {
        return attachmentService.uploadFile(file);
    }

    @Operation(summary = "Список загруженных файлов")
    @GetMapping
    public List<AttachmentDto> getFiles() {
        return attachmentService.getFiles();
    }

    @Operation(summary = "Скачивание файла контактом")
    @GetMapping("/{uuid}")
    public ResponseEntity<byte[]> getFile(@PathVariable UUID uuid, @RequestParam Long id) {
        return attachmentService.getFile(uuid, id);
    }

    @Operation(summary = "Удаление файла")
    @DeleteMapping("/{id}")
    public boolean deleteFile(@PathVariable("id") Long id) {
        return attachmentService.deleteFile(id);
    }

}
