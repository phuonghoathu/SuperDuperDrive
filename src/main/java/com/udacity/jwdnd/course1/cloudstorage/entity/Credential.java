package com.udacity.jwdnd.course1.cloudstorage.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Credential {
    Integer credentialId;
    String url;
    String username;
    String key;
    String password;
    Integer userId;
}
