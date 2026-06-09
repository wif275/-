package diplom.com.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import diplom.com.domain.User;
import diplom.com.service.UserService;



@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult bindingResult, Map<String, Object> model) {
        if (bindingResult.hasErrors()) {
            model.put("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            model.put("user", user);
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.put("message", "Пользователь существует");
            return "registration";
        }
        
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfuly activated");
        } else {
            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }
}
