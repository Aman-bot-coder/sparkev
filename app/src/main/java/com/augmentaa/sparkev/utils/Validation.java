
package com.augmentaa.sparkev.utils;

import android.text.Html;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ohm on 29-07-2018.
 */
public class Validation {
    private static Pattern pattern;
    private static Matcher matcher;
    //Email Pattern
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Validate Email with regular expression
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validatename(String name) {
        Pattern p = Pattern.compile("[^A-Za-z0-9\\s]");
        Matcher m = p.matcher(name);
        // boolean b = m.matches();
        return m.find();
    }

    public static boolean validatecarno(String carnumber) {
        //Pattern pattern1 = Pattern.compile("^[A-Z]{2}[A-Z0-9]{2}[A-Z]{1}[0-9]{4}$");
        //Pattern pattern2 = Pattern.compile("^[A-Z]{2}[0-9]{1}[A-Z0-9]{1}[A-Z]{1}[0-9]{4}$");
        Pattern pattern1 = Pattern.compile("^[a-zA-Z]{2}[a-zA-Z0-9]{1}[0-9]{4}$");
        Pattern pattern2 = Pattern.compile("^[a-zA-Z]{2}[a-zA-Z0-9]{2}[0-9]{4}$");
        Pattern pattern3 = Pattern.compile("^[a-zA-Z]{2}[a-zA-Z0-9]{3}[0-9]{4}$");
        Pattern pattern4 = Pattern.compile("^[a-zA-Z]{2}[a-zA-Z0-9]{4}[0-9]{4}$");
        Matcher m1 = pattern1.matcher(carnumber);
        Matcher m2 = pattern2.matcher(carnumber);
        Matcher m3 = pattern3.matcher(carnumber);
        Matcher m4 = pattern4.matcher(carnumber);
        boolean b = m1.find();
        boolean c = m2.find();
        boolean d = m3.find();
        boolean e = m4.find();
        boolean carno = b || c || d || e;
        return carno;
    }
public static boolean validatepwd(String pwd)
{
    //Pattern pattern=Pattern.compile("^[A-Za-z0-9@#$%]{8,16}$");
    Pattern pattern =
            Pattern.compile("^(?=(.*\\d){1})(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%=]{8,16}$");


    Matcher m1 = pattern.matcher(pwd);
    boolean b = m1.find();

    return b;
}
{

}
    public static Spanned stripHtml(String html) {
        return Html.fromHtml(html);
    }
}
