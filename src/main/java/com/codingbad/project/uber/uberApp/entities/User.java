package com.codingbad.project.uber.uberApp.entities;

import com.codingbad.project.uber.uberApp.entities.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "app_user",indexes = {
        @Index(name = "idx_user_email",columnList = "email")
})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // GenerationType.Sequence is mostly used for batch operation
    private Long id;
    @Column
    private String name;
    @Column(unique = true)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)               // we have EnumType.OrDINAL what it does is take admin as 0, driver as 1 and so on
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority("Role_"+role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return "";
    }
}
