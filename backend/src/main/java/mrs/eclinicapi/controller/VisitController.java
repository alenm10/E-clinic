package mrs.eclinicapi.controller;

import mrs.eclinicapi.dto.*;
import mrs.eclinicapi.model.*;
import mrs.eclinicapi.service.DiagnosisService;
import mrs.eclinicapi.service.NurseService;
import mrs.eclinicapi.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/visit", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisitController {

    @Autowired
    VisitService service;
    @Autowired
    NurseService nurseService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VisitDTO>> getAllVisits() {
        List<Visit> it = service.findAll();
        return new ResponseEntity<>(it.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(path = "/clinic/{clinicId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VisitDTO>> getAllVisitsByClinic(@PathVariable("clinicId") String clinicId) {
        List<Visit> it = service.findAllByClinic(clinicId);
        return new ResponseEntity<>(it.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping(path ="/certified/{visitId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VisitDTO> updateMedicine(@RequestBody UserDTO userDTO, @PathVariable("visitId") String visitId) {
        System.out.println(visitId);
        Optional<Visit> visit = service.findById(visitId);
        if(visit.isPresent()){
            Nurse nurse = nurseService.findByUserID(userDTO.getId());
            System.out.println(nurse.getId());
            visit.get().setCertified(true);
            visit.get().setNurse(nurse);
            Visit modify = service.modify(visit.get());
            if(modify== null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(this.convertToDTO(modify), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private VisitDTO convertToDTO(Visit visit) {
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setCertified(visit.isCertified());
        visitDTO.setAnamnesis(visit.getAnamnesis());
        visitDTO.setId(visit.getId());
        visitDTO.setDoctorName(visit.getIntervention().getDoctor().getUser().getName());
        visitDTO.setMedicines(visit.getMedicines());
        visitDTO.setDiagnoses(visit.getDiagnoses());
        visitDTO.setPatientName(visit.getIntervention().getPatient().getUser().getName());
        if(visit.getNurse()!= null)
            visitDTO.setNurseId(visit.getNurse().getId());

        return visitDTO;
    }
}
