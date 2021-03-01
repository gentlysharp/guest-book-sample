package com.guestbook.jinasuna.guestbook;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;



@Entity
public @Data class Guestbook {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long seq;

    private String subject;
    private String content;
    private LocalDateTime createdAt;

    public Guestbook(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public Guestbook() {
    }

    @PrePersist
    public void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

}
