package net.artux.sendler.repository.file;

import net.artux.sendler.entity.file.AttachmentEntity;
import net.artux.sendler.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {

    Optional<AttachmentEntity> findByOwnerAndId(UserEntity user, Long id);

    List<AttachmentEntity> findAllByOwner(UserEntity user);

    void deleteByOwnerAndId(UserEntity user, Long id);

}
