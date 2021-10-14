package mrs.eclinicapi.service;

import mrs.eclinicapi.model.VacationRequest;
import mrs.eclinicapi.repository.VacationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class VacationRequestService {

    @Autowired
    private VacationRequestRepository repository;

    public VacationRequest addVacationRequest(VacationRequest newVacation) {
        return repository.save(newVacation);
    }

    public VacationRequest findOne(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<VacationRequest> findAll() {
        return repository.findAll();
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public VacationRequest modifyVacationRequest(VacationRequest vacationRequest) {
        VacationRequest toModify = this.findOne(vacationRequest.getId());
        if (toModify == null) return null;
        toModify.setStartDate(vacationRequest.getStartDate());
        toModify.setEndDate(vacationRequest.getEndDate());
        toModify.setClinic(vacationRequest.getClinic());
        toModify.setUser(vacationRequest.getUser());
        toModify.setStatus(vacationRequest.getStatus());
        return repository.save(toModify);
    }

    public List<VacationRequest> getVacationRequestForClinic(String id) {
        return repository.getVacationRequestForClinic(id);
    }

    public List<VacationRequest> getVacationRequestForUser(String id) {
        return repository.getVacationRequestForUser(id);
    }
}
