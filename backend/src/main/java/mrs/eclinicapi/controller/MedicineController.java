package mrs.eclinicapi.controller;

import mrs.eclinicapi.dto.DiagnosisDTO;
import mrs.eclinicapi.dto.MedicineDTO;
import mrs.eclinicapi.model.Diagnosis;
import mrs.eclinicapi.model.Medicine;
import mrs.eclinicapi.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/medicine")
public class MedicineController {

    @Autowired
    private MedicineService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicineDTO>> getAllMedicine() {
        List<Medicine> medicines = service.findAll();
        return new ResponseEntity<>(medicines.stream().map(this::convertToDTO).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicineDTO> addMedicine(@RequestBody MedicineDTO medicine) {
        Medicine added = service.add(this.convertToEntity(medicine));
        return new ResponseEntity<>(this.convertToDTO(added), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicineDTO> updateMedicine(@RequestBody MedicineDTO medicine) {
        Medicine modified = service.modify(this.convertToEntity(medicine));
        if (modified == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.convertToDTO(modified), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteMedicine(@PathVariable("id") String id) {

        Medicine medicine = service.findOne(id);
        if (medicine == null) {
            return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        }

        service.deleteById(id);
        return new ResponseEntity<>("room deleted", HttpStatus.OK);
    }
    public MedicineDTO convertToDTO(Medicine medicine) {
        return new MedicineDTO(medicine.getId(), medicine.getName(), medicine.getManufacturer());
    }

    public Medicine convertToEntity(MedicineDTO medicineDTO) {
        Medicine m;
        if(medicineDTO.getId() != null) m = service.findOne(medicineDTO.getId());
        else m = new Medicine();
        if(m == null) m = new Medicine();
        m.setName(medicineDTO.getName());
        m.setManufacturer(medicineDTO.getManufacturer());
        return m;
    }
}
