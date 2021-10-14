package mrs.eclinicapi.service;

import mrs.eclinicapi.model.Diagnosis;
import mrs.eclinicapi.repository.DiagnosisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class DiagnosisService {

    private DiagnosisRepository repository;

    @Autowired
    public DiagnosisService(DiagnosisRepository repository) {
        this.repository = repository;
    }

    public List<Diagnosis> findAll() {
        return repository.findAll();
    }

    public Diagnosis add(Diagnosis diagnosis) {
        return repository.save(diagnosis);
    }

    public Diagnosis modify(Diagnosis diagnosis) {
        return repository.save(diagnosis);
    }

    public Diagnosis findOne(String id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
