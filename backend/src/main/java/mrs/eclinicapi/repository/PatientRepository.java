package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, String> {
    Patient findPatientByUser_Id(String userID);
}
