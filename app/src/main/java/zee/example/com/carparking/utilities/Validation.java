package zee.example.com.carparking.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zul Qarnain on 11/3/2017.
 */

public class Validation {
    private static final Pattern REGEX_EMAIL =Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static boolean isEmailValid(String args){
        Matcher matcher =REGEX_EMAIL.matcher(args);

        return matcher.find();
    }
}
