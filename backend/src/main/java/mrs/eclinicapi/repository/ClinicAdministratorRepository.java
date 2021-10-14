package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.ClinicAdministrator;
import mrs.eclinicapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicAdministratorRepository extends JpaRepository<ClinicAdministrator, String> {
    Optional<ClinicAdministrator> findClinicAdministratorByUser(User var1);

}
