package com.softnexist.controller;

import com.softnexist.dao.ContactDAO;
import com.softnexist.dao.ContactDAOImpl;
import com.softnexist.model.Contact;
import com.softnexist.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/contacts/post")
public class AddContactServlet extends HttpServlet {
    private ContactDAO contactDAO;

    @Override
    public void init() throws ServletException {
        this.contactDAO = new ContactDAOImpl(DBUtil.getDataSource());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        name = (name != null) ? name.trim() : "";
        email = (email != null) ? email.trim() : "";
        phone = (phone != null) ? phone.trim() : "";

        Map<String, String> errors = new HashMap<>();

        // Business Validation Layers
        if (name.isEmpty() || name.length() < 2 || name.length() > 100) {
            errors.put("name", "Please enter valid name");
        }

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (email.isEmpty() || !email.matches(emailRegex)) {
            errors.put("email", "Invalid email address");
        }

        if (!phone.isEmpty() && !phone.matches("^\\d{10}$")) {
            errors.put("phone", "Use 10-digit format");
        }

        if (!errors.isEmpty()) {
            sendBackToForm(request, response, errors, name, email, phone);
            return;
        }

        try {
            // Package the bean model representation
            Contact newContact = new Contact(name, email, phone);

            // Execute Database Persist
            contactDAO.addContact(newContact);

            // Trigger temporary success notification toast
            HttpSession session = request.getSession();
            session.setAttribute("successToast", "Contact added successfully!");

            // 302 Redirect to neutralize structural form refreshes
            response.sendRedirect(request.getContextPath() + "/contacts");

        } catch (SQLException e) {
            log("Database writing exception caught", e);

            // Catching Duplicate Entry Handling natively via PostgreSQL unique violation code 23505
            if ("23505".equals(e.getSQLState())) {
                errors.put("email", "Email already exists");
                sendBackToForm(request, response, errors, name, email, phone);
            } else {
                // Graceful "Service Unavailable" fallback layout
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                request.setAttribute("error", "An internal data system processing failure occurred.");
                request.getRequestDispatcher("/WEB-INF/views/contact-form.jsp").forward(request, response);
            }
        }
    }

    private void sendBackToForm(HttpServletRequest request, HttpServletResponse response,
                                Map<String, String> errors, String name, String email, String phone)
            throws ServletException, IOException {
        request.setAttribute("errors", errors);
        request.setAttribute("presetName", name);
        request.setAttribute("presetEmail", email);
        request.setAttribute("presetPhone", phone);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        request.getRequestDispatcher("/WEB-INF/views/contact-form.jsp").forward(request, response);
    }
}