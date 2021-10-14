package mrs.eclinicapi.service;

import mrs.eclinicapi.model.ClinicRoom;
import mrs.eclinicapi.repository.ClinicAdministratorRepository;
import mrs.eclinicapi.repository.ClinicRoomRepository;
import mrs.eclinicapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ClinicRoomService {

    @Autowired
    private ClinicRoomRepository repository;
    @Autowired
    private ClinicAdministratorRepository clinicAdministratorRepository;
    @Autowired
    private UserRepository userRepository;

    private ClinicService clinicService;

    public ClinicRoom addClinicRoom(ClinicRoom newClinicRoom) {
        return repository.save(newClinicRoom);
    }

    public ClinicRoom findOne(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<ClinicRoom> findAll() {
        return repository.findAll();
    }

    public List<ClinicRoom> findByClinic(String clinicID) {
        return repository.findAllByClinic_Id(clinicID);
    }

    public Page<ClinicRoom> findPagedByClinic(String clinicID, int pageNumber, int pageSize) {
        return this.findPagedByClinic(clinicID, pageNumber, pageSize, null, false);
    }

    public Page<ClinicRoom> findPagedByClinic(String clinicID, int pageNumber, int pageSize, String sort, boolean desc) {
        Pageable p;
        if (sort != null) {
            Sort s;
            if (desc) s = Sort.by(Sort.Direction.DESC, sort);
            else s = Sort.by(Sort.Direction.ASC, sort);
            p = PageRequest.of(--pageNumber, pageSize, s);
        } else p = PageRequest.of(--pageNumber, pageSize);
        return repository.findAllByClinic_Id(clinicID, p);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public ClinicRoom modifyRoom(ClinicRoom clinicRoom) {
        if (this.findOne(clinicRoom.getId()) == null) return null;
        return repository.save(clinicRoom);
    }

    public Page<ClinicRoom> search(String clinicID, String clinicRoomName, String clinicRoomID, LocalDateTime dateTime, int duration, int pageNumber, int pageSize) {
        return this.search(clinicID, clinicRoomName, clinicRoomID, dateTime, duration, pageNumber, pageSize, null, false);
    }

    public Page<ClinicRoom> search(String clinicID,
                                   String clinicRoomName,
                                   String clinicRoomID,
                                   LocalDateTime dateTime,
                                   int duration,
                                   int pageNumber,
                                   int pageSize,
                                   String sort,
                                   boolean desc) {
        Pageable p;
        int fakePageSize = pageSize;
        if (fakePageSize < 1) fakePageSize = 1000;
        if (sort != null) {
            Sort s;
            if (desc) s = Sort.by(Sort.Direction.DESC, sort);
            else s = Sort.by(Sort.Direction.ASC, sort);
            p = PageRequest.of(--pageNumber, fakePageSize, s);
        } else p = PageRequest.of(--pageNumber, fakePageSize);


        return this.someOtherFunction(clinicID, clinicRoomName, clinicRoomID, dateTime, duration, p, pageSize);
    }

    private Page<ClinicRoom> someOtherFunction(String clinicID,
                                               String clinicRoomName,
                                               String clinicRoomID,
                                               LocalDateTime dateTime,
                                               int duration,
                                               Pageable p,
                                               int pageSize) {
        List<ClinicRoom> clinicRooms = findByClinic(clinicID);
        Stream<ClinicRoom> filtered = this.filterClinicRooms(clinicRooms, clinicRoomName, clinicRoomID, dateTime, duration);
        if (p.getSort().isSorted()) {
            Sort.Order o = p.getSort().iterator().next();
            String property = o.getProperty();
            boolean desc = o.getDirection().isDescending();
            filtered = filtered
                    .sorted((c1, c2) -> this.sortFunction(c1, c2, property, desc));
        }
        List<ClinicRoom> fullList = filtered.collect(Collectors.toList());
        if (pageSize < 1) return new PageImpl<>(fullList, p, fullList.size());
        else {
            int start = (int) p.getOffset();
            int end = Math.min((start + p.getPageSize()), fullList.size());
            return new PageImpl<>(fullList.subList(start, end), p, fullList.size());
        }
    }

    private Stream<ClinicRoom> filterClinicRooms(List<ClinicRoom> clinicRooms, String roomName, String roomID, LocalDateTime dateTime, int duration) {
        LocalDateTime interventionEnd = dateTime.plusMinutes(duration);
        return clinicRooms.stream()
                .filter(room -> room.getName().toLowerCase().contains(roomName.toLowerCase()) &&
                        room.getId().toLowerCase().contains(roomID.toLowerCase()))
                .filter(room -> room.getInterventions().stream().noneMatch(it ->
                        it.getDateTime().getStart().isBefore(dateTime) && it.getDateTime().getEnd().isAfter(dateTime) ||
                                it.getDateTime().getStart().isBefore(interventionEnd)
                                        && it.getDateTime().getEnd().isAfter(interventionEnd)));
    }

    private int sortFunction(ClinicRoom c1, ClinicRoom c2, String sort, boolean desc) {
        int sorted;
        switch (sort) {
            case "id":
                sorted = c1.getId().compareTo(c2.getId());
                break;
            case "name":
                sorted = c1.getName().compareTo(c2.getName());
                break;
            default:
                sorted = -1;
        }
        sorted = desc ? sorted * -1 : sorted;
        return sorted;
    }
}
