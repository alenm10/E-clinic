package mrs.eclinicapi.service;

import mrs.eclinicapi.model.Patient;
import mrs.eclinicapi.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PatientService {

    private PatientRepository repository;

    @Autowired
    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public Patient addPatient(Patient p) {
        return this.repository.save(p);
    }

    public List<Patient> getPatients() {
        return this.repository.findAll();
    }

    public Patient getPatientById(String id) {
        return this.repository.findById(id).orElse(null);
    }

    @Transactional
    public Patient deletePatient(String id) throws Exception {
        Patient p = this.repository.findById(id).orElseThrow(Exception::new);
        this.repository.deleteById(id);
        return p;
    }

    public Page<Patient> findPaged(int pageNumber, int pageSize) {
        return this.findPaged(pageNumber, pageSize, null, false);
    }

    public Page<Patient> findPaged(int pageNumber, int pageSize, String sort, boolean desc) {
        Pageable p;
        if (sort != null) {
            sort = "user." + sort; // za ime, prezime i personalId jer drugo ni ne treba
            Sort s;
            if (desc) s = Sort.by(Sort.Direction.DESC, sort);
            else s = Sort.by(Sort.Direction.ASC, sort);
            p = PageRequest.of(--pageNumber, pageSize, s);
        } else p = PageRequest.of(--pageNumber, pageSize);
        return repository.findAll(p);
    }

    public Patient getByUserId(String userID) {
        return repository.findPatientByUser_Id(userID);
    }

    public Page<Patient> search(String firstName,
                               String lastName,
                               String personalID,
                               int pageNumber,
                               int pageSize) {
        return this.search(firstName, lastName, personalID, pageNumber, pageSize, null, false);
    }

    public Page<Patient> search(String firstName,
                               String lastName,
                               String personalID,
                               int pageNumber,
                               int pageSize,
                               String sort,
                               boolean desc) {
        Pageable p;
        int fakePageSize = pageSize;
        if (fakePageSize < 1) fakePageSize = 1000;
        if (sort != null) {
            Sort s;
            if (desc) s = Sort.by(Sort.Direction.DESC, sort);
            else s = Sort.by(Sort.Direction.ASC, sort);
            p = PageRequest.of(--pageNumber, fakePageSize, s);
        } else p = PageRequest.of(--pageNumber, fakePageSize);


        return this.someOtherFunction(firstName, lastName, personalID, p, pageSize);
    }

    private Page<Patient> someOtherFunction(String firstName, String lastName, String personalID, Pageable p, int pageSize) {
        List<Patient> patients = getPatients();
        Stream<Patient> filtered = this.filterPatients(patients, firstName, lastName, personalID);
        if (p.getSort().isSorted()) {
            Sort.Order o = p.getSort().iterator().next();
            String property = o.getProperty();
            boolean desc = o.getDirection().isDescending();
            filtered = filtered
                    .sorted((c1, c2) -> this.sortFunction(c1, c2, property, desc));
        }
        List<Patient> fullList = filtered.collect(Collectors.toList());
        if (pageSize < 1) return new PageImpl<>(fullList, p, fullList.size());
        else {
            int start = (int) p.getOffset();
            int end = Math.min((start + p.getPageSize()), fullList.size());
            return new PageImpl<>(fullList.subList(start, end), p, fullList.size());
        }
    }

    private Stream<Patient> filterPatients(List<Patient> patients, String firstName, String lastName, String personalID) {
        return patients.stream().filter(p -> p.getUser().getName().toLowerCase().contains(firstName.toLowerCase())
                && p.getUser().getSurname().toLowerCase().contains(lastName.toLowerCase())
                && p.getUser().getPersonalID().toLowerCase().contains(personalID.toLowerCase()));
    }

    private int sortFunction(Patient d1, Patient d2, String sort, boolean desc) {
        int sorted;
        switch (sort) {
            case "name":
                sorted = d1.getUser().getName().compareTo(d2.getUser().getName());
                break;
            case "surname":
                sorted = d1.getUser().getSurname().compareTo(d2.getUser().getSurname());
                break;
            case "personalID":
                sorted = d1.getUser().getPersonalID().compareTo(d2.getUser().getPersonalID());
                break;
            default:
                sorted = -1;
        }
        sorted = desc ? sorted * -1 : sorted;
        return sorted;
    }
}
