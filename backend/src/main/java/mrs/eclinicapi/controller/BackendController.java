package mrs.eclinicapi.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(path = "/error")
public class BackendController implements ErrorController {

    @RequestMapping
    public ModelAndView forward() {
        return new ModelAndView("forward:/");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
