package mrs.eclinicapi.service;

import mrs.eclinicapi.model.DoctorRating;
import mrs.eclinicapi.repository.DoctorRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class DoctorRatingService {

    private DoctorRatingRepository repository;

    @Autowired
    public DoctorRatingService(DoctorRatingRepository repository) {
        this.repository = repository;
    }

    public DoctorRating save(DoctorRating r) {return repository.save(r);}

    public DoctorRating get(String doctorID, String patientID) {
        return repository.findByDoctor_IdAndPatient_Id(doctorID, patientID).orElse(null);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
