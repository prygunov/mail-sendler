package net.artux.sendler.service.file;

import net.artux.sendler.model.file.AttachmentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AttachmentService {

    AttachmentDto uploadFile(MultipartFile file) throws IOException;

    ResponseEntity<byte[]> getFile(UUID uuid, Long id);

    List<AttachmentDto> getFiles();

    boolean deleteFile(Long id);
}
