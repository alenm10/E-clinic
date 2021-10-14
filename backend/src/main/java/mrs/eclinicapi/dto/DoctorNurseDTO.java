package mrs.eclinicapi.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mrs.eclinicapi.model.Intervention;
import mrs.eclinicapi.model.TimePeriod;
import mrs.eclinicapi.model.enums.Weekday;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class DoctorNurseDTO {

    private String id;
    private String userID;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String personalID;
    private String clinicID;
    private List<String> specialties;
    private Map<Weekday, TimePeriod<LocalTime>> workingSchedule;
    private List<TimePeriod<LocalDateTime>> busyTimes;
    private List<TimePeriod<LocalDateTime>> vacations;
    private List<AppointmentRequestDTO> appointmentRequests;
    private List<TimePeriod<LocalDateTime>> oneClickAppointments;
    private double avgRating;

    @Override
    public String toString() {
        return "DoctorNurseDTO [id=" + id + ", userID=" + userID + ", email=" + email + ", password=" + password
                + ", name=" + name + ", surname=" + surname + ", phoneNumber=" + phoneNumber + ", address=" + address
                + ", city=" + city + ", country=" + country + ", personalID=" + personalID + ", clinicID=" + clinicID
                + ", specialties=" + specialties + ", rating = " + avgRating + "]";
    }


}
