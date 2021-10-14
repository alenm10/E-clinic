package mrs.eclinicapi.service;

import mrs.eclinicapi.model.Medicine;
import mrs.eclinicapi.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository repository;

    @Autowired
    public MedicineService(MedicineRepository repository) {
        this.repository = repository;
    }

    public List<Medicine> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Medicine modify(Medicine medicine) {
        return repository.save(medicine);
    }

    @Transactional
    public Medicine add(Medicine medicine) {
        return repository.save(medicine);
    }

    @Transactional
    public Medicine findOne(String id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
