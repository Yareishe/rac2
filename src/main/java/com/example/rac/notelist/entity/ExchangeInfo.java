package com.example.rac.notelist.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exchange_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exchange_name", nullable = false)
    private String exchangeName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "apikey", nullable = false)
    private String apikey;

    @Column(name = "secretapi", nullable = false)
    private String secretapi;

    @Column(name = "extra_info")
    private String extraInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
