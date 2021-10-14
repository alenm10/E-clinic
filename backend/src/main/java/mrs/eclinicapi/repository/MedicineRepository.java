package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, String> {

}
