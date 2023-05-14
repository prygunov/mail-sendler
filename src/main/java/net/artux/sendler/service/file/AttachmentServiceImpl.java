package net.artux.sendler.service.file;

import lombok.RequiredArgsConstructor;
import net.artux.sendler.entity.file.AttachmentEntity;
import net.artux.sendler.entity.user.UserEntity;
import net.artux.sendler.model.file.AttachmentDto;
import net.artux.sendler.model.file.AttachmentMapper;
import net.artux.sendler.repository.file.AttachmentRepository;
import net.artux.sendler.repository.user.UserRepository;
import net.artux.sendler.service.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper mapper;

    @Override
    public AttachmentDto uploadFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        AttachmentEntity entity = new AttachmentEntity(fileName, file.getContentType(), file.getBytes());
        entity.setOwner(userService.getUserEntity());
        return mapper.dto(attachmentRepository.save(entity));
    }

    @Override
    public ResponseEntity<byte[]> getFile(UUID uuid, Long id) {
        UserEntity user = userRepository.findByToken(uuid).orElseThrow();
        AttachmentEntity entity = attachmentRepository.findByOwnerAndId(user, id).orElseThrow();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + entity.getName() + "\"")
                .body(entity.getData());
    }

    @Override
    public List<AttachmentDto> getFiles() {
        return mapper.dto(attachmentRepository.findAllByOwner(userService.getUserEntity()));
    }

    @Override
    public boolean deleteFile(Long id) {
        attachmentRepository.deleteByOwnerAndId(userService.getUserEntity(), id);
        return true;
    }
}
