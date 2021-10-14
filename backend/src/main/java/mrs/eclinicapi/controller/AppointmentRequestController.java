package mrs.eclinicapi.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mrs.eclinicapi.EClinicApiApplication;
import mrs.eclinicapi.dto.AppointmentRequestDTO;
import mrs.eclinicapi.dto.EmailEvent;
import mrs.eclinicapi.model.*;
import mrs.eclinicapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/appointment-requests", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppointmentRequestController {

    @Autowired
    AppointmentRequestService service;

    @Autowired
    ClinicService clinicService;


    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @Autowired
    InterventionTypeService interventionTypeService;

    Map<String, Timer> timers = new HashMap<>();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentRequestDTO>> get() {

        return new ResponseEntity<>(service.findAll()
                .stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentRequestDTO> getRequest(@PathVariable String id) {
        AppointmentRequest appointmentRequest = this.service.findOne(id);
        if (appointmentRequest == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(this.convertToDTO(appointmentRequest), HttpStatus.OK);
    }

    @GetMapping(path = "/clinic/{clinicID}/{pageNumber}/{pageSize}/{sort}/{desc}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse> getAllForClinic(
            @PathVariable String clinicID,
            @PathVariable int pageNumber,
            @PathVariable int pageSize,
            @PathVariable String sort,
            @PathVariable String desc) {
        PagedResponse response;
        if (pageSize < 1) {
            List<AppointmentRequest> requests = service.findByClinicID(clinicID);
            response = new PagedResponse(requests.stream().map(this::convertToDTO).collect(Collectors.toList()),
                    requests.size());

        } else {
            Page<AppointmentRequest> appointmentRequestPage;
            if (sort.equals("undefined"))
                appointmentRequestPage = service.findByClinicIDPaged(clinicID, pageNumber, pageSize);
            else {
                appointmentRequestPage = service.findByClinicIDPaged(clinicID, pageNumber, pageSize, sort, desc.equals("true"));
            }
            response = new PagedResponse(appointmentRequestPage.getContent().stream()
                    .map(this::convertToDTO).collect(Collectors.toList()), appointmentRequestPage.getTotalElements());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/patient/{patientID}/{pageNumber}/{pageSize}/{sort}/{desc}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse> getAllForPatient(
            @PathVariable String patientID,
            @PathVariable int pageNumber,
            @PathVariable int pageSize,
            @PathVariable String sort,
            @PathVariable String desc) {
        PagedResponse response;
        if (pageSize < 1) {
            List<AppointmentRequest> requests = service.findByPatientID(patientID);
            response = new PagedResponse(requests.stream().map(this::convertToDTO).collect(Collectors.toList()),
                    requests.size());

        } else {
            Page<AppointmentRequest> appointmentRequestPage;
            if (sort.equals("undefined"))
                appointmentRequestPage = service.findByPatientIDPaged(patientID, pageNumber, pageSize);
            else {
                appointmentRequestPage = service.findByPatientIDPaged(patientID, pageNumber, pageSize, sort, desc.equals("true"));
            }
            response = new PagedResponse(appointmentRequestPage.getContent().stream()
                    .map(this::convertToDTO).collect(Collectors.toList()), appointmentRequestPage.getTotalElements());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentRequestDTO> save(@RequestBody AppointmentRequestDTO appointmentRequestDTO,
                                                      HttpServletRequest httpServletRequest) {
        AppointmentRequest appointmentRequest = this.convertToEntity(appointmentRequestDTO);
        if (appointmentRequest == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        LocalDate dateOfCreation = LocalDate.now();
        appointmentRequest.setDateOfCreation(dateOfCreation);
        AppointmentRequest saved;
        try {
            saved = service.save(appointmentRequest);
        } catch (OptimisticLockException e) {
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }


        String[] sendTo = appointmentRequest.getClinic().getClinicAdministrator()
                .stream().map(ca -> ca.getUser().getEmail()).toArray(String[]::new);

        String url = EClinicApiApplication.APP_URL;

        if(url.contains("local")) {
            url += ":" + httpServletRequest.getServerPort();
        }
        String content = "There is a new appointment request that should be reviewed. Submitted by "
                + appointmentRequest.getPatient().getUser().getEmail()
                + "\r\n\r\n" + url + "/choose-clinic-room/request=:" +
                saved.getId() + "/clinic=" + saved.getClinic().getId();

        eventPublisher.publishEvent(new EmailEvent(appointmentRequest,
                "Review appointment request", content, sendTo));

        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date scheduled = Date.from(dateOfCreation.plusDays(1).atStartOfDay(defaultZoneId).toInstant());
        timers.put(appointmentRequest.getId(), new Timer());
        timers.get(appointmentRequest.getId()).schedule(new AppointmentTimer(timers, saved), scheduled);
        return new ResponseEntity<>(this.convertToDTO(saved), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<AppointmentRequest> deleteRequest(@PathVariable String id) {
        AppointmentRequest request;
        request = this.service.delete(id);

        if (request == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(request, HttpStatus.OK);
    }


    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentRequest> changeDoctorTime(@PathVariable String id,
                                                               @RequestBody AppointmentRequestDTO requestDTO) {

        if (!id.equals(requestDTO.getId()))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        AppointmentRequest modified = service.modify(id, this.convertToEntity(requestDTO));

        if (modified == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        else return new ResponseEntity<>(modified, HttpStatus.OK);
    }


    private AppointmentRequestDTO convertToDTO(AppointmentRequest appointmentRequest) {
        return new AppointmentRequestDTO(
                appointmentRequest.getId(),
                appointmentRequest.getDateTime(),
                appointmentRequest.getInterventionType().getId(),
                appointmentRequest.getClinic().getId(),
                appointmentRequest.getDoctor().getId(),
                appointmentRequest.getPatient().getId(),
                appointmentRequest.getDateOfCreation()
        );
    }

    private AppointmentRequest convertToEntity(AppointmentRequestDTO appointmentRequestDTO) {
        Clinic foundClinic = clinicService.findOne(appointmentRequestDTO.getClinicID());
        if (foundClinic == null) return null;
        InterventionType interventionType =
                interventionTypeService.findOne(appointmentRequestDTO.getInterventionTypeID());
        if (interventionType == null) return null;
        Doctor doctor = doctorService.findOne(appointmentRequestDTO.getDoctorID());
        if (doctor == null) return null;
        Patient patient = patientService.getPatientById(appointmentRequestDTO.getPatientID());
        if (patient == null) return null;
        return new AppointmentRequest(
                appointmentRequestDTO.getId(),
                appointmentRequestDTO.getDateTime(),
                interventionType,
                foundClinic,
                doctor,
                patient,
                null
        );
    }

    @AllArgsConstructor
    @Getter
    @Setter
    static class PagedResponse {
        private List<AppointmentRequestDTO> requests;
        private long totalLength;
    }
}
