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

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class VacationRequest {

    @Id
    @Column(length = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vr_seq")
    @GenericGenerator(name = "vr_seq",
            strategy = "mrs.eclinicapi.generator.IdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "VR")})
    private String id;

    @OneToOne
    private User user;

    @Version
    private long version;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Clinic clinic;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String status;

    private String reason;
    
    @Override
    public String toString() {
        return "VacationRequest [id=" + id + ", user=" + user.getId() + ", clinic=" + clinic.getId() + ", startDate=" + startDate
                + ", endDate=" + endDate + ", status=" + status + ", reason=" + reason + "]";
    }


}
