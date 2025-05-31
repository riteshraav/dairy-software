package com.dairy.take12.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SmsService {

    @Value("${twilio.phone.number}")
    private String from;

    public String sendSms(String to, String msg) {
        try {
            Message message = Message.creator(
                            new PhoneNumber(to),
                            new PhoneNumber(from),
                            msg)
                    .create();
            return "Message sent successfully! SID: " + message.getSid();
        } catch (Exception e) {
            return "Failed to send message: " + e.getMessage();
        }
    }
    public String sendOtp(String toPhoneNumber,String msg) {
        // Generate OTP
        String otp = generateOtp();

        // Send OTP via Twilio
        String messageBody = msg + otp;
        Message message = Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(from),
                messageBody
        ).create();

        System.out.println("OTP sent successfully: " + message.getSid());
        System.out.println(otp);
        return otp; // Return the OTP for further processing (e.g., storing it in DB)
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000); // Generate 6-digit OTP
        return String.valueOf(otp);
    }
}

