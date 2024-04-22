package com.konasl.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private String password;

    private boolean approvalOfAdmin;

    public User( String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.approvalOfAdmin = false;
    }
}
