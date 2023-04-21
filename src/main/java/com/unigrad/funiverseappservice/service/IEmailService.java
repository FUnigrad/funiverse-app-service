package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.service.impl.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public interface IEmailService {

    void send(EmailService.EmailServer server, String from, String[] to, String subject, String text) throws MessagingException, UnsupportedEncodingException;

    void sendAnnouncement(Group group, List<UserDetail> users, Post post) throws MessagingException, UnsupportedEncodingException;
}