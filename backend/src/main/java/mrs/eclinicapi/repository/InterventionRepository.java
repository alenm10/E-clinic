package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.Intervention;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InterventionRepository extends JpaRepository<Intervention, String> {

    @Query("select i from Intervention i where i.clinic.id = ?1")
    List<Intervention> getClinicIntervention(String id);

    List<Intervention> findInterventionsByDoctor_Id(String doctorID);

    List<Intervention> findInterventionsByPatient_IdAndVisitNotNull(String id);

    List<Intervention> findInterventionsByDoctor_IdAndPatient_Id(String doctorID, String patientID);

    @Query("select i from Intervention i where i.interventionType.id = ?1")
    List<Intervention> findInterventionsByInterventionType(String id);

    List<Intervention> findAllByClinicIdAndPatientNull(String clinicId);

    Page<Intervention> findAllByClinicIdAndPatientNull(String clinicId, Pageable p);

    Optional<Intervention> findByIdAndPatientNull(String id);

}
