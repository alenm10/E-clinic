package mrs.eclinicapi.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mrs.eclinicapi.generator.IdGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class ClinicalCentarAdministrator {

    @Id
    @Column(length = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cca_seq")
    @GenericGenerator(name = "cca_seq",
            strategy = "mrs.eclinicapi.generator.IdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "CCA")})
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private List<Codebook> codebook;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private List<Clinic> clinics;

    /*
     *
     * OVO JE VRLO PROBLEMATICAN DIO DOLJE
     *
     *
     * */


}
