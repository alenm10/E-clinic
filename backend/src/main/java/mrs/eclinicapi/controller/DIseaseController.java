package mrs.eclinicapi.controller;

import mrs.eclinicapi.dto.DiagnosisDTO;
import mrs.eclinicapi.model.Diagnosis;
import mrs.eclinicapi.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/disease")
public class DIseaseController {

    @Autowired
    DiagnosisService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DiagnosisDTO>> getAllDiseases() {
        List<Diagnosis> it = service.findAll();
        return new ResponseEntity<>(it.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiagnosisDTO> addDiagnosis(@RequestBody DiagnosisDTO diagnosisDTO) {
        Diagnosis d = this.convertToEntity(diagnosisDTO);
        Diagnosis added = service.add(d);
        return new ResponseEntity<>(this.convertToDTO(added), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiagnosisDTO> modifyDisease(@RequestBody DiagnosisDTO diagnosisDTO) {
        Diagnosis d = this.convertToEntity(diagnosisDTO);

        Diagnosis newD = service.findOne(d.getId());
        newD.setName(d.getName());
        Diagnosis modified = service.add(newD);
        if (modified == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(this.convertToDTO(modified), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteDisease(@PathVariable("id") String id) {

        Diagnosis d = service.findOne(id);

        if (d == null) {
            return new ResponseEntity<>("Disease not found", HttpStatus.NOT_FOUND);
        }
        service.deleteById(id);
        return new ResponseEntity<>("deleted InterventionType", HttpStatus.OK);
    }

    public DiagnosisDTO convertToDTO(Diagnosis diagnosis) {
        return new DiagnosisDTO(diagnosis.getId(), diagnosis.getName());
    }

    public Diagnosis convertToEntity(DiagnosisDTO diagnosisDTO) {
        Diagnosis d;
        if(diagnosisDTO.getId() != null) d = service.findOne(diagnosisDTO.getId());
        else d = new Diagnosis();
        if(d == null) d = new Diagnosis();
        d.setName(diagnosisDTO.getName());
        return d;
    }
}
