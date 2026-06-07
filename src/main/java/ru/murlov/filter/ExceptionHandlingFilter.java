package ru.murlov.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.murlov.exception.DuplicateCurrencyCodeException;
import ru.murlov.exception.MethodNotAllowedException;
import ru.murlov.exception.NotFoundException;
import ru.murlov.exception.ValidationException;

import java.io.IOException;
import java.util.Map;

@WebFilter("/*")
public class ExceptionHandlingFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (NotFoundException e) {
            sendError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DuplicateCurrencyCodeException e) {
            sendError(response, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (ValidationException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (MethodNotAllowedException e) {
            sendError(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, e.getMessage());
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> error = Map.of(
                "message", message
        );
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), error);
    }
}
