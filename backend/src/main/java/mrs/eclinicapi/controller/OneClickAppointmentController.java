package mrs.eclinicapi.controller;

import lombok.AllArgsConstructor;
import mrs.eclinicapi.dto.OneClickAppointmentDTO;
import mrs.eclinicapi.model.*;
import mrs.eclinicapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/oneClick")
public class OneClickAppointmentController {

    @Autowired
    ClinicRoomService clinicRoomService;
    @Autowired
    ClinicService clinicService;
    @Autowired
    InterventionTypeService interventionTypeService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    InterventionService service;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OneClickAppointmentDTO> save(@RequestBody OneClickAppointmentDTO oneClickAppointmentDTO) {
        Intervention toSave = this.convertToEntity(oneClickAppointmentDTO);
        if(toSave == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Intervention saved = service.add(toSave);
        return new ResponseEntity<>(this.convertToDTO(saved), HttpStatus.OK);
    }

    @GetMapping(path = "/clinic/{clinicID}/{pageNumber}/{pageSize}/{sort}/{desc}")
    public ResponseEntity<PagedResponse> getAllForClinic(@PathVariable String clinicID,
                                                         @PathVariable int pageNumber,
                                                         @PathVariable int pageSize,
                                                         @PathVariable String sort,
                                                         @PathVariable String desc) {

        PagedResponse response;
        if (pageSize < 1) {
            List<Intervention> oneClickAppointments = service.findByClinicID(clinicID);
            response = new PagedResponse(oneClickAppointments.stream().map(this::convertToDTO).collect(Collectors.toList()),
                    oneClickAppointments.size());

        } else {
            Page<Intervention> oneClickAppointmentPage;
            if (sort.equals("undefined"))
                oneClickAppointmentPage = service.findByClinicIDPaged(clinicID, pageNumber, pageSize);
            else {
                oneClickAppointmentPage = service.findByClinicIDPaged(clinicID, pageNumber, pageSize, sort, desc.equals("true"));
            }
            response = new PagedResponse(oneClickAppointmentPage.getContent().stream()
                    .map(this::convertToDTO).collect(Collectors.toList()), oneClickAppointmentPage.getTotalElements());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<OneClickAppointmentDTO> delete(@PathVariable String id) {
        Intervention appointment = service.deleteById(id);
        if (appointment == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(this.convertToDTO(appointment), HttpStatus.OK);
    }


    @PutMapping(path = "{id}")
    public ResponseEntity<OneClickAppointmentDTO> update(@RequestBody OneClickAppointmentDTO oneClickAppointmentDTO,
                                                         @PathVariable String id) {
        Intervention oneClickAppointment = service.modifyOneClick(id, this.convertToEntity(oneClickAppointmentDTO));
        if (oneClickAppointment == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(this.convertToDTO(oneClickAppointment), HttpStatus.OK);
    }


    private OneClickAppointmentDTO convertToDTO(Intervention oneClickAppointment) {
        return new OneClickAppointmentDTO(
                oneClickAppointment.getId(),
                oneClickAppointment.getDateTime(),
                oneClickAppointment.getInterventionType().getId(),
                oneClickAppointment.getInterventionType().getName(),
                oneClickAppointment.getInterventionType().getPrice(),
                oneClickAppointment.getClinic().getId(),
                oneClickAppointment.getDoctor().getId(),
                oneClickAppointment.getDoctor().getUser().getName(),
                oneClickAppointment.getDoctor().getUser().getSurname(),
                oneClickAppointment.getClinicRoom().getId(),
                oneClickAppointment.getClinicRoom().getName()
        );
    }

    private Intervention convertToEntity(OneClickAppointmentDTO oneClickAppointmentDTO) {
        Clinic foundClinic = clinicService.findOne(oneClickAppointmentDTO.getClinicID());
        if (foundClinic == null) return null;
        InterventionType interventionType =
                interventionTypeService.findOne(oneClickAppointmentDTO.getTypeID());
        if (interventionType == null) return null;
        Doctor doctor = doctorService.findOne(oneClickAppointmentDTO.getDoctorID());
        if (doctor == null) return null;
        ClinicRoom room = clinicRoomService.findOne(oneClickAppointmentDTO.getClinicRoomID());
        if (room == null) return null;
        return new Intervention(
                oneClickAppointmentDTO.getId(),
                oneClickAppointmentDTO.getDateTime(),
                room,
                null,
                doctor,
                foundClinic,
                interventionType
        );
    }

    @AllArgsConstructor
    static class PagedResponse {
        public List<OneClickAppointmentDTO> oneClicks;
        public long totalLength;
    }
}
