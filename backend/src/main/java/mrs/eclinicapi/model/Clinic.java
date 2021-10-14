package mrs.eclinicapi.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mrs.eclinicapi.generator.IdGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class Clinic {

    @Id
    @Column(length = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "c_seq")
    @GenericGenerator(name = "c_seq",
            strategy = "mrs.eclinicapi.generator.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "C")})
    private String id;

    private String name;
    private String description;
    private String address;

    @Embedded
    private Coordinates coordinates;

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ClinicAdministrator> clinicAdministrator;

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ClinicRoom> clinicRoom = new HashSet<>();

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Doctor> doctors = new HashSet<>();

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Nurse> nurses = new HashSet<>();

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Intervention> interventions = new HashSet<>();

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<VacationRequest> vacationRequests = new HashSet<>();

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<InterventionType> interventionTypes = new HashSet<>();

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AppointmentRequest> appointmentRequests = new HashSet<>();

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Intervention> oneClicks = new HashSet<>();

    @ManyToMany
    private Set<Patient> patients;

    @ManyToOne
    private ClinicalCentarAdministrator admin;

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ClinicRating> rating;

    @Version
    private long version;

    public void addPatient(Patient p) {
        if(patients == null) patients = new HashSet<>();
        patients.add(p);
    }
    @Override
    public String toString() {
        return "Clinic{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
