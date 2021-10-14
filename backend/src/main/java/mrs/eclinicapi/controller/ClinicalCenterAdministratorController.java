package mrs.eclinicapi.controller;

import mrs.eclinicapi.dto.ClinicCenterAdministratorDTO;
import mrs.eclinicapi.model.ClinicalCentarAdministrator;
import mrs.eclinicapi.service.ClinicalCenterAdministratorService;
import mrs.eclinicapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/clinicalcenteradministrator")
public class ClinicalCenterAdministratorController {

    @Autowired
    private ClinicalCenterAdministratorService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ClinicCenterAdministratorDTO addClinicCenterAdministrator(@RequestBody ClinicCenterAdministratorDTO clinicCenterAdministratorDTO) {

        return service.addClinicCenterAdministrator(clinicCenterAdministratorDTO);

    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteClinicCenterAdministrator(@PathVariable("id") String id) {
        service.deleteById(id);
        return new ResponseEntity<>("deleted clinicCenterAdministrator", HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ClinicCenterAdministratorDTO modifyClinicCenterAdministrator(@RequestBody ClinicCenterAdministratorDTO clinicCenterAdministratorDTO) {
        return service.updateClinicCenterAdministrator(clinicCenterAdministratorDTO);

    }

    @GetMapping()
    public ResponseEntity<List<ClinicCenterAdministratorDTO>> getAllClinicCenterAdmins() {
        List<ClinicCenterAdministratorDTO> clinicAdministratorDtoList = service.findAll();
        if (clinicAdministratorDtoList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(clinicAdministratorDtoList, HttpStatus.OK);
    }

}
