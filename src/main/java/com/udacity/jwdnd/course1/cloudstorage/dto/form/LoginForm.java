package com.udacity.jwdnd.course1.cloudstorage.dto.form;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginForm {
    String userName;
    String password;
}
