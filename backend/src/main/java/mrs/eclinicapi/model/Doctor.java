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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class Doctor extends MedicalStaff {

    @ManyToMany
    private List<InterventionType> specialties;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Intervention> interventions = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<AppointmentRequest> appointmentRequests = new ArrayList<>();
    @Id
    @Column(length = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_seq")
    @GenericGenerator(name = "d_seq",
            strategy = "mrs.eclinicapi.generator.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "D")})
    private String id;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DoctorRating> rating = new HashSet<>();
    @Version
    private long version;
    private LocalDateTime changeTime;

    @Override
    public Clinic getClinic() {
        return super.getClinic();
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id='" + id + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
