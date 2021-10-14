package mrs.eclinicapi.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mrs.eclinicapi.dto.ClinicDTO;
import mrs.eclinicapi.dto.ClinicSearchRequest;
import mrs.eclinicapi.model.Clinic;
import mrs.eclinicapi.model.ClinicRating;
import mrs.eclinicapi.model.InterventionType;
import mrs.eclinicapi.service.ClinicService;
import mrs.eclinicapi.service.DoctorService;
import mrs.eclinicapi.service.InterventionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/clinic")
@Slf4j
public class ClinicController {

    @Autowired
    private ClinicService service;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private InterventionTypeService interventionTypeService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClinicDTO> addClinic(@RequestBody ClinicDTO clinic) {
        Clinic newClinic = service.addClinic(this.convertToEntity(clinic));
        if (newClinic == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        return new ResponseEntity<>(this.convertToDTO(newClinic), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClinicDTO> modifyClinic(@RequestBody ClinicDTO clinic) {
        Clinic modified = service.modifyClinic(this.convertToEntity(clinic));
        if (modified == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.convertToDTO(modified), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClinicDTO>> getAllClinic() {

        List<Clinic> clinics = service.findAll();

        if (clinics == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(clinics.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(path = "/{pageNumber}/{pageSize}/{sort}/{desc}")
    public ResponseEntity<PagedResponse> getPagedClinics(@PathVariable int pageNumber,
                                                         @PathVariable int pageSize,
                                                         @PathVariable String sort,
                                                         @PathVariable String desc) {
        PagedResponse response;
        if (pageSize < 1) {
            List<Clinic> allClinics = service.findAll();
            response = new PagedResponse(allClinics.stream().map(this::convertToDTO).collect(Collectors.toList()),
                    allClinics.size());

        } else {
            Page<Clinic> clinicPage;
            if (sort.equals("undefined"))
                clinicPage = service.findPaged(pageNumber, pageSize);
            else {
                clinicPage = service.findPaged(pageNumber, pageSize, sort, desc.equals("true"));
            }
            response = new PagedResponse(clinicPage.getContent().stream().map(this::convertToDTO).collect(Collectors.toList()),
                    clinicPage.getTotalElements());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping(path = "/user/{id}")
    public ResponseEntity<ClinicDTO> getClinicByUserID(@PathVariable("id") String userID) {

        Clinic clinic = service.findByUser(userID);

        if (clinic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.convertToDTO(clinic), HttpStatus.OK);
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<ClinicDTO> getOneClinic(@PathVariable("id") String id) {


        Clinic clinic = service.findOne(id);

        if (clinic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(this.convertToDTO(clinic), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteClinic(@PathVariable("id") String id) {
        Clinic clinic = service.findOne(id);
        if (clinic == null) {
            return new ResponseEntity<>("clinic not found", HttpStatus.NOT_FOUND);
        }
        service.deleteById(id);
        return new ResponseEntity<>("deleted clinic", HttpStatus.OK);
    }

    @PostMapping(path = "/search/{pageNumber}/{pageSize}/{sort}/{desc}")
    public ResponseEntity<PagedResponse> searchClinics(@RequestBody ClinicSearchRequest searchRequest,
                                                       @PathVariable int pageNumber,
                                                       @PathVariable int pageSize,
                                                       @PathVariable String sort,
                                                       @PathVariable String desc) {
        LocalDate date = searchRequest.getDate();
        InterventionType type = interventionTypeService.findOne(searchRequest.getInterventionType());
        String searchQuery = searchRequest.getSearchQuery();
        if (searchQuery == null) searchQuery = "";
        if (type == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        PagedResponse response;
        Page<Clinic> clinicPage;
        if (sort.equals("undefined"))
            clinicPage = service.search(searchQuery, date, type, pageNumber, pageSize);
        else {
            clinicPage = service.search(searchQuery, date, type, pageNumber, pageSize, sort, desc.equals("true"));
        }
        response = new PagedResponse(clinicPage.getContent().stream().map(this::convertToDTO).collect(Collectors.toList()),
                clinicPage.getTotalElements());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    static class PagedResponse {
        private List<ClinicDTO> clinics;
        private long totalLength;
    }


    public ClinicDTO convertToDTO(Clinic c) {
        return new ClinicDTO(
                c.getId(), c.getName(), c.getDescription(), c.getAddress(), c.getCoordinates(),
                c.getClinicAdministrator(), c.getClinicRoom(), c.getDoctors(), c.getNurses(), c.getInterventions(),
                c.getVacationRequests(), c.getInterventionTypes(), c.getAppointmentRequests(), c.getOneClicks(),
                c.getPatients(), c.getAdmin(), getAvg(c.getRating())
        );
    }

    public Clinic convertToEntity(ClinicDTO clinicDTO) {
        Clinic found = service.findOne(clinicDTO.getId());
        if (found == null) found = new Clinic();
        found.setName(clinicDTO.getName());
        found.setDescription(clinicDTO.getDescription());
        found.setAddress(clinicDTO.getAddress());
        found.setCoordinates(clinicDTO.getCoordinates());
        return found;
    }

    private double getAvg(Set<ClinicRating> ratings) {
        if(ratings == null || ratings.isEmpty()) {
            return 0;
        }
        double sum = 0;
        for (ClinicRating cr : ratings) {
            sum += cr.getRating();
        }
        return Math.round(sum/ratings.size() * 10.0) / 10.0;
    }

}
