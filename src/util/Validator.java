package util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Validator {

    public static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    public static String requireNationalId(String value) {
        String id = requireText(value, "National ID");
        if (!id.matches("\\d{7,8}")) {
            throw new IllegalArgumentException("National ID must be 7 or 8 digits.");
        }
        return id;
    }

    public static String requirePhone(String value) {
        String phone = requireText(value, "Phone number");
        if (!phone.matches("^(\\+254|0)[17]\\d{8}$")) {
            throw new IllegalArgumentException("Phone must look like 0712345678 or +254712345678.");
        }
        return phone;
    }

    public static String optionalEmail(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String email = value.trim();
        if (!email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new IllegalArgumentException("Email format is not valid.");
        }
        return email;
    }

    public static LocalDate requireDate(String value, String fieldName) {
        String text = requireText(value, fieldName);
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(fieldName + " must be in the format yyyy-MM-dd (e.g. 2026-11-17).");
        }
    }

    public static double requirePositiveAmount(String value, String fieldName) {
        String text = requireText(value, fieldName);
        try {
            double amount = Double.parseDouble(text);
            if (amount <= 0) {
                throw new IllegalArgumentException(fieldName + " must be greater than 0.");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a number (e.g. 2000 or 1500.50).");
        }
    }
}
