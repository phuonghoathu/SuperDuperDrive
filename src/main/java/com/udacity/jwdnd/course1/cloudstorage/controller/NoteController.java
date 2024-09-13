package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
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
@RequestMapping("/note")
@AllArgsConstructor
public class NoteController {
    final NoteService noteService;
    final UserService userService;

    @PostMapping
    public String upsertNote(Note note,
                          RedirectAttributes redirectAttributes) {
        String message = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByName(authentication.getName());
        if(note.getNoteId() != null) {
            note.setUserId(currentUser.getUserId());
            try {
                int updateId = noteService.updateNote(note);
                if (updateId <= 0) {
                    message = "Have error when update note.Please try again later";
                }
            } catch (IOException e) {
                message = "Have error when update note.Please try again later";
            }
        } else {
            note.setUserId(currentUser.getUserId());
            try {
                int createId = noteService.addNote(note);
                if (createId < 0) {
                    message = "Have error when add note.Please try again later";
                }
            } catch (IOException e) {
                message = "Have error when add note.Please try again later";
            }
        }

        if (!message.isEmpty()) {
            redirectAttributes.addFlashAttribute("errMsg", message);
            return "redirect:/home/result?error";
        }

        return "redirect:/home/result?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByName(authentication.getName());
        int deleteId = noteService.deleteNote(id, currentUser.getUserId());
        if (deleteId == 0) {
            redirectAttributes.addFlashAttribute("errMsg", "Not exist file or not permission on it");
            return "redirect:/home/result?error";
        }
        return "redirect:/home/result?success";
    }
}
