package com.udacity.jwdnd.course1.cloudstorage.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Note {
    Integer noteId;
    String noteTitle;
    String noteDescription;
    Integer userId;
}
