package mrs.eclinicapi.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.time.temporal.Temporal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class TimePeriod<T extends Temporal> {
    private T start;
    private T end;
}
