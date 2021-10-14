package mrs.eclinicapi.service;

import mrs.eclinicapi.model.Doctor;
import mrs.eclinicapi.model.Nurse;
import mrs.eclinicapi.repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class NurseService {

    @Autowired
    private NurseRepository repository;

    public Nurse addNurse(Nurse newNurse) {
        return repository.save(newNurse);
    }

    public Nurse findOne(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<Nurse> findAll() {
        return repository.findAll();
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public List<Nurse> getNurseForClinic(String id) {

        return repository.getNurseForClinic(id);
    }
    
    public Nurse findByUserID(String userID) {
        return repository.findNurseByUser_Id(userID).orElse(null);

    }
}
