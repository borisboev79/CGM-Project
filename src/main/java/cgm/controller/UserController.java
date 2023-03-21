package cgm.controller;


import cgm.model.CurrentUser;
import cgm.model.ObjectNotFoundException;
import cgm.model.dto.UserModificationDto;
import cgm.model.dto.UserRegistrationDto;
import cgm.model.dto.UserViewDto;
import cgm.model.entity.UserEntity;
import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
import cgm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

    @ModelAttribute(name = "userModificationDto")
    public UserModificationDto userModificationDto() {
        return new UserModificationDto();
    }

    //REGISTER USER

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
        this.userService.registerUser(userRegistrationDto);

        return "redirect:/";
    }

    //VIEW ALL USERS

    @GetMapping("/all")
    public String getUsersList(@AuthenticationPrincipal CurrentUser currentUser, Model model) {

        model.addAttribute("firstName", currentUser.getFirstName());

        List<UserViewDto> allUsers = this.userService.getAllUsers();

        model.addAttribute("allUsers", allUsers);

        return "users-all";
    }


    //MODIFY USER

    @GetMapping("/modify/{id}")
    public String getModifyUser(@PathVariable Long id, @AuthenticationPrincipal CurrentUser currentUser, Model model) {

        model.addAttribute("firstName", currentUser.getFirstName());

        UserModificationDto userModificationDto = this.userService.getUserById(id);

        if (userModificationDto == null) {

            throw new ObjectNotFoundException(id, "user");
        }

        model.addAttribute("userModificationDto", userModificationDto);
        model.addAttribute("userBranch", userModificationDto.getBranch().name());

        model.addAttribute("branches", BranchCode.values());

        model.addAttribute("adminRole", Role.ADMIN);
        model.addAttribute("managerRole", Role.MANAGER);
        model.addAttribute("userRole", Role.USER);

        return "users-modify";
    }

    @GetMapping("/errors/{id}")
    public String getErrorsModifying(@PathVariable Long id, @AuthenticationPrincipal CurrentUser currentUser,
                                     Model model){

        model.addAttribute("firstName", currentUser.getFirstName());


        model.addAttribute("branches", BranchCode.values());

        model.addAttribute("adminRole", Role.ADMIN);
        model.addAttribute("managerRole", Role.MANAGER);
        model.addAttribute("userRole", Role.USER);

        return "users-modify";
    }

    @PutMapping("/modify/{id}")
    public String modifyUser(@Valid @ModelAttribute(name = "userModificationDto") UserModificationDto userModificationDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @PathVariable Long id
    ) {


        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userModificationDto", userModificationDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userModificationDto", bindingResult);

            return "redirect:/users/errors/" + id;
        }

        this.userService.modifyUser(userModificationDto, id);


        return "redirect:/users/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {

        UserEntity user = this.userService.findById(id);

        if (user == null) {

            throw new ObjectNotFoundException(id, "user");
        }

        this.userService.deleteUser(user);

        return "redirect:/users/all";
    }


}
