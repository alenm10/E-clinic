package mrs.eclinicapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mrs.eclinicapi.generator.IdGenerator;
import mrs.eclinicapi.model.enums.Weekday;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class WorkingCalendar {
    @Id
    @Column(length = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wc_seq")
    @GenericGenerator(name = "wc_seq",
            strategy = "mrs.eclinicapi.generator.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "WC")})

    String id;

    @ElementCollection
    private Set<TimePeriod<LocalDate>> vacations;

    @ElementCollection
    private Map<Weekday, TimePeriod<LocalTime>> workingSchedule;

    public WorkingCalendar() {
        vacations = new HashSet<>();
        workingSchedule = new HashMap<>();
        TimePeriod<LocalTime> ninethrufive = new TimePeriod<>();
        ninethrufive.setStart(LocalTime.of(9, 0));
        ninethrufive.setEnd(LocalTime.of(17, 0));
        workingSchedule.put(Weekday.MONDAY, ninethrufive);
        workingSchedule.put(Weekday.TUESDAY, ninethrufive);
        workingSchedule.put(Weekday.WEDNESDAY, ninethrufive);
        workingSchedule.put(Weekday.THURSDAY, ninethrufive);
        workingSchedule.put(Weekday.FRIDAY, ninethrufive);
    }
    
    public void addVacation(TimePeriod<LocalDate> v) {
    	vacations.add(v);
    }
}
