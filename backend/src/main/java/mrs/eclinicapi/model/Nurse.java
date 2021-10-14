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
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class Nurse extends MedicalStaff {

    @Id
    @Column(length = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "n_seq")
    @GenericGenerator(name = "n_seq",
            strategy = "mrs.eclinicapi.generator.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "N")})
    private String id;

    @Version
    private long version;

    @OneToMany(mappedBy = "nurse", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Visit> visits;
    
    @Override
    public String toString() {
        return "Nurse{" +
                "id='" + id + '\'' +
                '}';
    }
}
