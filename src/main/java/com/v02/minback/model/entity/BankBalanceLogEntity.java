package com.v02.minback.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bank_balance_log")
public class BankBalanceLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String prevData;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String updatedData;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String classMethod;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

    public BankBalanceLogEntity(String prevData, String updatedData, String classMethod) {
        this.prevData = prevData;
        this.updatedData = updatedData;
        this.classMethod = classMethod;
    }

}
