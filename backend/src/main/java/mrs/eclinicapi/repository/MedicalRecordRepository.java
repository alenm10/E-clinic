package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, String> {

}
