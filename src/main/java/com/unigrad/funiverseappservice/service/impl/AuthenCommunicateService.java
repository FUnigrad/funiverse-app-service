package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.Workspace;
import com.unigrad.funiverseappservice.entity.socialnetwork.Role;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.payload.DTO.AuthenUserDTO;
import com.unigrad.funiverseappservice.service.IAuthenCommunicateService;
import com.unigrad.funiverseappservice.service.IWorkspaceService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthenCommunicateService implements IAuthenCommunicateService {

    private final Logger LOG = LoggerFactory.getLogger(AuthenCommunicateService.class);

    private final String AUTHEN_SERVICE_URL = "authen.system.funiverse.world";

    private final DTOConverter dtoConverter;

    private final IWorkspaceService workspaceService;

    private WebClient webClient;

    @Override
    public boolean saveUser(UserDetail user, String token) {
        setUrl(token);

        AuthenUserDTO authenUserDTO = dtoConverter.convert(user, AuthenUserDTO.class);
        authenUserDTO.setWorkspace(workspaceService.get());
        if (!user.isAdmin()) {
            authenUserDTO.setRole(Role.USER);
        }

        Mono<Boolean> isSuccessful =  webClient.post()
                .uri("/user")
                .bodyValue(authenUserDTO)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return Mono.just(true);
                    } else {
                        return Mono.just(false);
                    }
                });

        if (Boolean.TRUE.equals(isSuccessful.block())) {
            LOG.info("Save User into %s successful".formatted(AUTHEN_SERVICE_URL));
            return true;
        } else {
            LOG.error("An error occurs when calling to %s. Can not save Workspace Admin".formatted(AUTHEN_SERVICE_URL));
            return false;
        }
    }

    @Override
    public boolean inactiveUser(String email, String token) {
        setUrl(token);

        Mono<Boolean> isSuccessful =  webClient.delete()
                .uri("/user/%s".formatted(email))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return Mono.just(true);
                    } else {
                        return Mono.just(false);
                    }
                });

        if (Boolean.TRUE.equals(isSuccessful.block())) {
            LOG.info("Save User into %s successful".formatted(AUTHEN_SERVICE_URL));
            return true;
        } else {
            LOG.error("An error occurs when calling to %s. Can not save Workspace Admin".formatted(AUTHEN_SERVICE_URL));
            return false;
        }
    }

    @Override
    public boolean activeWorkspace(String token) {
        setUrl(token);

        Workspace workspace = workspaceService.get();

        Mono<Boolean> isSuccessful =  webClient.put()
                .uri("/workspace/%s".formatted(workspace.getId()))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return Mono.just(true);
                    } else {
                        return Mono.just(false);
                    }
                });

        if (Boolean.TRUE.equals(isSuccessful.block())) {
            LOG.info("Active Workspace successful");
            return true;
        } else {
            LOG.error("An error occurs when calling to %s. Can not active Workspace".formatted(AUTHEN_SERVICE_URL));
            return false;
        }
    }

    private void setUrl(String token) {
        try {
            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .secure(t -> t.sslContext(sslContext))
                    .responseTimeout(Duration.ofSeconds(3600));

            webClient = WebClient.builder()
                    .baseUrl("http://" + AUTHEN_SERVICE_URL)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, token)
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();

        } catch (SSLException e) {
            throw new RuntimeException(e);
        }
    }
}