package ru.murlov.util;

import jakarta.servlet.http.HttpServletRequest;
import ru.murlov.exception.ValidationException;

public final class FormatUtil {

    private FormatUtil() {}

    public static String getRequiredNormalizedStringParameter(HttpServletRequest request, String parameterName) {
        String value = request.getParameter(parameterName);
        if (value == null || value.isBlank()) {
            throw new ValidationException("Parameter '" + parameterName + "' is required");
        }

        return value.strip();
    }

    public static float getRequiredNormalizedFloatParameter(HttpServletRequest request, String parameterName) {
        String value = request.getParameter(parameterName);
        if (value == null || value.isBlank()) {
            throw new ValidationException("Parameter '" + parameterName + "' is required");
        }

        String capitalizedParameterName = parameterName.substring(0,1).toUpperCase() + parameterName.substring(1);

        try {
            return Float.parseFloat(value.strip());
        } catch (NumberFormatException e) {
            throw new ValidationException(capitalizedParameterName + " must be a decimal number.");
        }
    }
}
