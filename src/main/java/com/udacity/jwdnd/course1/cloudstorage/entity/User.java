package com.udacity.jwdnd.course1.cloudstorage.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class User{
    Integer userId;
    String username;
    String salt;
    String password;
    String firstName;
    String lastName;
}
