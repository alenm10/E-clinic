package mrs.eclinicapi.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mrs.eclinicapi.dto.MedicalRecordDTO;
import mrs.eclinicapi.dto.PatientDTO;
import mrs.eclinicapi.dto.PatientSearchRequest;
import mrs.eclinicapi.model.MedicalRecord;
import mrs.eclinicapi.model.Patient;
import mrs.eclinicapi.service.PatientService;
import mrs.eclinicapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/patient")
public class PatientController {

    @Autowired
    private PatientService service;

    @Autowired
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> getPatients() {

        List<Patient> patients = service.getPatients();

        if (patients == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> getPatient(@PathVariable String id) {
        Patient p = service.getPatientById(id);
        if (p == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(this.convertToDto(p), HttpStatus.OK);
    }

    @GetMapping(path = "/{pageNumber}/{pageSize}/{sort}/{desc}")
    public ResponseEntity<PagedResponse> getPagedPatients(@PathVariable int pageNumber,
                                                          @PathVariable int pageSize,
                                                          @PathVariable String sort,
                                                          @PathVariable String desc) {
        PagedResponse response;
        if (pageSize < 1) {
            List<Patient> allPatients = service.getPatients();
            response = new PagedResponse(allPatients.stream().map(this::convertToDto).collect(Collectors.toList()),
                    allPatients.size());

        } else {
            Page<Patient> patientPage;
            if (sort.equals("undefined"))
                patientPage = service.findPaged(pageNumber, pageSize);
            else {
                patientPage = service.findPaged(pageNumber, pageSize, sort, desc.equals("true"));
            }
            response = new PagedResponse(
                    patientPage.getContent().stream().map(this::convertToDto).collect(Collectors.toList()),
                    patientPage.getTotalElements());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/search/{pageNumber}/{pageSize}/{sort}/{desc}")
    public ResponseEntity<PagedResponse> searchPatients(@RequestBody PatientSearchRequest searchRequest,
                                                        @PathVariable int pageNumber,
                                                        @PathVariable int pageSize,
                                                        @PathVariable String sort,
                                                        @PathVariable String desc) {
        String firstName = searchRequest.getFirstName();
        String lastName = searchRequest.getLastName();
        String personalID = searchRequest.getPersonalID();
        if (firstName == null) firstName = "";
        if (lastName == null) lastName = "";
        if (personalID == null) personalID = "";

        PagedResponse response;
        Page<Patient> patientPage;
        if (sort.equals("undefined"))
            patientPage = service.search(firstName, lastName, personalID, pageNumber, pageSize);
        else {
            sort = "user." + sort;
            patientPage = service.search(firstName, lastName, personalID, pageNumber, pageSize, sort, desc.equals("true"));
        }
        response = new PagedResponse(patientPage.getContent()
                .stream().map(this::convertToDto).collect(Collectors.toList()), patientPage.getTotalElements());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/medical-record/{userID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalRecordDTO> getMedicalRecord(@PathVariable String userID) {
        Patient found = service.getByUserId(userID);
        if (found == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        MedicalRecord medicalRecord = found.getMedicalRecord();
        if (medicalRecord == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO(
                medicalRecord.getHeight(),
                medicalRecord.getWeight(),
                medicalRecord.getBloodType(),
                medicalRecord.getAllergies(),
                medicalRecord.getMedicines(),
                medicalRecord.getDiagnoses(),
                medicalRecord.getVisits()
        );
        return new ResponseEntity<>(medicalRecordDTO, HttpStatus.OK);
    }

    @PostMapping(path = "/medical-record/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> createMedicalRecord(@PathVariable String id, @RequestBody MedicalRecordDTO medicalRecordDTO) {
        Patient found = service.getByUserId(id);
        if (found == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        MedicalRecord newMedicalRecord = new MedicalRecord();

        newMedicalRecord.setHeight(medicalRecordDTO.getHeight());
        newMedicalRecord.setWeight(medicalRecordDTO.getWeight());
        newMedicalRecord.setBloodType(medicalRecordDTO.getBloodType());
        newMedicalRecord.setAllergies(medicalRecordDTO.getAllergies());
        found.setMedicalRecord(newMedicalRecord);

        Patient newPatient = service.addPatient(found);

        return new ResponseEntity<>(this.convertToDto(newPatient), HttpStatus.OK);
    }

    @PutMapping(path = "/medical-record/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> updateMedicalRecord(@PathVariable String id, @RequestBody MedicalRecordDTO medicalRecordDTO) {
        Patient found = service.getByUserId(id);
        if (found == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        MedicalRecord mr = found.getMedicalRecord();
        if(mr == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        mr.setHeight(medicalRecordDTO.getHeight());
        mr.setWeight(medicalRecordDTO.getWeight());
        mr.setBloodType(medicalRecordDTO.getBloodType());
        mr.setAllergies(medicalRecordDTO.getAllergies());
        found.setMedicalRecord(mr);

        Patient newPatient = service.addPatient(found);

        return new ResponseEntity<>(this.convertToDto(newPatient), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<PatientDTO> deletePatient(@PathVariable String id) {
        try {
            Patient p = service.deletePatient(id);
            return new ResponseEntity<>(this.convertToDto(p), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping(path = "/user/{userID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> getByUserId(@PathVariable String userID) {
        Patient found = service.getByUserId(userID);
        if (found == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(this.convertToDto(found), HttpStatus.OK);
    }

    private PatientDTO convertToDto(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getUser().getId(),
                patient.getUser().getEmail(),
                patient.getUser().getName(),
                patient.getUser().getSurname(),
                patient.getUser().getPhoneNumber(),
                patient.getUser().getAddress(),
                patient.getUser().getCity(),
                patient.getUser().getCountry(),
                patient.getUser().getPersonalID(),
                patient.getClinics(),
                patient.getInterventions(),
                patient.getRequests(),
                patient.getMedicalRecord(),
                patient.getDoctorRatings(),
                patient.getClinicRatings()
        );
    }

    @AllArgsConstructor
    @Getter
    @Setter
    static class PagedResponse {
        private List<PatientDTO> patients;
        private long totalLength;
    }
}
