package mrs.eclinicapi.service;

import mrs.eclinicapi.model.Doctor;
import mrs.eclinicapi.model.Intervention;
import mrs.eclinicapi.model.Patient;
import mrs.eclinicapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class InterventionService {

    @Autowired
    private InterventionRepository repository;

    @Autowired
    private ClinicRoomRepository clinicRoomRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private InterventionTypeRepository interventionTypeRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Transactional
    public Intervention add(Intervention newIntervention) {
        Doctor d = newIntervention.getDoctor();
        d.setChangeTime(LocalDateTime.now());
        doctorRepository.save(d);
        return repository.save(newIntervention);
    }

    public Intervention findOne(String id) {
        return repository.findById(id).orElse(null);
    }

    public Intervention findOneClick(String id) {return repository.findByIdAndPatientNull(id).orElse(null); }

    public List<Intervention> findAll() {
        return repository.findAll();
    }

    public Intervention deleteById(String id) {
        Intervention i = repository.findById(id).orElse(null);
        if (i == null) return null;
        repository.deleteById(id);
        return i;
    }

    @Transactional
    public Intervention scheduleOneClick(String oneClickID, String userID) {
        Intervention oneClick = repository.findByIdAndPatientNull(oneClickID).orElse(null);
        if(oneClick == null) return null;
        Patient p = patientRepository.findPatientByUser_Id(userID);
        if(p == null) return null;
        oneClick.setPatient(p);
        return oneClick;
    }

    public Intervention modifyOneClick(String id, Intervention appointment) {
        Intervention oneClickAppointment = repository.findById(id).orElse(null);
        if (oneClickAppointment == null) return null;
        if (!oneClickAppointment.getId().equals(appointment.getId())) return null;
        oneClickAppointment.setClinic(appointment.getClinic());
        oneClickAppointment.setClinicRoom(appointment.getClinicRoom());
        oneClickAppointment.setDoctor(appointment.getDoctor());
        oneClickAppointment.setDateTime(appointment.getDateTime());
        oneClickAppointment.setInterventionType(appointment.getInterventionType());
        return repository.save(oneClickAppointment);
    }

    public List<Intervention> getClinicIntervention(String clinicId) {
        return repository.getClinicIntervention(clinicId);
    }

    public List<Intervention> getPatientIntervention(String patientId) {
        return repository.findInterventionsByPatient_IdAndVisitNotNull(patientId);
    }

    public Intervention findUpcoming(String doctorID, String patientID) {
        List<Intervention> interventions = repository.findInterventionsByDoctor_IdAndPatient_Id(doctorID, patientID);
        if(interventions.isEmpty()) return null;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fifteen = now.plusMinutes(20);
        return interventions.stream().filter(in -> (in.getDateTime().getStart().isAfter(now)
                && in.getDateTime().getStart().isBefore(fifteen))
                || (in.getDateTime().getStart().isBefore(now) && in.getDateTime().getEnd().isAfter(now))
                && in.getVisit() == null).findFirst().orElse(null);
    }

    public Intervention findOngoing(String doctorID) {
        List<Intervention> interventions = repository.findInterventionsByDoctor_Id(doctorID);
        if(interventions.isEmpty()) return null;
        LocalDateTime now = LocalDateTime.now();
        return interventions.stream().filter(in -> in.getDateTime().getStart().isBefore(now)
                && in.getDateTime().getEnd().isAfter(now)
                && in.getVisit() == null).findFirst().orElse(null);
    }

    public boolean pastIntervention(String doctorID, String patientID) {
        List<Intervention> interventions = repository.findInterventionsByDoctor_IdAndPatient_Id(doctorID, patientID);
        return interventions.stream().anyMatch(in -> in.getVisit() != null);
    }

    public List<Intervention> findInterventionsByInterventionType(String interventionTypeId){
    	return repository.findInterventionsByInterventionType(interventionTypeId);
    }

    public List<Intervention> findByClinicID(String clinicID) {
        return repository.findAllByClinicIdAndPatientNull(clinicID);
    }

    public Page<Intervention> findByClinicIDPaged(String clinicID, int pageNumber, int pageSize) {
        return this.findByClinicIDPaged(clinicID, pageNumber, pageSize, null, false);
    }

    public Page<Intervention> findByClinicIDPaged(String clinicID, int pageNumber, int pageSize, String sort, boolean desc) {
        Pageable p;
        sort = modifySort(sort);
        if (sort != null) {
            String[] sorts = sort.split(";");
            Sort s;
            if (desc) s = Sort.by(Sort.Direction.DESC, sorts);
            else s = Sort.by(Sort.Direction.ASC, sorts);
            p = PageRequest.of(--pageNumber, pageSize, s);
        } else p = PageRequest.of(--pageNumber, pageSize);
        return repository.findAllByClinicIdAndPatientNull(clinicID, p);
    }

    private String modifySort(String sort) {
        if (sort == null) return null;
        String modified;
        switch (sort) {
            case "typeName":
                modified = "interventionType.name";
                break;
            case "dateTime.start":
                modified = "dateTime.start";
                break;
            case "dateTime.end":
                modified = "dateTime.end";
                break;
            case "doctorName":
                modified = "doctor.user.surname;doctor.user.name";
                break;
            case "clinicRoomName":
                modified = "clinicRoom.name";
                break;
            case "price":
                modified = "interventionType.price";
                break;
            default:
                modified = "id";
        }
        return modified;
    }
}
