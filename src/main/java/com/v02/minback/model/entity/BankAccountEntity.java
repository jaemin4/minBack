package com.v02.minback.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_bank_account")
public class BankAccountEntity {



    @Id
    @Column(length = 55, nullable = false, unique = true)
    private String bankAccountId;
    private Long accountNumber;

    @Column(length = 55)
    private String userId;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

    public BankAccountEntity(Long accountNumber, String userId) {
        this.accountNumber = accountNumber;
        this.userId = userId;
    }

    public BankAccountEntity(String bankAccountId, Long accountNumber, String userId) {
        this.bankAccountId = bankAccountId;
        this.accountNumber = accountNumber;
        this.userId = userId;
    }
}
