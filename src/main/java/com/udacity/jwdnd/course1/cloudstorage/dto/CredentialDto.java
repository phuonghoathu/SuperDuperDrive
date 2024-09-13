package com.udacity.jwdnd.course1.cloudstorage.dto;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CredentialDto extends Credential {
    private String passEncode;

    public CredentialDto(Integer credentialId, String url, String username, String key, String password, Integer userId) {
        super(credentialId, url, username, key, password, userId);
    }
}
