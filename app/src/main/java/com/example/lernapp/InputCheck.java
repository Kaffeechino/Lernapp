package com.example.lernapp;

import java.util.regex.Pattern;

public class InputCheck {
    public static Boolean checkEmail(String email) {
        String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z0-9.-]$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(email).matches();
    }

    public String passwordCheck() {
        AndererCheck andererCheck = new InputCheck.AndererCheck();
        return andererCheck.myString;
    }

    public class AndererCheck {
        public String myString = "myString";

    }

    public interface interfaceCheck {
        String abstracteMethode();
    }
}
