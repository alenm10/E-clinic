package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.ClinicRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicRatingRepository extends JpaRepository<ClinicRating, String> {
    Optional<ClinicRating> findByClinic_IdAndPatient_Id(String clinicID, String patientID);
}
