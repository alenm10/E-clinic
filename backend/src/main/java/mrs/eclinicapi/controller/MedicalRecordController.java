package mrs.eclinicapi.controller;

import mrs.eclinicapi.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/medicalrecord")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService service;
}
