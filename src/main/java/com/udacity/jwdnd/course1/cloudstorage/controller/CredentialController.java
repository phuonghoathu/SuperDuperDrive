package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/credential")
@AllArgsConstructor
public class CredentialController {
    final CredentialService credentialService;
    final UserService userService;

    @PostMapping
    public String upsertCredential(Credential credential,
                                   RedirectAttributes redirectAttributes) {
        String message = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByName(authentication.getName());
        if(credential.getCredentialId() != null) {
            credential.setUserId(currentUser.getUserId());
            try {
                int updateId = credentialService.updateCredential(credential);
                if (updateId < 0) {
                    message = "Have error when update credential.Please try again later";
                }
            } catch (IOException e) {
                message = "Have error when update credential.Please try again later";
            }
        } else {
            credential.setUserId(currentUser.getUserId());
            try {
                int createId = credentialService.addCredential(credential);
                if (createId < 0) {
                    message = "Have error when add credential.Please try again later";
                }
            } catch (IOException e) {
                message = "Have error when add credential.Please try again later";
            }
        }

        if (!message.isEmpty()) {
            redirectAttributes.addFlashAttribute("errMsg", message);
            return "redirect:/home/result?error";
        }

        return "redirect:/home/result?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteCredential(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByName(authentication.getName());
        int deleteId = credentialService.deleteCredential(id, currentUser.getUserId());
        if (deleteId == 0) {
            redirectAttributes.addFlashAttribute("errMsg", "Not exist file or not permission on it");
            return "redirect:/home/result?error";
        }
        return "redirect:/home/result?success";
    }
}
