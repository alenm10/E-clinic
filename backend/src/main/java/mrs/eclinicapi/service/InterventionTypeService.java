package mrs.eclinicapi.service;

import mrs.eclinicapi.model.InterventionType;
import mrs.eclinicapi.repository.InterventionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class InterventionTypeService {

    @Autowired
    private InterventionTypeRepository repository;

    public InterventionType add(InterventionType newDoctor) {
        return repository.save(newDoctor);
    }

    public InterventionType findOne(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<InterventionType> findAll() {
        return repository.findAll();
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public List<InterventionType> getClinicInterventionType(String clinicId) {
        return repository.getClinicInterventionType(clinicId);
    }

    @Transactional
    public List<InterventionType> findMany(List<String> ids) {
        List<InterventionType> interventionTypes = repository.findAllById(ids);
        if (interventionTypes.size() != ids.size()) return null;
        return interventionTypes;
    }

}
