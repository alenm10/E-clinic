package mrs.eclinicapi.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class EmailEvent extends ApplicationEvent {
    private String subject;
    private String content;
    private String[] address;

    public EmailEvent(Object source, String subject, String content, String[] address) {
        super(source);

        this.subject = subject;
        this.content = content;
        this.address = address;
    }

}
