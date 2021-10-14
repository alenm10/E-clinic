package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, String> {

    @Query("select d from VacationRequest d where d.clinic.id = ?1")
    List<VacationRequest> getVacationRequestForClinic(String id);

    @Query("select d from VacationRequest d where d.user.id = ?1")
    List<VacationRequest> getVacationRequestForUser(String id);
}
