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

    Optional<TemplateEntity> findByOwnerAndId(UserEntity owner, Long id);

    Page<TemplateEntity> findAllByOwnerAndSubjectContainsIgnoreCase(UserEntity owner, String subject, Pageable pageable);

    void deleteByOwnerAndId(UserEntity entity, Long id);

}
