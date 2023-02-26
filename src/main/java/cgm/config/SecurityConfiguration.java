package cgm.config;


import cgm.model.enums.Role;
import cgm.repository.UserRepository;
import cgm.service.ApplicationUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
                        authorizeHttpRequests().
                requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().
                requestMatchers("/", "/auth/login", "/groups/all").permitAll().
                requestMatchers("/auth/register").hasRole(Role.MANAGER.name()).
                requestMatchers("/groups", "/groups/add", "/groups/delete", "auth/modify").hasRole(Role.ADMIN.name()).
                anyRequest().authenticated().

                and().
                formLogin().
                loginPage("/auth/login").
                usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
                passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
                defaultSuccessUrl("/groups/all", true).
                failureForwardUrl("/auth/login")

                .and()
                .logout()
                .logoutSuccessUrl("/auth/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

               /* .permitAll()
                .and()
                .rememberMe()
                .rememberMeParameter("remember")
                .key("remember Me Encryption Key")
                .rememberMeCookieName("rememberCrusitCookie")
                .tokenValiditySeconds(10000);*/

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new ApplicationUserDetailsService(userRepository);
    }
}
