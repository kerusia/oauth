package com.withsw.oauth.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoogleProfileDto {
    private String sub;
    private String email;
    @JsonProperty("email_verified")
    private boolean emailVerified;
    private String picture;
    private String name;
}
