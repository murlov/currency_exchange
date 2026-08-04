package ru.murlov.util;

import jakarta.servlet.http.HttpServletRequest;
import ru.murlov.exception.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class FormatUtil {

    private FormatUtil() {}

    public static String getRequiredNormalizedStringParameter(HttpServletRequest request, String parameterName) {
        String text = request.getParameter(parameterName);
        if (text == null || text.isBlank()) {
            throw new ValidationException("Parameter '" + parameterName + "' is required");
        }

        return text.strip();
    }

    public static BigDecimal getRequiredNormalizedBigDecimalParameter(HttpServletRequest request, String parameterName) {
        String text = request.getParameter(parameterName);
        if (text == null || text.isBlank()) {
            throw new ValidationException("Parameter '" + parameterName + "' is required");
        }

        String capitalizedParameterName = parameterName.substring(0,1).toUpperCase() + parameterName.substring(1);
        BigDecimal result;
        try {
            result = new BigDecimal(text.strip()).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new ValidationException(capitalizedParameterName + " must be a decimal number.");
        }

        return result;
    }
}
