package cgm.model.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestInitialViewDto {

    private Long id;
    private Long cabinId;
    private String fullName;
    private Instant birthDate;
    private Integer age;
    private String EGN;
    private String passportNumber;
    private String email;
    private String phone;









}
