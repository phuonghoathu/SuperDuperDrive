package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileService {
    final FileMapper fileMapper;

    public List<File> getAllFileByUsername(String username) {
        return fileMapper.getAllFileByUser(username);
    }

    public File getFileById(int fileId, int userId) {
        return fileMapper.getFileById(fileId, userId);
    }

    public int addFile(MultipartFile file, int userId) throws IOException {
        return fileMapper.insert(File.builder().
                fileName(file.getOriginalFilename()).fileData(file.getBytes())
                .fileSize(Long.toString(file.getSize()))
                .contentType(file.getContentType())
                .userId(userId).build());
    }

    public boolean isDuplicateFileName(String fileName, int userId) {
        return fileMapper.getFileByName(fileName, userId) != null;
    }

    public int deleteFile(int fileId, int userId) {
        return fileMapper.delete(fileId, userId);
    }
}
