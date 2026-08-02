package ru.murlov.util;

import jakarta.servlet.http.HttpServletRequest;
import ru.murlov.exception.ValidationException;

public final class FormatUtil {

    private FormatUtil() {}

    public static String getRequiredNormalizedStringParameter(HttpServletRequest request, String parameterName) {
        String text = request.getParameter(parameterName);
        if (text == null || text.isBlank()) {
            throw new ValidationException("Parameter '" + parameterName + "' is required");
        }

        return text.strip();
    }

    public static float getRequiredNormalizedFloatParameter(HttpServletRequest request, String parameterName) {
        String text = request.getParameter(parameterName);
        if (text == null || text.isBlank()) {
            throw new ValidationException("Parameter '" + parameterName + "' is required");
        }

        String capitalizedParameterName = parameterName.substring(0,1).toUpperCase() + parameterName.substring(1);
        float value = Float.parseFloat(text.strip());
        float result = (float) Math.round(value * 100) / 100f;

        try {
            return result;
        } catch (NumberFormatException e) {
            throw new ValidationException(capitalizedParameterName + " must be a decimal number.");
        }
    }
}
