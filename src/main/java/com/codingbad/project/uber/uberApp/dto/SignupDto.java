package com.codingbad.project.uber.uberApp.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupDto {

    private String name;
    private String email;
    private String password;
}
