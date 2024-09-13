package com.udacity.jwdnd.course1.cloudstorage.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class File {
    Integer fileId;
    String fileName;
    String contentType;
    String fileSize;
    Integer userId;
    byte[] fileData;
}
