package mrs.eclinicapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientSearchRequest {
    private String firstName;
    private String lastName;
    private String personalID;
}
