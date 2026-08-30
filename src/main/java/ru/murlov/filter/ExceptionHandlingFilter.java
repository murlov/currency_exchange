package ru.murlov.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.murlov.exception.*;

import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class ExceptionHandlingFilter extends HttpFilter {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingFilter.class);
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();

        this.objectMapper =
                (ObjectMapper) getServletContext()
                        .getAttribute("objectMapper");

        if (objectMapper == null) {
            throw new IllegalStateException(
                    "ObjectMapper is not initialized"
            );
        }

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (NotFoundException e) {
            sendError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DuplicateCurrencyCodeException | DuplicateExchangeRateException e) {
            sendError(response, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (ValidationException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (MethodNotAllowedException e) {
            sendError(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        Map<String, String> error = Map.of(
                "message", message
        );
        response.setStatus(status);
        objectMapper.writeValue(response.getWriter(), error);
    }
}
