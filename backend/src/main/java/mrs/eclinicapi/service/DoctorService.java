package mrs.eclinicapi.service;

import mrs.eclinicapi.model.Doctor;
import mrs.eclinicapi.model.InterventionType;
import mrs.eclinicapi.model.enums.Weekday;
import mrs.eclinicapi.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class DoctorService {

    @Autowired
    private DoctorRepository repository;

    public Doctor addDoctor(Doctor newDoctor) {
        return repository.save(newDoctor);
    }

    public Doctor findOne(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<Doctor> findAll() {
        return repository.findAll();
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Doctor findByUserID(String userID) {
        return repository.findDoctorByUser_Id(userID).orElse(null);

    }

    public Page<Doctor> findPaged(int pageNumber, int pageSize) {
        return this.findPaged(pageNumber, pageSize, null, false);
    }

    public Page<Doctor> findPaged(int pageNumber, int pageSize, String sort, boolean desc) {
        Pageable p;
        if (sort != null) {
            Sort s;
            if (desc) s = Sort.by(Sort.Direction.DESC, sort);
            else s = Sort.by(Sort.Direction.ASC, sort);
            p = PageRequest.of(--pageNumber, pageSize, s);
        } else p = PageRequest.of(--pageNumber, pageSize);
        return repository.findAll(p);
    }

    public List<Doctor> findByClinicID(String clinicID) {
        return repository.findDoctorsByClinic_Id(clinicID);
    }

    public Page<Doctor> findByClinicIDPaged(String clinicID, int pageNumber, int pageSize) {
        return this.findByClinicIDPaged(clinicID, pageNumber, pageSize, null, false);
    }

    public Page<Doctor> findByClinicIDPaged(String clinicID, int pageNumber, int pageSize, String sort, boolean desc) {
        Pageable p;
        if (sort != null) {
            Sort s;
            if (desc) s = Sort.by(Sort.Direction.DESC, sort);
            else s = Sort.by(Sort.Direction.ASC, sort);
            p = PageRequest.of(--pageNumber, pageSize, s);
        } else p = PageRequest.of(--pageNumber, pageSize);
        return repository.findDoctorsByClinic_Id(clinicID, p);
    }

    public Page<Doctor> search(String searchQuery,
                               String clinicID,
                               LocalDate date,
                               InterventionType type,
                               int pageNumber,
                               int pageSize) {
        return this.search(searchQuery, clinicID, date, type, pageNumber, pageSize, null, false);
    }

    public Page<Doctor> search(String searchQuery,
                               String clinicID,
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


        return this.someOtherFunction(searchQuery, clinicID, date, type, p, pageSize);
    }

    private Page<Doctor> someOtherFunction(String searchQuery, String clinicID, LocalDate date, InterventionType type, Pageable p, int pageSize) {
        List<Doctor> doctors = findByClinicID(clinicID);
        Stream<Doctor> filtered = this.filterDoctors(doctors, searchQuery, date, type);
        if (p.getSort().isSorted()) {
            Sort.Order o = p.getSort().iterator().next();
            String property = o.getProperty();
            boolean desc = o.getDirection().isDescending();
            filtered = filtered
                    .sorted((c1, c2) -> this.sortFunction(c1, c2, property, desc));
        }
        List<Doctor> fullList = filtered.collect(Collectors.toList());
        if (pageSize < 1) return new PageImpl<>(fullList, p, fullList.size());
        else {
            int start = (int) p.getOffset();
            int end = Math.min((start + p.getPageSize()), fullList.size());
            return new PageImpl<>(fullList.subList(start, end), p, fullList.size());
        }
    }

    private Stream<Doctor> filterDoctors(List<Doctor> doctors, String searchQuery, LocalDate date, InterventionType type) {
        Weekday weekday = Weekday.values()[date.getDayOfWeek().getValue() - 1];
        return doctors.stream()
                .filter(doctor -> doctor.getUser().getName().toLowerCase().contains(searchQuery) ||
                        doctor.getUser().getSurname().toLowerCase().contains(searchQuery) ||
                        doctor.getUser().getPhoneNumber().toLowerCase().contains(searchQuery))
                .filter(doctor -> doctor.getSpecialties().contains(type))
                .filter(doctor -> doctor.getWorkingCalendar().getVacations()
                        .stream().noneMatch(v -> date.isAfter(v.getStart()) && date.isBefore(v.getEnd()))
                        && doctor.getWorkingCalendar().getWorkingSchedule().containsKey(weekday)
                );
    }

    private int sortFunction(Doctor d1, Doctor d2, String sort, boolean desc) {
        int sorted;
        switch (sort) {
            case "user.name":
                sorted = d1.getUser().getName().compareTo(d2.getUser().getName());
                break;
            case "user.surname":
                sorted = d1.getUser().getSurname().compareTo(d2.getUser().getSurname());
                break;
            case "user.email":
                sorted = d1.getUser().getEmail().compareTo(d2.getUser().getEmail());
                break;
            case "user.phoneNumber":
                sorted = d1.getUser().getPhoneNumber().compareTo(d2.getUser().getPhoneNumber());
                break;
            case "user.city":
                sorted = d1.getUser().getCity().compareTo(d2.getUser().getCity());
                break;
            case "user.country":
                sorted = d1.getUser().getCountry().compareTo(d2.getUser().getCountry());
                break;
            case "user.address":
                sorted = d1.getUser().getAddress().compareTo(d2.getUser().getAddress());
                break;
            default:
                sorted = -1;
        }
        sorted = desc ? sorted * -1 : sorted;
        return sorted;
    }

    public Page<Doctor> searchByTime(String clinicID,
                                     String typeID,
                                     LocalDateTime dateTime,
                                     int duration,
                                     int pageNumber,
                                     int pageSize) {
        return this.searchByTime(clinicID, typeID, dateTime, duration, pageNumber, pageSize, null, false);
    }

    public Page<Doctor> searchByTime(String clinicID,
                                     String typeID,
                                     LocalDateTime dateTime,
                                     int duration,
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

        return this.someOtherNewFunction(clinicID, typeID, dateTime, duration, p, pageSize);
    }

    private Page<Doctor> someOtherNewFunction(String clinicID, String typeID, LocalDateTime dateTime, int duration, Pageable p, int pageSize) {
        List<Doctor> doctors = findByClinicID(clinicID);
        Stream<Doctor> filtered = this.filterDoctorsByTime(doctors, typeID, dateTime, duration);
        if (p.getSort().isSorted()) {
            Sort.Order o = p.getSort().iterator().next();
            String property = o.getProperty();
            boolean desc = o.getDirection().isDescending();
            filtered = filtered
                    .sorted((c1, c2) -> this.sortFunction(c1, c2, property, desc));
        }
        List<Doctor> fullList = filtered.collect(Collectors.toList());
        if (pageSize < 1) return new PageImpl<>(fullList, p, fullList.size());
        else {
            int start = (int) p.getOffset();
            int end = Math.min((start + p.getPageSize()), fullList.size());
            return new PageImpl<>(fullList.subList(start, end), p, fullList.size());
        }
    }

    private Stream<Doctor> filterDoctorsByTime(List<Doctor> doctors, String typeID, LocalDateTime dateTime, int duration) {
        LocalDateTime endTime = dateTime.plusMinutes(duration);
        Weekday weekday = Weekday.values()[dateTime.getDayOfWeek().getValue() - 1];
        return doctors.stream()
                .filter(doctor -> doctor.getSpecialties().stream().anyMatch(type -> type.getId().equals(typeID)))
                .filter(doctor -> doctor.getWorkingCalendar().getVacations()
                                .stream().noneMatch(v -> dateTime.isAfter(v.getStart().atStartOfDay())
                                        && dateTime.isBefore(v.getEnd().atStartOfDay()))
                                && doctor.getWorkingCalendar().getWorkingSchedule().containsKey(weekday)
                                && dateTime.toLocalTime().isAfter(doctor.getWorkingCalendar().getWorkingSchedule().get(weekday).getStart())
                                && dateTime.toLocalTime().isBefore(doctor.getWorkingCalendar().getWorkingSchedule().get(weekday).getEnd())
                                && endTime.toLocalTime().isAfter(doctor.getWorkingCalendar().getWorkingSchedule().get(weekday).getStart())
                                && endTime.toLocalTime().isBefore(doctor.getWorkingCalendar().getWorkingSchedule().get(weekday).getEnd())
                                && doctor.getAppointmentRequests().stream().noneMatch(request -> {
                            LocalDateTime currentStartTime = request.getDateTime();
                            LocalDateTime currentEndTime = currentStartTime.plusMinutes(30);

                            if (dateTime.isAfter(currentStartTime) && dateTime.isBefore(currentEndTime))
                                return true;
                            return endTime.isAfter(currentStartTime) && endTime.isBefore(currentEndTime);
                        })
                                && doctor.getInterventions().stream().noneMatch(intervention -> {
                            LocalDateTime currentStartTime = intervention.getDateTime().getStart();
                            LocalDateTime currentEndTime = intervention.getDateTime().getEnd();

                            if (dateTime.isAfter(currentStartTime) && dateTime.isBefore(currentEndTime))
                                return true;
                            return endTime.isAfter(currentStartTime) && endTime.isBefore(currentEndTime);
                        })
                );
    }
}
