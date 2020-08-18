package com.apps.hackerrank.testdome;

public class UsernamePattern {

    public static boolean validate(String username) {
        return isValidLength(username) &&
                isValidStart(username) &&
                isValidChars(username) &&
                isValidDotNumber(username) &&
                isValidEnd(username);
    }

    public static boolean isValidLength(String username) {
        if (username.length() < 6) {
            return false;
        }
        if (username.length() > 20) {
            return false;
        }
        return true;
    }


    public static boolean isValidStart(String username) {
        return username.matches("^[A-Za-z].*");
    }

    public static boolean isValidChars(String username) {
        return username.matches("^[\\w\\d\\.]*");
    }

    public static boolean isValidDotNumber(String username) {
        int count = username.length() - username.replace(".", "").length();
        return count <= 1;
    }

    public static boolean isValidEnd(String username) {
        return !username.substring(username.length() - 1).equals(".");
    }


    public static void main(String[] args) {
        System.out.println(validate("Roberhsagdhsgdhasdklsahdjsdh")); // Invalid size
        System.out.println(validate("Rober"));  // Invalid size
        System.out.println(validate("1Robert.Domek"));  // Invalid start
        System.out.println(validate("RobertDomek.")); // Invalid end dot
        System.out.println(validate("Robert.Do.mek")); // Invalid two dots
        System.out.println(validate("Robert.Domek")); // Valid username, returns true
        System.out.println(validate("Robert Domek")); // Invalid username, returns false
    }
}
