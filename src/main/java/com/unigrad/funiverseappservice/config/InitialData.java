package com.unigrad.funiverseappservice.config;

import com.unigrad.funiverseappservice.entity.socialnetwork.Role;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialData implements CommandLineRunner {

    private final IUserDetailService userService;

    @Override
    public void run(String... args) throws Exception {
        if (userService.findByPersonalMail("funigrad2023@gmail.com").isEmpty()) {
            UserDetail user = UserDetail.builder()
                    .name("System Admin")
                    .eduMail("funigrad2023@gmail.com")
                    .personalMail("funigrad2023@gmail.com")
                    .isActive(true)
                    .role(Role.SYSTEM_ADMIN)
                    .build();

            userService.save(user);
        }
    }
}