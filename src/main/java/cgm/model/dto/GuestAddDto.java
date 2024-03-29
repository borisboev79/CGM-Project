package cgm.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Override
    public String toString() {
        return "GuestAddDto{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDate=" + birthDate +
                ", EGN='" + EGN + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                '}';
    }
}
