package Stock.Business;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class UniqueStringGenerator {
    public static String generateUniqueString(String inputString) {

        String encodedString = new String(Base64.getEncoder().encode(inputString.getBytes()));
        return encodedString;
    }

    public static String convertBackToString(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes, StandardCharsets.ISO_8859_1);
        return decodedString;
    }

}
