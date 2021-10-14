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
public class MedicalRecord {
    @Id
    @Column(length = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mr_seq")
    @GenericGenerator(name = "mr_seq",
            strategy = "mrs.eclinicapi.generator.IdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "MR")})
    private String id;

    private double height;
    private double weight;
    private String bloodType;
    @ElementCollection
    private Set<String> allergies;

    @OneToOne(mappedBy = "medicalRecord")
    private Patient patient;

    @ManyToMany
    private Set<Diagnosis> diagnoses;

    @OneToMany
    private Set<Visit> visits;

    @ManyToMany
    private Set<Medicine> medicines;

    public Patient getPatient() {
        return this.patient;
    }

    public void addVisit(Visit v) {
        if(this.visits == null) this.visits = new HashSet<>();
        this.visits.add(v);
    }

    public void addDiagnosis(Diagnosis d) {
        if(this.diagnoses == null) this.diagnoses = new HashSet<>();
        this.diagnoses.add(d);
    }

    public void addDiagnoses(Set<Diagnosis> d) {
        if(this.diagnoses == null) this.diagnoses = new HashSet<>();
        this.diagnoses.addAll(d);
    }

    public void addMedicine(Medicine m) {
        if(this.medicines == null) this.medicines = new HashSet<>();
        this.medicines.add(m);
    }

    public void addMedicines(Set<Medicine> m) {
        if(this.medicines == null) this.medicines = new HashSet<>();
        this.medicines.addAll(m);
    }
}
