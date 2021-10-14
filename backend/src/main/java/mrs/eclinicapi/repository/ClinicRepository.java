package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, String> {

}

