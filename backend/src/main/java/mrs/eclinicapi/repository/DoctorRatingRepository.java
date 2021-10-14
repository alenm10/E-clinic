package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.DoctorRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRatingRepository extends JpaRepository<DoctorRating, String> {
    Optional<DoctorRating> findByDoctor_IdAndPatient_Id(String doctorID, String patientID);
}
