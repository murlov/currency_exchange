package ru.murlov.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.murlov.exception.NotFoundException;

import java.io.IOException;

@WebServlet("/*")
public class NotFoundServlet extends BaseServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        throw new NotFoundException("Invalid path");
    }
}