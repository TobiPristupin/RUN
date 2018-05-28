package com.tobipristupin.simplerun.utils;


import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailValidator {

    private EmailValidator(){}

    public static boolean isValid(String email){
        try {
            InternetAddress address = new InternetAddress(email);
            address.validate();
        } catch (AddressException e){
            return false;
        }

        return true;
    }

}
