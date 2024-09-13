package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {
    final FileService fileService;
    final UserService userService;

    @PostMapping
    public String addFile(@RequestParam("fileUpload") MultipartFile file,
                          RedirectAttributes redirectAttributes) {
        String message = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByName(authentication.getName());
        if(file.isEmpty()) {
            message = "Please select a file to upload.";
        } else if (fileService.isDuplicateFileName(file.getOriginalFilename(), currentUser.getUserId())) {
            message = "File name exist, please try again with other file";
        } else {
            try {
                int fileId = fileService.addFile(file, currentUser.getUserId());
                if (fileId < 0) {
                    message = "Have error when upload file.Please try again later";
                }
            } catch (IOException e) {
                message = "Have error when upload file.Please try again later";
            }
        }
        if (!message.isEmpty()) {
            redirectAttributes.addFlashAttribute("errMsg", message);
            return "redirect:/home/result?error";
        }

        return "redirect:/home/result?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByName(authentication.getName());
        int deleteId = fileService.deleteFile(id, currentUser.getUserId());
        if (deleteId == 0) {
            redirectAttributes.addFlashAttribute("errMsg", "Not exist file or not permission on it");
            return "redirect:/home/result?error";
        }
        return "redirect:/home/result?success";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") int fileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByName(authentication.getName());
        File fileData = fileService.getFileById(fileId, currentUser.getUserId());

        if (fileData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileData.getContentType()));
        headers.setContentDispositionFormData("attachment", fileData.getFileName());
        headers.setContentLength(Long.parseLong(fileData.getFileSize()));

        return new ResponseEntity<>(fileData.getFileData(), headers, HttpStatus.OK);
    }
}
