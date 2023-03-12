package cgm.controller;


import cgm.model.dto.UserRegistrationDto;
import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
import cgm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final SecurityContextRepository securityContextRepository;


    @Autowired
    public AuthController(UserService userService, SecurityContextRepository securityContextRepository) {
        this.userService = userService;
        this.securityContextRepository = securityContextRepository;
    }

    @ModelAttribute(name ="userRegistrationDto")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping("/register")
    private String getRegister(Model model) {

        model.addAttribute("branches", BranchCode.values());
        model.addAttribute("adminRole", Role.ADMIN);
        model.addAttribute("managerRole", Role.MANAGER);
        model.addAttribute("userRole", Role.USER);

        return "auth-register";
    }

    @PostMapping("register")
    private String registerUser(@Valid @ModelAttribute(name = "userRegistrationDto") UserRegistrationDto userRegistrationDto, HttpServletRequest request,
                                HttpServletResponse response,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes
                                ){

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userRegistrationDto", userRegistrationDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDto", bindingResult);

            return "redirect:/auth/register";
        }


        userService.registerUser(userRegistrationDto, successfulAuth -> {

            SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
            SecurityContext context = strategy.createEmptyContext();
            context.setAuthentication(successfulAuth);

            strategy.setContext(context);

            securityContextRepository.saveContext(context, request, response);
        });

        return "redirect:/";

    }

    @GetMapping("/login")
    private String login() {
        return "auth-login";
    }


    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("bad_credentials", true);

        return "redirect:/auth/login";
    }




}
