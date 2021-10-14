package mrs.eclinicapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mrs.eclinicapi.model.Clinic;
import mrs.eclinicapi.model.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TokenResponse {
    private String accessToken;
    private User loggedUser;
    private Clinic clinic;
}
