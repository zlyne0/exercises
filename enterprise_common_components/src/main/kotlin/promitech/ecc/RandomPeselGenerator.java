package promitech.ecc;

import java.util.Random;

public class RandomPeselGenerator {

    private static final Random random = new Random(System.currentTimeMillis());

    public static Pesel nextPesel(int year, int month, int day) {
        int monthCode;
        if (year < 1900) {
            monthCode = 80 + month;
        } else if (year < 2000) {
            monthCode = month;
        } else if (year < 2100) {
            monthCode = 20 + month;
        } else if (year < 2200) {
            monthCode = 40 + month;
        } else if (year < 2300) {
            monthCode = 60 + month;
        } else {
            throw new IllegalArgumentException("year '" + year + "' out of range");
        }
        int yearCode = year % 100;

        StringBuilder peselStr = new StringBuilder();
        peselStr.append(String.format("%02d", yearCode));
        peselStr.append(String.format("%02d", monthCode));
        peselStr.append(String.format("%02d", day));

        int i = random.nextInt(10000);
        peselStr.append(String.format("%04d", i));

        peselStr.append(Pesel.Validator.calculateControl(peselStr.toString()));
        return new Pesel(peselStr.toString());
    }
}
