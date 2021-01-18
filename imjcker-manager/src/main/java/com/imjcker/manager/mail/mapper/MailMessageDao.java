package com.imjcker.spring.cloud.service.mail.mapper;

import com.imjcker.spring.cloud.service.mail.model.MailMessage;
import org.springframework.data.repository.CrudRepository;

public interface MailMessageDao extends CrudRepository<MailMessage, Integer> {
}
