package mrs.eclinicapi.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mrs.eclinicapi.model.Diagnosis;
import mrs.eclinicapi.model.Medicine;
import mrs.eclinicapi.model.Visit;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class MedicalRecordDTO {
    private double height;
    private double weight;
    private String bloodType;
    private Set<String> allergies;
    private Set<Medicine> medicines;
    private Set<Diagnosis> diagnoses;
    private Set<Visit> visits;
}
