package mrs.eclinicapi.service;


import mrs.eclinicapi.model.*;
import mrs.eclinicapi.model.enums.Weekday;
import mrs.eclinicapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class ClinicService {

    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClinicAdministratorRepository clinicAdministratorRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private NurseRepository nurseRepository;

    public Clinic addClinic(Clinic newClinic) {
        return clinicRepository.save(newClinic);
    }

    public Clinic findOne(String id) {
        if(id == null) return null;
        return clinicRepository.findById(id).orElse(null);
    }

    @Transactional
    public Clinic modifyClinic(Clinic clinic) {
        Clinic toModify = this.findOne(clinic.getId());
        if (toModify == null) return null;
        toModify.setName(clinic.getName());
        toModify.setCoordinates(clinic.getCoordinates());
        toModify.setDescription(clinic.getDescription());
        toModify.setAddress(clinic.getAddress());
        return clinicRepository.save(toModify);
    }

    public Clinic findByUser(String userID) {
        Optional<User> user = userRepository.findById(userID);
        if (!user.isPresent()) {

            return null;
        }

        Optional<ClinicAdministrator> clinicAdministrator = clinicAdministratorRepository.findClinicAdministratorByUser(user.get());
        Optional<Doctor> doctor = doctorRepository.findDoctorByUser(user.get());
        Optional<Nurse> nurse = nurseRepository.findNurseByUser(user.get());
        if (clinicAdministrator.isPresent()) return clinicAdministrator.get().getClinic();
        if (doctor.isPresent()) return doctor.get().getClinic();
        return nurse.map(MedicalStaff::getClinic).orElse(null);
    }

    public List<Clinic> findAll() {
        return clinicRepository.findAll();
    }

    public void deleteById(String id) {
        clinicRepository.deleteById(id);
    }

    public Page<Clinic> findPaged(int pageNumber, int pageSize) {
        return this.findPaged(pageNumber, pageSize, null, false);
    }

    public Page<Clinic> findPaged(int pageNumber, int pageSize, String sort, boolean desc) {
        Pageable p;
        if (sort != null) {
            Sort s;
            if (desc) s = Sort.by(Sort.Direction.DESC, sort);
            else s = Sort.by(Sort.Direction.ASC, sort);
            p = PageRequest.of(--pageNumber, pageSize, s);
        } else p = PageRequest.of(--pageNumber, pageSize);
        return clinicRepository.findAll(p);
    }

    public void deleteAll() {
        clinicRepository.deleteAll();
    }

    public void deleteOne(Clinic clinicToDelete) {
        clinicRepository.delete(clinicToDelete);
    }


    public Page<Clinic> search(String searchQuery, LocalDate date, InterventionType type, int pageNumber, int pageSize) {
        return this.search(searchQuery, date, type, pageNumber, pageSize, null, false);
    }

    public Page<Clinic> search(String searchQuery,
                               LocalDate date,
                               InterventionType type,
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


        return this.someOtherFunction(searchQuery, date, type, p, pageSize);
    }

    private Page<Clinic> someOtherFunction(String searchQuery, LocalDate date, InterventionType type, Pageable p, int pageSize) {
        List<Clinic> clinics = findAll();
        Stream<Clinic> filtered = this.filterClinics(clinics, searchQuery, date, type);
        if (p.getSort().isSorted()) {
            Sort.Order o = p.getSort().iterator().next();
            String property = o.getProperty();
            boolean desc = o.getDirection().isDescending();
            filtered = filtered
                    .sorted((c1, c2) -> this.sortFunction(c1, c2, property, desc));
        }
        List<Clinic> fullList = filtered.collect(Collectors.toList());
        if (pageSize < 1) return new PageImpl<>(fullList, p, fullList.size());
        else {
            int start = (int) p.getOffset();
            int end = Math.min((start + p.getPageSize()), fullList.size());
            return new PageImpl<>(fullList.subList(start, end), p, fullList.size());
        }
    }

    private Stream<Clinic> filterClinics(List<Clinic> clinics, String searchQuery, LocalDate date, InterventionType type) {
        Weekday weekday = Weekday.values()[date.getDayOfWeek().getValue() - 1];

        return clinics.stream()
                .filter(clinic -> clinic.getName().toLowerCase().contains(searchQuery))
                .filter(clinic -> clinic.getDoctors().stream()
                        .anyMatch(d -> d.getSpecialties().stream()
                                .anyMatch(s -> s == type)
                        )
                ).filter(clinic -> clinic.getDoctors().stream()
                        .anyMatch(d -> d.getWorkingCalendar().getVacations().stream()
                                .noneMatch(v -> date.isAfter(v.getStart()) && date.isBefore(v.getEnd()))
                                && d.getWorkingCalendar().getWorkingSchedule().containsKey(weekday)
                        )
                );
    }

    private int sortFunction(Clinic c1, Clinic c2, String sort, boolean desc) {
        int sorted;
        switch (sort) {
            case "name":
                sorted = desc ? c1.getName().compareTo(c2.getName()) * -1 : c1.getName().compareTo(c2.getName());
                break;
            case "address":
                sorted = desc ? c1.getAddress().compareTo(c2.getAddress()) * -1 : c1.getAddress().compareTo(c2.getAddress());
                break;
            default:
                sorted = -1;
        }
        return sorted;
    }
}
