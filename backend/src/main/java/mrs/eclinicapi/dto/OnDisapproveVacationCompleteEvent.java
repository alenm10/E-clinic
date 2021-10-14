package mrs.eclinicapi.dto;

import lombok.Getter;
import lombok.Setter;
import mrs.eclinicapi.model.User;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnDisapproveVacationCompleteEvent extends ApplicationEvent {
    private User user;
    private String reason;

    public OnDisapproveVacationCompleteEvent(User user, String reason) {
        super(user);
        this.user = user;
        this.reason = reason;
    }

}
