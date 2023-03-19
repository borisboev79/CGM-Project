package cgm.controller;


import cgm.model.CurrentUser;
import cgm.model.dto.UserRegistrationDto;
import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
import cgm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @ModelAttribute(name = "userRegistrationDto")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping("/register")
    private String getRegisterUser(@AuthenticationPrincipal CurrentUser currentUser, Model model) {

        model.addAttribute("firstName", currentUser.getFirstName());
        model.addAttribute("principalBranch", currentUser.getBranch());

        model.addAttribute("branches", BranchCode.values());

        model.addAttribute("adminRole", Role.ADMIN);
        model.addAttribute("managerRole", Role.MANAGER);
        model.addAttribute("userRole", Role.USER);

        return "users-register";
    }

    @PostMapping("/register")
    private String registerUser(@Valid @ModelAttribute(name = "userRegistrationDto") UserRegistrationDto userRegistrationDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userRegistrationDto", userRegistrationDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDto", bindingResult);

            return "redirect:/users/register";
        }
        userService.registerUser(userRegistrationDto);

        return "redirect:/";
    }


}
