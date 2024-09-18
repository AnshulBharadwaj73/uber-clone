package com.codingbad.project.uber.uberApp.dto;

import com.codingbad.project.uber.uberApp.entities.enums.Role;
import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;

    private Set<Role> roles;
}
