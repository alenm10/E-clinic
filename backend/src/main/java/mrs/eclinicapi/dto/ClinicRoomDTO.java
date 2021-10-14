package mrs.eclinicapi.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mrs.eclinicapi.model.Intervention;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = JSOGGenerator.class)

public class ClinicRoomDTO {
    private String id;
    private String name;
    private String clinicId;
    private Set<Intervention> interventions;
    private boolean removable = true;

}
