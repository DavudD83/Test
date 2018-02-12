package space.dotcat.assistant.utils;


public class UrlUtils {

    public UrlUtils() {
    }

    public static boolean isValidURL(String url) {
        return url.startsWith("http://") || (url.startsWith("https://"));
    }
}
