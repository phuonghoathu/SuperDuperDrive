package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.form.SignUpForm;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpController {

    final UserService useService;

    @GetMapping
    public String index(SignUpForm signUpForm, Model model) {
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute("signUpForm") SignUpForm signUpForm, Model model
            , RedirectAttributes redirectAttributes) {
        User existUser = useService.getUserByName(signUpForm.getUsername());
        if (existUser != null) {
            model.addAttribute("msgError", "User Name exist. Please using other username");
        } else {
            int userAddId = useService.createUser(User.builder().firstName(signUpForm.getFirstName())
                    .lastName(signUpForm.getLastName())
                    .password(signUpForm.getPassword())
                    .username(signUpForm.getUsername()).build());
            if (userAddId < 0) {
                model.addAttribute("msgError", "There was an error signing you up. Please try again.");
            } else {
                redirectAttributes.addFlashAttribute("signUpOk", true);
                return "redirect:/login";
            }
        }

        return "signup";
    }
}
