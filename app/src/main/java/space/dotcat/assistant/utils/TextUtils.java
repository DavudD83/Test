package space.dotcat.assistant.utils;

/**
 *  Util class for substitution native TextUtils. Created for unit testing.
 */
public class TextUtils {

    private TextUtils(){
    }

    public static boolean isEmpty(CharSequence sequence){
        return sequence == null || sequence.length() == 0;
    }
}
