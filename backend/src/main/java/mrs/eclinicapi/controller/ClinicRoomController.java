package mrs.eclinicapi.controller;

import lombok.AllArgsConstructor;
import mrs.eclinicapi.dto.ClinicRoomDTO;
import mrs.eclinicapi.dto.ClinicRoomSearchRequest;
import mrs.eclinicapi.model.Clinic;
import mrs.eclinicapi.model.ClinicRoom;
import mrs.eclinicapi.service.ClinicRoomService;
import mrs.eclinicapi.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/clinicroom")
public class ClinicRoomController {

    @Autowired
    private ClinicRoomService service;

    @Autowired
    private ClinicService clinicService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClinicRoomDTO> addClinicRoom(@RequestBody ClinicRoomDTO clinicRoomDto) {
        ClinicRoom clinicRoom = this.convertToEntity(clinicRoomDto);
        ClinicRoom added = service.addClinicRoom(clinicRoom);
        return new ResponseEntity<>(this.convertToDTO(added), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClinicRoomDTO>> getAllRooms() {
        List<ClinicRoom> clinicRooms = service.findAll();
        List<ClinicRoomDTO> clinicRoomDTOS = clinicRooms.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(clinicRoomDTOS, HttpStatus.OK);
    }

    @GetMapping(path = "/{clinicID}/{pageNumber}/{pageSize}/{sort}/{desc}")
    public ResponseEntity<PagedResponse> getPagedClinics(@PathVariable String clinicID,
                                                         @PathVariable int pageNumber,
                                                         @PathVariable int pageSize,
                                                         @PathVariable String sort,
                                                         @PathVariable String desc) {

        PagedResponse response;
        if (pageSize < 1) {
            List<ClinicRoom> allClinicRooms = service.findByClinic(clinicID);
            response = new PagedResponse(allClinicRooms.stream().map(this::convertToDTO).collect(Collectors.toList()),
                    allClinicRooms.size());

        } else {
            Page<ClinicRoom> clinicRoomsPage;
            if (sort.equals("undefined"))
                clinicRoomsPage = service.findPagedByClinic(clinicID, pageNumber, pageSize);
            else {
                clinicRoomsPage = service.findPagedByClinic(clinicID, pageNumber, pageSize, sort, desc.equals("true"));
            }
            response = new PagedResponse(clinicRoomsPage.getContent()
                    .stream().map(this::convertToDTO).collect(Collectors.toList()),
                    clinicRoomsPage.getTotalElements());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping(path = "/search/{clinicID}/{pageNumber}/{pageSize}/{sort}/{desc}")
    public ResponseEntity<PagedResponse> searchClinicRooms(@RequestBody ClinicRoomSearchRequest searchRequest,
                                                           @PathVariable String clinicID,
                                                           @PathVariable int pageNumber,
                                                           @PathVariable int pageSize,
                                                           @PathVariable String sort,
                                                           @PathVariable String desc) {
        LocalDateTime dateTime = searchRequest.getDateTime();
        String roomName = searchRequest.getRoomName();
        if (roomName == null) roomName = "";
        String roomID = searchRequest.getRoomID();
        if (roomID == null) roomID = "";
        int duration = searchRequest.getDuration();
        PagedResponse response;
        Page<ClinicRoom> clinicPage;
        if (sort.equals("undefined"))
            clinicPage = service.search(clinicID, roomName, roomID, dateTime, duration, pageNumber, pageSize);
        else {
            clinicPage = service.search(clinicID, roomName, roomID, dateTime, duration, pageNumber, pageSize, sort, desc.equals("true"));
        }
        response = new PagedResponse(clinicPage.getContent()
                .stream().map(this::convertToDTO).collect(Collectors.toList()), clinicPage.getTotalElements());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClinicRoomDTO> updateClinicRoom(@RequestBody ClinicRoomDTO clinicRoomDTO) {
        ClinicRoom clinicRoom = this.convertToEntity(clinicRoomDTO);
        ClinicRoom modified = service.modifyRoom(clinicRoom);
        if (modified == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.convertToDTO(modified), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteClinicRoom(@PathVariable("id") String id) {

        ClinicRoom room = service.findOne(id);
        if (room == null) {
            return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        }
        if(room.getInterventions().size() != 0) {
        	System.out.println("room interventions size != 0");
            return new ResponseEntity<>("this room has scheduled appointments", HttpStatus.NOT_FOUND);            	
        }
        service.deleteById(id);
        return new ResponseEntity<>("room deleted", HttpStatus.OK);
    }

    public ClinicRoom convertToEntity(ClinicRoomDTO clinicRoomDTO) {
        Clinic foundClinic = clinicService.findOne(clinicRoomDTO.getClinicId());
        if (foundClinic == null) return null;
        ClinicRoom clinicRoom = new ClinicRoom();
        clinicRoom.setId(clinicRoomDTO.getId());
        clinicRoom.setName(clinicRoomDTO.getName());
        clinicRoom.setClinic(foundClinic);
        clinicRoom.setInterventions(clinicRoomDTO.getInterventions());
        return clinicRoom;
    }

    public ClinicRoomDTO convertToDTO(ClinicRoom clinicRoom) {
        ClinicRoomDTO clinicRoomDTO = new ClinicRoomDTO();
        clinicRoomDTO.setId(clinicRoom.getId());
        clinicRoomDTO.setName(clinicRoom.getName());
        clinicRoomDTO.setClinicId(clinicRoom.getClinic().getId());

        clinicRoomDTO.setInterventions(clinicRoom.getInterventions());
        return clinicRoomDTO;
    }

    @AllArgsConstructor
    static class PagedResponse {
        public List<ClinicRoomDTO> clinicRooms;
        public long totalLength;
    }
}
