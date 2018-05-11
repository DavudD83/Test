package space.dotcat.assistant.utils;

public class AddressUtils {

    private final static String API_PATH = "/api/rest";

    private final static String STREAMING_PATH = "/api/streaming";

    private final static String API_VERSION = "/v1/";

    private final static String HOST_PORT_DIVIDER = ":";

    private final static String HTTP_INSECURE_LEVEL = "http://";

    private final static String HTTP_SECURE_LEVEL = "https://";

    private final static String WS_INSECURE_LEVEL = "ws://";

    private final static String WS_SECURE_LEVEL = "wss://";

    public static String createBaseAddress(boolean isSecured, String host, String port) {
        String security_level;

        security_level = isSecured ? HTTP_SECURE_LEVEL : HTTP_INSECURE_LEVEL;

        return security_level + createAddress(host, port) + API_PATH + API_VERSION;
    }

    public static String createSteamingAddress(boolean isSecured, String host, String port) {
        String security_level;

        security_level = isSecured ? WS_SECURE_LEVEL : WS_INSECURE_LEVEL;

        return security_level + createAddress(host, port) + STREAMING_PATH + API_VERSION;
    }

    private static String createAddress(String host, String port) {
        return host + HOST_PORT_DIVIDER + port;
    }
}
