package com.neology.parking.util.initializer;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class LicensePlateGen {

    private static final Random RAND = new Random();
    private static final int PLATE_START = 'A';
    private static final int PLATE_END = 'Z';
    private static final int CHARS_LENGTH = PLATE_END - PLATE_START + 1;
    public static final String[] FORMATS = new String[] {
            "AAA-XX-XX",
            "XX-XX-AAA",
            "XXX-AAA",
            "AAA-XXX-A"
    };

    public static String generateRandomPlate() {
        return generateRandomPlate(FORMATS[RAND.nextInt(FORMATS.length)]);
    }

    public static String generateRandomPlate(String format) {
        return Arrays.stream(format.split(""))
                .map(LicensePlateGen::getRandomPlatePart)
                .collect(Collectors.joining(""));
    }

    private static String getRandomPlatePart(String s) {
        if ("-".equals(s)) {
            return s;
        }
        if ("X".equals(s)) {
            return String.valueOf(RAND.nextInt(10));
        }
        return String.valueOf((char) (PLATE_START + RAND.nextInt(CHARS_LENGTH)));
    }
}
