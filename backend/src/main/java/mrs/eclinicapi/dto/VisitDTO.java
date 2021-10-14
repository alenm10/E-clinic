package mrs.eclinicapi.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mrs.eclinicapi.model.Diagnosis;
import mrs.eclinicapi.model.Medicine;
import mrs.eclinicapi.model.Nurse;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class VisitDTO {
    private String patientName;
    private String anamnesis;
    private String usageMethod;
    private String doctorName;
    private boolean certified;
    private String id;
    private String nurseId;
    Set<Diagnosis> diagnoses;
    Set<Medicine> medicines;

}
