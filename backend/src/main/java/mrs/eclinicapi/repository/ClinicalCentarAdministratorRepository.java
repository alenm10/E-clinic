package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.ClinicalCentarAdministrator;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClinicalCentarAdministratorRepository
        extends JpaRepository<ClinicalCentarAdministrator, String> {
}
