package mrs.eclinicapi.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mrs.eclinicapi.model.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class ClinicDTO {
    private String id;
    private String name;
    private String description;
    private String address;
    private Coordinates coordinates;
    private Set<ClinicAdministrator> clinicAdministrator;
    private Set<ClinicRoom> clinicRoom = new HashSet<>();
    private Set<Doctor> doctors = new HashSet<>();
    private Set<Nurse> nurses = new HashSet<>();
    private Set<Intervention> interventions = new HashSet<>();
    private Set<VacationRequest> vacationRequests = new HashSet<>();
    private Set<InterventionType> interventionTypes = new HashSet<>();
    private Set<AppointmentRequest> appointmentRequests = new HashSet<>();
    private Set<Intervention> oneClicks = new HashSet<>();
    private Set<Patient> patients;
    private ClinicalCentarAdministrator admin;
    private double rating;
}
