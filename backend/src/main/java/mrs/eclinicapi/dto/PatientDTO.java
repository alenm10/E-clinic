package mrs.eclinicapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mrs.eclinicapi.model.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
    private String id;
    private String userID;
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String personalID;
    private List<Clinic> clinics;
    private List<Intervention> interventions;
    private List<AppointmentRequest> requests;
    private MedicalRecord medicalRecord;
    private Set<DoctorRating> doctorRatings;
    private Set<ClinicRating> clinicRatings;
}
