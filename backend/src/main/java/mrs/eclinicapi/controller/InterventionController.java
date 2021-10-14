package mrs.eclinicapi.controller;

import lombok.Getter;
import lombok.Setter;
import mrs.eclinicapi.dto.AppointmentRequestDTO;
import mrs.eclinicapi.dto.EmailEvent;
import mrs.eclinicapi.dto.InterventionDTO;
import mrs.eclinicapi.dto.VisitDTO;
import mrs.eclinicapi.model.*;
import mrs.eclinicapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/intervention")
public class InterventionController {

    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    ClinicRoomService clinicRoomService;
    @Autowired
    ClinicService clinicService;
    @Autowired
    InterventionTypeService interventionTypeService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    VisitService visitService;
    @Autowired
    private InterventionService service;
    @Autowired
    private AppointmentRequestService requestService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private ClinicRatingService clinicRatingService;
    @Autowired
    private DoctorRatingService doctorRatingService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InterventionDTO> getIntervention(@PathVariable String id) {
        Intervention it = service.findOne(id);
        if(it == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(this.convertToDTO(it), HttpStatus.OK);
    }

    @GetMapping(path = "/clinic/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Intervention>> getClinicIntervention(@PathVariable("id") String clinicId) {

        List<Intervention> it = service.getClinicIntervention(clinicId);

        return new ResponseEntity<>(it, HttpStatus.OK);
    }

    @GetMapping(path = "/patient/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InterventionDTO>> getPatientIntervention(@PathVariable("id") String patientId) {

        Patient patient = patientService.getByUserId(patientId);
        if (patient == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<Intervention> it = service.getPatientIntervention(patient.getId());

        return new ResponseEntity<>(it.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }
    @GetMapping(path = "/ongoing/{doctorID}")
    public ResponseEntity<InterventionDTO> ongoingIntervention(@PathVariable String doctorID) {

        Doctor d = doctorService.findByUserID(doctorID);
        if(d == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Intervention ongoing = service.findOngoing(d.getId());
        if(ongoing == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(this.convertToDTO(ongoing), HttpStatus.OK);
    }
    @GetMapping(path = "/upcoming/{doctorID}/{patientUserID}")
        public ResponseEntity<InterventionDTO> upcomingIntervention(@PathVariable String doctorID,
                @PathVariable String patientUserID) {
            Patient p = patientService.getByUserId(patientUserID);
            if(p == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            Doctor d = doctorService.findByUserID(doctorID);
            if(d == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            Intervention upcoming = service.findUpcoming(d.getId(), p.getId());
            if(upcoming == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(this.convertToDTO(upcoming), HttpStatus.OK);
    }

    @GetMapping(path = "/report/{clinicId}/{startDate}/{endDate}")
    public ResponseEntity<Report> financialReport(@PathVariable String clinicId,
    												@PathVariable String startDate,
    												@PathVariable String endDate) {
    	System.out.println("fianncial report clinic = "+ clinicId +" start = " + startDate + " end = " + endDate);

    	Report report = new Report();

    	if(startDate.equals("all") && endDate.equals("all")) {
    		List<Intervention> interventions, pastInterventions, futureInterventions = new ArrayList<>();
    		interventions = service.getClinicIntervention(clinicId);
    		pastInterventions = interventions.stream().filter(in -> in.getVisit() != null).collect(Collectors.toList());;
    		futureInterventions = interventions.stream().filter(in -> in.getVisit() == null).collect(Collectors.toList());
    		double revenue = 0;
    		for(Intervention i : pastInterventions) {
    			revenue += i.getInterventionType().getPrice();
    		}
        	report.totalInterventions = interventions.size();
        	report.totalPastInterventions = pastInterventions.size();
        	report.totalFutureInterventions = futureInterventions.size();
        	report.totalRevenue = revenue;
    	}else if(endDate.equals("none")) {
    		startDate += " 00:00:00";
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = start.plusDays(1);
            System.out.println("start = " + start);
            System.out.println("end none = " + end);

    		List<Intervention> interventions, pastInterventions, futureInterventions = new ArrayList<>();
    		interventions = service.getClinicIntervention(clinicId)
    									.stream()
    									.filter(in -> (in.getDateTime().getEnd().isAfter(start)
    													&& in.getDateTime().getEnd().isBefore(end)))
    									.collect(Collectors.toList());
    		pastInterventions = interventions.stream().filter(in -> in.getVisit() != null).collect(Collectors.toList());
    		futureInterventions = interventions.stream().filter(in -> in.getVisit() == null).collect(Collectors.toList());
    		double revenue = 0;
    		for(Intervention i : pastInterventions) {
    			revenue += i.getInterventionType().getPrice();
    		}
        	report.totalInterventions = interventions.size();
        	report.totalPastInterventions = pastInterventions.size();
        	report.totalFutureInterventions = futureInterventions.size();
        	report.totalRevenue = revenue;
    	}else {
    		startDate += " 00:00:00";
        	endDate += " 00:00:00";
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = LocalDateTime.parse(endDate, formatter);
            System.out.println("start = " + start);
            System.out.println("end = " + end);

    		List<Intervention> interventions, pastInterventions, futureInterventions = new ArrayList<>();
    		interventions = service.getClinicIntervention(clinicId)
    									.stream()
    									.filter(in -> (in.getDateTime().getEnd().isAfter(start)
    													&& in.getDateTime().getEnd().isBefore(end)))
    									.collect(Collectors.toList());
    		pastInterventions = interventions.stream().filter(in -> in.getVisit() != null).collect(Collectors.toList());
    		futureInterventions = interventions.stream().filter(in -> in.getVisit() == null).collect(Collectors.toList());
    		double revenue = 0;
    		for(Intervention i : pastInterventions) {
    			revenue += i.getInterventionType().getPrice();
    		}
        	report.totalInterventions = interventions.size();
        	report.totalPastInterventions = pastInterventions.size();
        	report.totalFutureInterventions = futureInterventions.size();
        	report.totalRevenue = revenue;
    	}
    	System.out.println("report = " + report);

    	return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping(path = "/{doctorID}/{patientUserID")
    public ResponseEntity<Boolean> pastIntervention(@PathVariable String doctorID,
                                                    @PathVariable String patientUserID) {
        Patient p = patientService.getByUserId(patientUserID);
        if(p == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Doctor d = doctorService.findByUserID(doctorID);
        if(d == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        boolean exists = service.pastIntervention(d.getId(), p.getId());
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @PostMapping(path = "/finish/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InterventionDTO> finish(@PathVariable String id, @RequestBody VisitDTO visitDTO) {
        Intervention intervention = service.findOne(id);
        if(intervention == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Visit newVisit = new Visit();
        newVisit.setAnamnesis(visitDTO.getAnamnesis());
        newVisit.setDiagnoses(visitDTO.getDiagnoses());
        newVisit.setUsageMethod(visitDTO.getUsageMethod());
        newVisit.setMedicines(visitDTO.getMedicines());
        newVisit.setIntervention(intervention);
        newVisit.setCertified(false);
        Visit savedVisit = visitService.save(newVisit);

        Patient p = intervention.getPatient();
        p.getMedicalRecord().addVisit(savedVisit);
        p.getMedicalRecord().addDiagnoses(savedVisit.getDiagnoses());
        p.getMedicalRecord().addMedicines(savedVisit.getMedicines());
        intervention.setVisit(savedVisit);
        Intervention saved = service.add(intervention);
        return new ResponseEntity<>(this.convertToDTO(saved), HttpStatus.OK);
    }

    @PostMapping(path = "/one-click/{oneClickID}/{userID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InterventionDTO> scheduleOneClick(@PathVariable String oneClickID,
                                                            @PathVariable String userID) {
//        OneClickAppointment appointment = service.findByID(oneClickID);
//
//        if (appointment == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        oneClickAppointmentService.delete(appointment.getId());
//
//        Patient pat = patientService.getByUserId(userID);
//        if (pat == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        Intervention toAdd = new Intervention(appointment, pat);
//        Intervention added = service.add(toAdd);
        Intervention added;
        try {
            added = service.scheduleOneClick(oneClickID, userID);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if(added == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String content = "An intervention for the date " + added.getDateTime().getStart() + " has been added." +
                "\r\nYou have an option to refuse coming, via this link: " +
                "\r\nnekilinkcebit.";
        String[] sendToPatient = {added.getPatient().getUser().getEmail()};
        eventPublisher.publishEvent(new EmailEvent(added.getPatient().getUser(), "Appointment scheduled", content, sendToPatient));

        content = "An intervention for the date " + added.getDateTime().getStart() + " has been added.";

        String[] sendToDoctor = {added.getPatient().getUser().getEmail()};
        eventPublisher.publishEvent(new EmailEvent(added.getDoctor().getUser(), "Appointment scheduled", content, sendToDoctor));
        return new ResponseEntity<>(this.convertToDTO(added), HttpStatus.OK);
    }

    @GetMapping(path = "/rating/{patientUserId}/{clinicId}/{doctorId}")
    public ResponseEntity<Rating> getRating(@PathVariable String patientUserId,
                                            @PathVariable String clinicId,
                                            @PathVariable String doctorId) {
        Clinic clinic = clinicService.findOne(clinicId);
        if (clinic == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Patient p = patientService.getByUserId(patientUserId);
        if (p == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Doctor doctor = doctorService.findOne(doctorId);
        if (doctor == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        DoctorRating dr = doctorRatingService.get(doctor.getId(), p.getId());
        ClinicRating cr = clinicRatingService.get(clinic.getId(), p.getId());
        int crating = cr == null ? 0 : cr.getRating();
        int drating = dr == null ? 0 : dr.getRating();
        Rating r = new Rating();
        r.setClinicRating(crating);
        r.setDoctorRating(drating);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PostMapping(path = "/rate/{patientUserId}/{clinicId}/{clinicRating}/{doctorId}/{doctorRating}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> rate(@PathVariable String patientUserId,
                                       @PathVariable String clinicId,
                                       @PathVariable int clinicRating,
                                       @PathVariable String doctorId,
                                       @PathVariable int doctorRating) {
    	Clinic clinic = clinicService.findOne(clinicId);
    	if (clinic == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	Patient p = patientService.getByUserId(patientUserId);
        if (p == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Doctor doctor = doctorService.findOne(doctorId);
        if (doctor == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(clinicRating > 0) {
    	    ClinicRating cr = clinicRatingService.get(clinic.getId(), p.getId());
    	    if (cr == null) {
                cr = new ClinicRating();
                cr.setClinic(clinic);
                cr.setPatient(p);
            }
    	    cr.setRating(clinicRating);
    	    clinicRatingService.save(cr);
        }
    	if(doctorRating > 0) {
    	    DoctorRating dr = doctorRatingService.get(doctor.getId(), p.getId());
    	    if (dr == null) {
    	        dr = new DoctorRating();
    	        dr.setDoctor(doctor);
    	        dr.setPatient(p);
            }
    	    dr.setRating(doctorRating);
    	    doctorRatingService.save(dr);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/approve/{requestID}/{roomID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InterventionDTO> approve(@PathVariable String requestID,
                                                   @PathVariable String roomID) {
        AppointmentRequest request = requestService.findOne(requestID);
        if (request == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ClinicRoom room = clinicRoomService.findOne(roomID);
        if (room == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        try {
            requestService.delete(request);
        } catch (AppointmentRequestDTO.ConcurrentRequest concurrentRequest) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Intervention toAdd = new Intervention(request, room);
        Clinic c = toAdd.getClinic();
        c.addPatient(toAdd.getPatient());
        clinicService.addClinic(c);
        Intervention added = service.add(toAdd);
        String content = "An intervention for the date " + added.getDateTime().getStart() + " has been added." +
                "\r\nYou have an option to refuse coming, via this link: " +
                "\r\nnekilinkcebit.";
        String[] sendToPatient = {added.getPatient().getUser().getEmail()};
        eventPublisher.publishEvent(new EmailEvent(added.getPatient().getUser(), "Appointment scheduled", content, sendToPatient));

        content = "An intervention for the date " + added.getDateTime().getStart() + " has been added.";

        String[] sendToDoctor = {added.getDoctor().getUser().getEmail()};
        eventPublisher.publishEvent(new EmailEvent(added.getDoctor().getUser(), "Appointment scheduled", content, sendToDoctor));


        return new ResponseEntity<>(this.convertToDTO(added), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteIntervention(@PathVariable("id") String id) {

        Intervention it = service.findOne(id);

        if (it == null) {
            return new ResponseEntity<>("Interventionnot found", HttpStatus.NOT_FOUND);
        }
        service.deleteById(id);
        return new ResponseEntity<>("deleted Intervention", HttpStatus.OK);
    }


    private InterventionDTO convertToDTO(Intervention intervention) {
        return new InterventionDTO(
                intervention.getId(),
                intervention.getDateTime(),
                intervention.getClinicRoom().getId(),
                intervention.getClinicRoom().getName(),
                intervention.getDoctor().getId(),
                intervention.getDoctor().getUser().getName(),
                intervention.getInterventionType().getId(),
                intervention.getInterventionType().getName(),
                intervention.getInterventionType().getPrice(),
                intervention.getPatient().getId(),
                intervention.getClinic().getId(),
                intervention.getClinic().getName(),
                intervention.getPrice()
        );
    }

    private Intervention convertToEntity(InterventionDTO interventionDTO) {
        Clinic foundClinic = clinicService.findOne(interventionDTO.getClinicID());
        if (foundClinic == null) return null;
        InterventionType interventionType =
                interventionTypeService.findOne(interventionDTO.getInterventionTypeID());
        if (interventionType == null) return null;
        Doctor doctor = doctorService.findOne(interventionDTO.getDoctorID());
        if (doctor == null) return null;
        Patient patient = patientService.getPatientById(interventionDTO.getPatientID());
        if (patient == null) return null;
        ClinicRoom room = clinicRoomService.findOne(interventionDTO.getClinicRoomID());
        if (room == null) return null;
        return new Intervention(
                interventionDTO.getId(),
                interventionDTO.getDateTime(),
                room,
                patient,
                doctor,
                foundClinic,
                interventionType
        );
    }
    @Getter
    @Setter
    static class Rating {
        private int doctorRating;
        private int clinicRating;
    }

    @Getter
    @Setter
    static class Report{
    	private int totalInterventions;
    	private int totalPastInterventions;
    	private int totalFutureInterventions;
    	private double totalRevenue;
		@Override
		public String toString() {
			return "Report [totalInterventions=" + totalInterventions + ", totalPastInterventions="
					+ totalPastInterventions + ", totalFutureInterventions=" + totalFutureInterventions
					+ ", totalRevenue=" + totalRevenue + "]";
		}

    }

}
