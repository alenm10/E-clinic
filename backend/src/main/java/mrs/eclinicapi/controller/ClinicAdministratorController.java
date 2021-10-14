package mrs.eclinicapi.controller;

import mrs.eclinicapi.dto.ClinicAdministratorDto;
import mrs.eclinicapi.model.Clinic;
import mrs.eclinicapi.service.ClinicAdministratorService;
import mrs.eclinicapi.service.ClinicService;
import mrs.eclinicapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/clinicadministrator")
public class ClinicAdministratorController {

    @Autowired
    private ClinicAdministratorService service;
    @Autowired
    private UserService userService;
    @Autowired
    private ClinicService clinicService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ClinicAdministratorDto addClinicAdministrator(@RequestBody ClinicAdministratorDto clinicAdministratorDto) {
        Clinic c = clinicService.findOne(clinicAdministratorDto.getClinicID());
        if(c == null) return null;
        clinicAdministratorDto.setClinic(c);
        return service.addClinicAdministraor(clinicAdministratorDto);

    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteClinicAdministrator(@PathVariable("id") String id) {
        service.deleteById(id);
        return new ResponseEntity<>("deleted clinicAdministrator", HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ClinicAdministratorDto modifyClinic(@RequestBody ClinicAdministratorDto clinicAdministratorDto) {
        Clinic c = clinicService.findOne(clinicAdministratorDto.getClinicID());
        if(c == null) return null;
        clinicAdministratorDto.setClinic(c);
        return service.updateClinicAdministraor(clinicAdministratorDto);

    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<ClinicAdministratorDto>> getAllClinicAdmin() {
        List<ClinicAdministratorDto> clinicAdministratorsDto = service.findAll();
        if (clinicAdministratorsDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(clinicAdministratorsDto, HttpStatus.OK);
    }

}
