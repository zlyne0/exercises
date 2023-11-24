package promitech.ecc;

import java.time.LocalDate;

public class Pesel {

    private final String value;

    Pesel(String value) {
        this.value = value;
    }

    public boolean isValid() {
        return Validator.validate(value) == ValidationResult.VALID;
    }

    public Sex extractSex() {
        if (!isValid()) {
            throw new IllegalStateException("pesel '" + value + "' is invalid");
        }
        if (Integer.parseInt(String.valueOf(value.charAt(9))) % 2 == 0) {
            return Sex.FEMALE;
        } else {
            return Sex.MALE;
        }
    }

    public LocalDate extractBirthDate() {
        if (!isValid()) {
            throw new IllegalStateException("pesel '" + value + "' is invalid");
        }
        int year = Integer.parseInt(value.substring(0, 2));
        int monthCode = Integer.parseInt(value.substring(2, 4));
        int day = Integer.parseInt(value.substring(4, 6));
        int century = 1900;
        int month = monthCode;
        if (monthCode > 80) {
            century = 1800;
            month = monthCode - 80;
        } else if (monthCode > 60) {
            century = 2200;
            month = monthCode - 60;
        } else if (monthCode > 40) {
            century = 2100;
            month = monthCode - 40;
        } else if (monthCode > 20) {
            century = 2000;
            month = monthCode - 20;
        }
        return LocalDate.of(century + year, month, day);
    }

    public String getValue() {
        return value;
    }

    public enum Sex {
        FEMALE, MALE
    }

    public enum ValidationResult {
        VALID, INVALID
    }

    public static class Validator {

        public static final int PESEL_LENGTH = 11;
        public static int[] WEIGHTS = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};

        public static ValidationResult validate(String pesel) {
            if (pesel.length() != PESEL_LENGTH) {
                return ValidationResult.INVALID;
            }
            if (!pesel.matches("\\d+")) {
                return ValidationResult.INVALID;
            }
            int control = calculateControl(pesel);
            int lastDigit = Integer.parseInt(String.valueOf(pesel.charAt(10)));
            if (lastDigit != control) {
                return ValidationResult.INVALID;
            }
            return ValidationResult.VALID;
        }

        public static int calculateControl(String str) {
            int sum = 0;
            for (int i = 0; i < WEIGHTS.length; i++) {
                sum += WEIGHTS[i] * Integer.parseInt(String.valueOf(str.charAt(i)));
            }
            int control = 10 - (sum % 10);
            if (control == 10) {
                return 0;
            }
            return control;
        }
    }

}
