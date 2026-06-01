package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Map;

@WebServlet("/*")
public class NotFoundServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(404);
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> error = Map.of(
                "status", 404,
                "error", "Not found",
                "message", "Invalid path"
        );

        mapper.writeValue(response.getWriter(), error);
    }
}