package com.nbacm.trelldochi.domain.attachment.repository;

import com.nbacm.trelldochi.domain.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
