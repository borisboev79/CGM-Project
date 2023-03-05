package cgm.model.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GuestAddDto {

    @NotBlank
    @Size(min = 7)
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @Size(min = 8)
    @NotBlank
    private String phone;

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotBlank
    @Size(min = 10, max = 10)
    private String EGN;

    @NotBlank
    private String passportNumber;

}
