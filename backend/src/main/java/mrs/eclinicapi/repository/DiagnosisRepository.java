package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, String> {

}
