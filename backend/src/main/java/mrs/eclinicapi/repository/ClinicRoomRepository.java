package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.ClinicRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicRoomRepository extends JpaRepository<ClinicRoom, String> {
    Page<ClinicRoom> findAllByClinic_Id(String clinicID, Pageable p);

    List<ClinicRoom> findAllByClinic_Id(String clinicID);
}
