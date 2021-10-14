package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.InterventionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterventionTypeRepository extends JpaRepository<InterventionType, String> {

    @Query("select i from InterventionType i where i.clinic.id = ?1")
    List<InterventionType> getClinicInterventionType(String id);
}
