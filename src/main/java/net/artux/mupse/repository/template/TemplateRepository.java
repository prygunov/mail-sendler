package net.artux.mupse.repository.template;

import net.artux.mupse.entity.template.TemplateEntity;
import net.artux.mupse.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface TemplateRepository extends JpaRepository<TemplateEntity, Long> {

    Optional<TemplateEntity> findByOwnerAndTitle(UserEntity owner, String name);
    Optional<TemplateEntity> findByOwnerAndId(UserEntity owner, Long id);

    Page<TemplateEntity> findAllByOwner(UserEntity owner, Pageable pageable);
    Page<TemplateEntity> findAllByOwnerAndTitleContainsIgnoreCase(UserEntity owner, String title, Pageable pageable);

    void deleteByOwnerAndId(UserEntity entity, Long id);

}
