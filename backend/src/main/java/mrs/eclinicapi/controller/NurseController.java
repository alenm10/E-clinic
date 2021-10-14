package mrs.eclinicapi.controller;

import mrs.eclinicapi.dto.DoctorNurseDTO;
import mrs.eclinicapi.model.*;
import mrs.eclinicapi.model.enums.UserType;
import mrs.eclinicapi.service.ClinicService;
import mrs.eclinicapi.service.NurseService;
import mrs.eclinicapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/nurse")
public class NurseController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NurseService service;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DoctorNurseDTO> addNurse(@RequestBody DoctorNurseDTO nurseDto) {
        Nurse newNurse = this.convertToEntity(nurseDto);
        Nurse added = service.addNurse(newNurse);



        return new ResponseEntity<>(this.convertToDTO(added), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DoctorNurseDTO> updateNurse(@RequestBody DoctorNurseDTO nurseDto) {
        Nurse toModify = this.convertToEntity(nurseDto);

        Nurse modified = service.addNurse(toModify);

        if (modified == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.convertToDTO(modified), HttpStatus.OK);
    }

    @GetMapping(path = "/user/{id}")
    public ResponseEntity<DoctorNurseDTO> getNurseByUserId(@PathVariable String id) {

        Nurse nurse = service.findByUserID(id);
        if (nurse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.convertToDTO(nurse), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteNurse(@PathVariable("id") String id) {


        Nurse nurse = service.findOne(id);
        if (nurse == null) {

            return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        }


        service.deleteById(id);
        return new ResponseEntity<>("nurse deleted", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DoctorNurseDTO>> getAll() {

        List<Nurse> nurseList = service.findAll();
        if (nurseList == null) {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(nurseList.stream().map(this::convertToDTO).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping(path = "/getNurseForClinic/{id}")
    public ResponseEntity<List<DoctorNurseDTO>> getNurseForClinic(@PathVariable("id") String id) {


        List<Nurse> nurseList = service.getNurseForClinic(id);
        if (nurseList == null) {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(nurseList.stream().map(this::convertToDTO).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DoctorNurseDTO> getNurse(@PathVariable("id") String id) {

        Nurse nurse = service.findOne(id);
        if (nurse == null) {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }


        return new ResponseEntity<>(this.convertToDTO(nurse), HttpStatus.OK);
    }

    private Nurse convertToEntity(DoctorNurseDTO doctorNurseDTO) {
        Nurse toAdd = new Nurse();
        Clinic foundClinic = clinicService.findOne(doctorNurseDTO.getClinicID());
        if (foundClinic == null) return null;

        toAdd.setClinic(foundClinic);
        User foundUser = userService.findOne(doctorNurseDTO.getUserID());
        User nurseUser;
        if (foundUser == null)
            nurseUser = new User(
                    doctorNurseDTO.getEmail(),
                    passwordEncoder.encode(doctorNurseDTO.getPassword()),
                    doctorNurseDTO.getName(),
                    doctorNurseDTO.getSurname(),
                    UserType.nurse,
                    doctorNurseDTO.getPhoneNumber(),
                    doctorNurseDTO.getAddress(),
                    doctorNurseDTO.getCity(),
                    doctorNurseDTO.getCountry(),
                    doctorNurseDTO.getPersonalID());
        else
            nurseUser = foundUser;

        toAdd.setUser(nurseUser);
        return toAdd;
    }

    private DoctorNurseDTO convertToDTO(Nurse nurse) {
        return new DoctorNurseDTO(
                nurse.getId(),
                nurse.getUser().getId(),
                nurse.getUser().getEmail(),
                null,
                nurse.getUser().getName(),
                nurse.getUser().getSurname(),
                nurse.getUser().getPhoneNumber(),
                nurse.getUser().getAddress(),
                nurse.getUser().getCity(),
                nurse.getUser().getCountry(),
                nurse.getUser().getPersonalID(),
                nurse.getClinic().getId(),
                null,
                null,
                null,
                nurse.getWorkingCalendar().getVacations().stream()
                        .map(oc -> new TimePeriod<>(oc.getStart().atStartOfDay(), oc.getEnd().atStartOfDay()))
                        .collect(Collectors.toList()),
                null,
                null,
                0
        );
    }


}
