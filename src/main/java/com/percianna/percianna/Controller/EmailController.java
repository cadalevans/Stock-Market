package com.percianna.percianna.Controller;

import com.percianna.percianna.Dto.MailRequest;
import com.percianna.percianna.Dto.MailResponse;
import com.percianna.percianna.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EmailController {

    @Autowired
    private EmailService service;

    @PostMapping("/sendingEmail")
    public MailResponse sendEmail(@RequestBody MailRequest request) {
        Map<String, Object> model = new HashMap<>();
        model.put("Name", request.getName());
        model.put("location", "Ariana,Tunisia");
        return service.sendEmail(request, model);

    }
}
