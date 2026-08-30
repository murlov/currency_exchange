package ru.murlov.util;

import jakarta.servlet.http.HttpServletRequest;
import ru.murlov.exception.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class FormatUtil {

    public static final int FIRST_LETTER_START = 0;
    public static final int FIRST_LETTER_END = 1;

    private FormatUtil() {}

    public static String getRequiredNormalizedStringParameter(HttpServletRequest request, String parameterName) {
        String text = request.getParameter(parameterName);
        if (text == null || text.isBlank()) {
            throw new ValidationException("Parameter '" + parameterName + "' is required");
        }

        return text.strip();
    }

    public static BigDecimal getRequiredNormalizedBigDecimalParameter(HttpServletRequest request,
                                                                      String parameterName,
                                                                      int numberOfDecimals) {
        String text = request.getParameter(parameterName);
        if (text == null || text.isBlank()) {
            throw new ValidationException("Parameter '" + parameterName + "' is required");
        }

        String capitalizedParameterName = parameterName.substring(FIRST_LETTER_START,FIRST_LETTER_END)
                .toUpperCase() + parameterName.substring(FIRST_LETTER_END);

        BigDecimal result;
        try {
            result = new BigDecimal(text.strip()).setScale(numberOfDecimals, RoundingMode.DOWN);
        } catch (NumberFormatException e) {
            throw new ValidationException(capitalizedParameterName + " must be a decimal number.");
        }

        return result;
    }
}
