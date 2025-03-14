package com.pro.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_auth_refresh")
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ 수정됨
    private Long id;

    @Column(length = 255)
    private String email;

    private String refreshToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refreshExpiration;

    private String accessToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime accessExpiration;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

    public RefreshEntity(String email, String refreshToken, LocalDateTime refreshExpiration, String accessToken, LocalDateTime accessExpiration) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.refreshExpiration = refreshExpiration;
        this.accessToken = accessToken;
        this.accessExpiration = accessExpiration;
    }



}
