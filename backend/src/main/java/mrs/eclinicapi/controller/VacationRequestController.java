package mrs.eclinicapi.controller;

import mrs.eclinicapi.dto.OnDisapproveVacationCompleteEvent;
import mrs.eclinicapi.model.Doctor;
import mrs.eclinicapi.model.Nurse;
import mrs.eclinicapi.model.TimePeriod;
import mrs.eclinicapi.model.User;
import mrs.eclinicapi.model.VacationRequest;
import mrs.eclinicapi.service.DoctorService;
import mrs.eclinicapi.service.NurseService;
import mrs.eclinicapi.service.UserService;
import mrs.eclinicapi.service.VacationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping(value = "api/vacationRequest")
public class VacationRequestController {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private VacationRequestService service;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private NurseService nurseService;
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VacationRequest>> getAllVacationRequest() {
        List<VacationRequest> vacationRequest = service.findAll();
        if (vacationRequest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vacationRequest, HttpStatus.OK);
    }

    @GetMapping(path = "/user/{id}")
    public ResponseEntity<List<VacationRequest>> getVacationRequestForUser(@PathVariable("id") String id) {


        List<VacationRequest> vacationRequest = service.getVacationRequestForUser(id);
        if (vacationRequest == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vacationRequest, HttpStatus.OK);
    }

    @GetMapping(path = "/approve/{id}")
    public ResponseEntity<VacationRequest> approveVacationRequest(@PathVariable("id") String id) {

        VacationRequest vac = service.findOne(id);
        if (vac == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        vac.setStatus("approved");
        vac.setReason("approved");
        
        TimePeriod<LocalDate> vacations = new TimePeriod<LocalDate>();
        vacations.setStart(vac.getStartDate().toLocalDate());
        vacations.setEnd(vac.getEndDate().toLocalDate());
        
        Doctor d = doctorService.findByUserID(vac.getUser().getId());
        Nurse n = nurseService.findByUserID(vac.getUser().getId());
        
        if(d != null) {
        	System.out.println("doctor != null");
        	d.getWorkingCalendar().addVacation(vacations);
            doctorService.addDoctor(d);            
        }else if(n != null){
        	System.out.println("nurse != null");
        	n.getWorkingCalendar().addVacation(vacations);
            nurseService.addNurse(n);                    	
        }else {
        	System.out.println("doctor and nurse == null");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
                
        VacationRequest modified = service.addVacationRequest(vac);

        return new ResponseEntity<>(modified, HttpStatus.OK);
    }

    @PostMapping(path = "/disapprove/{id}")
    public ResponseEntity<VacationRequest> disapproveVacationRequest(@PathVariable("id") String id,
                                                                     @RequestBody String reason) {
        reason = reason.substring(0, reason.length() - 1);    //brise = koje se na string dodaje iz nekog razloga
        reason = reason.replace('+', ' ');


        VacationRequest vac = service.findOne(id);
        if (vac == null) {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        vac.setStatus("disapproved");
        vac.setReason(reason);


        VacationRequest modified = service.addVacationRequest(vac);

        eventPublisher.publishEvent(new OnDisapproveVacationCompleteEvent(vac.getUser(), reason));

        return new ResponseEntity<>(modified, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<List<VacationRequest>> getVacationRequestForClinic(@PathVariable("id") String id) {


        List<VacationRequest> vacationRequest = service.getVacationRequestForClinic(id);
        if (vacationRequest == null) {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }


        return new ResponseEntity<>(vacationRequest, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VacationRequest> addVacationRequest(@RequestBody VacationRequest vacationRequest) {

        VacationRequest newVacation = service.addVacationRequest(vacationRequest);
        if (newVacation == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        return new ResponseEntity<>(newVacation, HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VacationRequest> modifyVacationRequest(@RequestBody VacationRequest vacationRequest) {
        VacationRequest modified = service.modifyVacationRequest(vacationRequest);
        if (modified == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(modified, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteVacationRequest(@PathVariable("id") String id) {
        VacationRequest vacationRequest = service.findOne(id);
        if (vacationRequest == null) {
            return new ResponseEntity<>("vacationRequest not found", HttpStatus.NOT_FOUND);
        }
        service.deleteById(id);
        return new ResponseEntity<>("deleted vacationRequest", HttpStatus.OK);
    }

}
