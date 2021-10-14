package mrs.eclinicapi.service;

import mrs.eclinicapi.model.MedicalRecord;
import mrs.eclinicapi.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordService {

    private MedicalRecordRepository repository;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository repository) {
        this.repository = repository;
    }

    public MedicalRecord save(MedicalRecord medicalRecord) {
        return this.repository.save(medicalRecord);
    }
}
