package com.shareit.data.repository;

import com.shareit.domain.entity.MediaEntity;
import com.shareit.domain.entity.MediaType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile({"test", "dev"})
public class DbSeeder implements CommandLineRunner {
    private final MediaRepository mediaRepository;

    private final String strategy;


    public DbSeeder(MediaRepository mediaRepository, Environment environment) {
        this.mediaRepository = mediaRepository;
        this.strategy = environment.getProperty("spring.jpa.hibernate.ddl-auto","none");
    }

    @Override
    public void run(String... args) throws Exception {
        if(!"create".equals(strategy)) {
            return;
        }
        mediaRepository.save(new MediaEntity(1L, MediaType.IMAGE, "test.png", "123123"));
    }
}
