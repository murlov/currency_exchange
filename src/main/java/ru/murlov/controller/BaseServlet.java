package ru.murlov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.murlov.exception.MethodNotAllowedException;

import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        throw new MethodNotAllowedException("GET method is not supported for this endpoint");
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) {
        throw new MethodNotAllowedException("HEAD method is not supported for this endpoint");
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        throw new MethodNotAllowedException("PATCH method is not supported for this endpoint");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        throw new MethodNotAllowedException("POST method is not supported for this endpoint");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        throw new MethodNotAllowedException("PUT method is not supported for this endpoint");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        throw new MethodNotAllowedException("DELETE method is not supported for this endpoint");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        throw new MethodNotAllowedException("OPTIONS method is not supported for this endpoint");
    }

    protected void sendResponse(HttpServletResponse response, int status, Object value) throws IOException {
        response.setStatus(status);
        objectMapper.writeValue(response.getWriter(), value);
    }
}
