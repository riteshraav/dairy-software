package com.dairy.take12.controller;

import com.dairy.take12.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
@CrossOrigin(origins = "*")

public class SmsController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/send/{to}/{msg}")
    public String sendSms(@PathVariable String to, @PathVariable String msg) {

        return smsService.sendSms(to, msg);
    }
    @PostMapping("/sendOtp/{to}/{msg}")
    public String sendOtp(@PathVariable String to,@PathVariable String msg) {
        // Send OTP and return a response
        // Here, you would typically save the OTP and phone number in the database for verification
        return smsService.sendOtp(to,msg);
    }

}
