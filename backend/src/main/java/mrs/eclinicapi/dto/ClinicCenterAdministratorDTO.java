package mrs.eclinicapi.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mrs.eclinicapi.model.ClinicalCentarAdministrator;

@Getter
@Setter
@AllArgsConstructor
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class ClinicCenterAdministratorDTO extends UserDTO {

    public ClinicCenterAdministratorDTO(ClinicalCentarAdministrator clinicalCentarAdministrator) {
        super();
        this.id = clinicalCentarAdministrator.getId();
        this.password = clinicalCentarAdministrator.getUser().getPassword();
        this.surname = clinicalCentarAdministrator.getUser().getSurname();
        this.name = clinicalCentarAdministrator.getUser().getName();
        this.type = clinicalCentarAdministrator.getUser().getType();
        this.email = clinicalCentarAdministrator.getUser().getEmail();
        this.city = clinicalCentarAdministrator.getUser().getCity();
        this.country = clinicalCentarAdministrator.getUser().getCountry();
        this.userID = clinicalCentarAdministrator.getUser().getId();
        this.address = clinicalCentarAdministrator.getUser().getAddress();
        this.phoneNumber = clinicalCentarAdministrator.getUser().getPhoneNumber();
        this.personalID = clinicalCentarAdministrator.getUser().getPersonalID(); //JMBG
    }
}
