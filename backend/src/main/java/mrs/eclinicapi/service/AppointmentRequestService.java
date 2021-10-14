package mrs.eclinicapi.service;

import mrs.eclinicapi.dto.AppointmentRequestDTO;
import mrs.eclinicapi.model.AppointmentRequest;
import mrs.eclinicapi.model.Doctor;
import mrs.eclinicapi.repository.AppointmentRequestRepository;
import mrs.eclinicapi.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AppointmentRequestService {
    @Autowired
    private AppointmentRequestRepository repository;

    @Autowired
    private DoctorRepository doctorRepository;

    public List<AppointmentRequest> findAll() {
        return repository.findAll();
    }

    public AppointmentRequest findOne(String id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public AppointmentRequest delete(String id) {
        AppointmentRequest request = repository.findById(id).orElse(null);
        if (request == null) return null;
        LocalDateTime doc = request.getDateTime();
        long diff = LocalDateTime.now().until(doc, ChronoUnit.MILLIS);
        if (diff < 86400000) return null;
        repository.delete(request);
        return request;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = AppointmentRequestDTO.ConcurrentRequest.class)
    public void delete(AppointmentRequest request) throws AppointmentRequestDTO.ConcurrentRequest {
        repository.delete(request);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = AppointmentRequestDTO.ConcurrentRequest.class)
    public AppointmentRequest modify(String id, AppointmentRequest request) {
        AppointmentRequest ar = repository.findById(id).orElse(null);
        if (ar == null) return null;
        ar.setDoctor(request.getDoctor());
        ar.setDateTime(request.getDateTime());

        List<AppointmentRequest> requests = repository.findAppointmentRequestsByDoctor_Id(ar.getDoctor().getId());

        if (checkRequests(requests, ar))
            return repository.save(ar);
        else
            return null;

    }

    @Transactional
    public AppointmentRequest save(AppointmentRequest ar) {
        Doctor d = ar.getDoctor();
        List<AppointmentRequest> requests = repository.findAppointmentRequestsByDoctor_Id(d.getId());

        if (checkRequests(requests, ar)) {
            d.setChangeTime(LocalDateTime.now());
            doctorRepository.save(d);
            return repository.save(ar);
        }
        else
            return null;
    }

    private boolean checkRequests(List<AppointmentRequest> requests, AppointmentRequest ar) {
        LocalDateTime startTime = ar.getDateTime();
        LocalDateTime endTime = startTime.plusMinutes(30);
        for (AppointmentRequest request : requests) {
            LocalDateTime currentStartTime = request.getDateTime();
            LocalDateTime currentEndTime = currentStartTime.plusMinutes(30);


            if (startTime.isAfter(currentStartTime) && startTime.isBefore(currentEndTime))
                return false;
            if (endTime.isAfter(currentStartTime) && endTime.isBefore(currentEndTime))
                return false;
        }
        return true;
    }

    public Page<AppointmentRequest> findPaged(int pageNumber, int pageSize) {
        return this.findPaged(pageNumber, pageSize, null, false);
    }

    public Page<AppointmentRequest> findPaged(int pageNumber, int pageSize, String sort, boolean desc) {
        Pageable p;
        if (sort != null) {
            Sort s;
            if (desc) s = Sort.by(Sort.Direction.DESC, sort);
            else s = Sort.by(Sort.Direction.ASC, sort);
            p = PageRequest.of(--pageNumber, pageSize, s);
        } else p = PageRequest.of(--pageNumber, pageSize);
        return repository.findAll(p);
    }

    public List<AppointmentRequest> findByClinicID(String clinicID) {
        return repository.findAppointmentRequestsByClinic_Id(clinicID);
    }

    public Page<AppointmentRequest> findByClinicIDPaged(String clinicID, int pageNumber, int pageSize) {
        return this.findByClinicIDPaged(clinicID, pageNumber, pageSize, null, false);
    }

    public Page<AppointmentRequest> findByClinicIDPaged(String clinicID, int pageNumber, int pageSize, String sort, boolean desc) {
        Pageable p;
        if (sort != null) {
            Sort s;
            if (desc) s = Sort.by(Sort.Direction.DESC, sort);
            else s = Sort.by(Sort.Direction.ASC, sort);
            p = PageRequest.of(--pageNumber, pageSize, s);
        } else p = PageRequest.of(--pageNumber, pageSize);
        return repository.findAppointmentRequestsByClinic_Id(clinicID, p);
    }

    public List<AppointmentRequest> findByPatientID(String patientID) {
        return repository.findAppointmentRequestsByPatient_Id(patientID);
    }

    public Page<AppointmentRequest> findByPatientIDPaged(String patientID, int pageNumber, int pageSize) {
        return this.findByPatientIDPaged(patientID, pageNumber, pageSize, null, false);
    }

    public Page<AppointmentRequest> findByPatientIDPaged(String patientID, int pageNumber, int pageSize, String sort, boolean desc) {
        Pageable p;
        if (sort != null) {
            Sort s;
            if (desc) s = Sort.by(Sort.Direction.DESC, sort);
            else s = Sort.by(Sort.Direction.ASC, sort);
            p = PageRequest.of(--pageNumber, pageSize, s);
        } else p = PageRequest.of(--pageNumber, pageSize);
        return repository.findAppointmentRequestsByPatient_Id(patientID, p);
    }
}
