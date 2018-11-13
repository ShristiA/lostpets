package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class HomeController {
@Autowired
MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        model.addAttribute("user", user);
        if(result.hasErrors())
        {
            return "registration";
        }
        else
        {
            userService.saveUser(user);
            model.addAttribute("message","User Account Created");
        }
        return "login";
    }
    @RequestMapping("/login")
    public String index(){

        return"login";
    }

@RequestMapping("/")
    public String allpets(Model model){
    model.addAttribute("messages", messageRepository.findAll());
    if(getUser() != null){
        model.addAttribute("user_id", getUser().getId()); //getting user name in the userid.
    }
    return "list";
}


    @RequestMapping("/foundpets")
    public String showfoundpets(Model model){
        model.addAttribute("messages", messageRepository.findAll());

            model.addAttribute("user_id", getUser().getId()); //getting user name in the userid.

        return "foundpets";
    }
    @RequestMapping("/lostpets")
    public String showlostpets(Model model){
        model.addAttribute("messages", messageRepository.findAll());

            model.addAttribute("user_id", getUser().getId()); //getting user name in the userid.

        return "lostpets";
    }

@GetMapping("/add")
public String courseForm(Model model){
    model.addAttribute("message", new Message());
    return "messageform";
}
@PostMapping("/process")
    public String processForm(@Valid Message message, BindingResult result) {
    if (result.hasErrors()) {
        return "messageform";
    }
    message.setUser(getUser()); //like saving a value of userid in message table.
    messageRepository.save(message);
    return "redirect:/";
}

@RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model)
{
    model.addAttribute("message", messageRepository.findById(id).get());
    return "show";
}
@RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model){
    model.addAttribute("message", messageRepository.findById(id).get());
    return "messageform";

}
    protected User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userRepository.findByUserName(currentUsername);
        return user;
    }

}
