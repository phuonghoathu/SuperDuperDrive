package com.udacity.jwdnd.course1.cloudstorage.dto.form;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SignUpForm {
    String username;
    String password;
    String firstName;
    String lastName;
}
