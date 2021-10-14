package mrs.eclinicapi.service;

import mrs.eclinicapi.model.Medicine;
import mrs.eclinicapi.model.Visit;
import mrs.eclinicapi.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    private VisitRepository repository;

    @Autowired
    public VisitService(VisitRepository repository) {
        this.repository = repository;
    }

    public Visit save(Visit visit) {
        return this.repository.save(visit);
    }

    public List<Visit> findAll(){ return this.repository.findAll();}

    public Optional<Visit> findById(String id){ return this.repository.findById(id);}

    public List<Visit> findAllByClinic(String clinicId) {

        List<Visit> allVisits =  this.repository.findAll();
        ArrayList<Visit> correctVisits = new ArrayList<>();
        for (Visit v:  allVisits) {
            if(v.getIntervention().getClinic().getId().equalsIgnoreCase(clinicId)){
                correctVisits.add(v);
            }
        }
        return correctVisits;
    }

    @Transactional
    public Visit modify(Visit visit) {
        return this.repository.save(visit);
    }
}
