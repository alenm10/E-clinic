package mrs.eclinicapi.service;

import mrs.eclinicapi.model.ClinicRating;
import mrs.eclinicapi.repository.ClinicRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class ClinicRatingService {

    private ClinicRatingRepository repository;

    @Autowired
    public ClinicRatingService(ClinicRatingRepository repository) {
        this.repository = repository;
    }

    public ClinicRating save(ClinicRating r) {return repository.save(r);}

    public ClinicRating get(String clinicID, String patientID) {
        return repository.findByClinic_IdAndPatient_Id(clinicID, patientID).orElse(null);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
