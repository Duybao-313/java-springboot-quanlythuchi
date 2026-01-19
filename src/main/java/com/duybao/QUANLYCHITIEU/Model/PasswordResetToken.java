//package com.duybao.QUANLYCHITIEU.Model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import java.time.LocalDateTime;
//
//@Entity
//@Table
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class PasswordResetToken {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    private String token;
//
//    private LocalDateTime expirationDate;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    // Getters and Setters
//}