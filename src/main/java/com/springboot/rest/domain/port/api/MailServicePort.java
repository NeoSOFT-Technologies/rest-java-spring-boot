package com.springboot.rest.domain.port.api;

import com.springboot.rest.infrastructure.entity.User;

public interface MailServicePort {
    void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml);
    void sendEmailFromTemplate(User user, String templateName, String titleKey);
    void sendActivationEmail(User user);
    void sendCreationEmail(User user);
    void sendPasswordResetMail(User user);
}
