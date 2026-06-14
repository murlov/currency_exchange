package ru.murlov.util;

import jakarta.servlet.http.HttpServletRequest;
import ru.murlov.exception.ValidationException;

public final class FormatUtil {

    private FormatUtil() {}

    public static String getRequiredNormalizedParameter(HttpServletRequest request, String parameterName) {
        String value = request.getParameter(parameterName);
        if (value == null || value.isBlank()) {
            throw new ValidationException("Parameter '" + parameterName + "' is required");
        }

        if (parameterName.toLowerCase().contains("code")) {
            value = value.toUpperCase();
        }

        return value.strip();
    }
}
