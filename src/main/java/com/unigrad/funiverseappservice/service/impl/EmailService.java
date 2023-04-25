package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private final JavaMailSender javaMailSender;

    private final ResourceLoader resourceLoader;

    @Override
    public void send(EmailServer server, String from, String[] to, String subject, String content) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("noreply.funiverse@gmail.com", "%s via Funiverse".formatted(from));
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        javaMailSender.send(message);

    }

    @Override
    public void sendAnnouncement(Group group, List<UserDetail> userDetail, Post post) throws MessagingException, UnsupportedEncodingException {
        String from = group.getName();
        String[] to = userDetail.stream().map(UserDetail::getName).toArray(String[]::new);
        String subject = "You have new Announcement";
        String content = post.getContent();

        send(EmailServer.NOTIFICATION, from, to, subject, content);
    }

    public enum EmailServer {
        NOTIFICATION, SERVICES
    }
}