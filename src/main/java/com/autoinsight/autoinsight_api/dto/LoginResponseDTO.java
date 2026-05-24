package com.autoinsight.autoinsight_api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String token;
    private String username;
    private String role;
    private String message;
}
