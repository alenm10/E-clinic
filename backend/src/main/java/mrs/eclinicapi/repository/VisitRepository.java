package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, String> {

}
