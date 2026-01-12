package base;

import java.util.Random;

public class Utilities {
    public String generateRandomRoomId() {

        final Random random = new Random();
        return String.valueOf(2000 + random.nextInt(900));

    }
}
