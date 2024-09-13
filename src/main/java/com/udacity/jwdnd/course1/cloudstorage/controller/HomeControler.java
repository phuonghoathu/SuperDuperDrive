package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/home")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeControler {

    final FileService fileService;
    final NoteService noteService;
    final CredentialService credentialService;
    final EncryptionService encryptionService;

    @GetMapping
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("lstFile",fileService.getAllFileByUsername(authentication.getName()));
        model.addAttribute("lstNote",noteService.getAllNoteByUsername(authentication.getName()));

        model.addAttribute("lstCred",credentialService.getAllCredentialByUsername(authentication.getName()));
        model.addAttribute("encrypt",encryptionService);

        return "home";
    }

    @GetMapping("/result")
    public String showResult() {
        return "result";
    }
}
