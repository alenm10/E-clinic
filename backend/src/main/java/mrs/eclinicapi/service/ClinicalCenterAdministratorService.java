package mrs.eclinicapi.service;

import mrs.eclinicapi.dto.ClinicCenterAdministratorDTO;
import mrs.eclinicapi.model.ClinicalCentarAdministrator;
import mrs.eclinicapi.model.User;
import mrs.eclinicapi.repository.ClinicalCentarAdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClinicalCenterAdministratorService {

    private ClinicalCentarAdministratorRepository clinicalCentarAdministratorRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ClinicalCenterAdministratorService(ClinicalCentarAdministratorRepository clinicalCentarAdministratorRepository, PasswordEncoder passwordEncoder) {
        this.clinicalCentarAdministratorRepository = clinicalCentarAdministratorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ClinicCenterAdministratorDTO addClinicCenterAdministrator(ClinicCenterAdministratorDTO clinicCenterAdministratorDTO) {
        User newUser = new User(clinicCenterAdministratorDTO);
        newUser.setFirstPassword(passwordEncoder.encode(newUser.getPassword()));
        ClinicalCentarAdministrator clinicalCentarAdministrator = new ClinicalCentarAdministrator();
        clinicalCentarAdministrator.setUser(newUser);


        return new ClinicCenterAdministratorDTO(clinicalCentarAdministratorRepository.save(clinicalCentarAdministrator));
    }

    public ClinicCenterAdministratorDTO updateClinicCenterAdministrator(ClinicCenterAdministratorDTO clinicCenterAdministratorDTO) {
        User newUser = new User(clinicCenterAdministratorDTO);
        newUser.setId(clinicCenterAdministratorDTO.getUserID());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        ClinicalCentarAdministrator clinicalCentarAdministrator = new ClinicalCentarAdministrator();
        clinicalCentarAdministrator.setUser(newUser);
        clinicalCentarAdministrator.setId(clinicCenterAdministratorDTO.getId());
        return new ClinicCenterAdministratorDTO(clinicalCentarAdministratorRepository.save(clinicalCentarAdministrator));

    }

    public ClinicalCentarAdministrator findById(String id) {
        return clinicalCentarAdministratorRepository.findById(id).orElse(null);
    }

    public void deleteById(String id) {
        clinicalCentarAdministratorRepository.deleteById(id);
    }


    public List<ClinicCenterAdministratorDTO> findAll() {
        List<ClinicalCentarAdministrator> clinicalCentarAdministrators = clinicalCentarAdministratorRepository.findAll();

        List<ClinicCenterAdministratorDTO> clinicCenterAdministratorDTOList = new ArrayList<>();
        for (ClinicalCentarAdministrator c : clinicalCentarAdministrators) {
            ClinicCenterAdministratorDTO cdto = new ClinicCenterAdministratorDTO(c);
            clinicCenterAdministratorDTOList.add(cdto);
        }
        return clinicCenterAdministratorDTOList;

    }
}
