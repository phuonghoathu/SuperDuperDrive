package com.udacity.jwdnd.course1.cloudstorage.entites;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class User{
    Integer userId;
    String username;
    String salt;
    String password;
    String firstName;
    String lastName;
}
