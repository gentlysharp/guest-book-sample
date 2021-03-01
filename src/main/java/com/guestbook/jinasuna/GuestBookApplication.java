package com.guestbook.jinasuna;

import com.guestbook.jinasuna.guestbook.GuestbookRepository;
import com.guestbook.jinasuna.guestbook.Guestbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class GuestBookApplication {

    private static final Logger log = LoggerFactory.getLogger(GuestBookApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GuestBookApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(GuestbookRepository repository) {
        return (args) -> {
            repository.save(new Guestbook("hey everyone", "hahaha" ));
            repository.save(new Guestbook("Chloe", "O'Brian"));
            repository.save(new Guestbook("Kim", "Bauer"));
            repository.save(new Guestbook("David", "Palmer"));
            repository.save(new Guestbook("Michelle", "Dessler"));

            // fetch all guestbooks
            log.info("guestbooks found with findAll():");
            log.info("-------------------------------");
            for (Guestbook guestbook : repository.findAll()) {
                log.info(guestbook.toString());
            }
            log.info("");

            // fetch an individual guestbook by ID
            Optional<Guestbook> guestbook = repository.findById(1L);
            log.info("guestbook found with findById(1L):");
            log.info("--------------------------------");
            log.info(guestbook.toString());
            log.info("");
        };
    }


}
