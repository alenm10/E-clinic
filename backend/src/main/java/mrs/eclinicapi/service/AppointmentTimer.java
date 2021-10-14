package mrs.eclinicapi.service;

import mrs.eclinicapi.model.AppointmentRequest;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AppointmentTimer extends TimerTask {

    Map<String, Timer> timers;
    AppointmentRequest appointmentRequest;

    public AppointmentTimer(Map<String, Timer> timers, AppointmentRequest request) {
        super();
        this.timers = timers;
        this.appointmentRequest = request;
    }

    @Override
    public void run() {
        System.out.println("ZA BRISANJE");
        timers.remove(appointmentRequest.getId());
        cancel();
    }
}
