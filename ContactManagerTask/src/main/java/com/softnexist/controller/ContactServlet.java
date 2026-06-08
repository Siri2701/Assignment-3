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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/contacts", "/contacts/add"})
public class ContactServlet extends HttpServlet {
    private ContactDAO contactDAO;

    @Override
    public void init() throws ServletException {
        // Initialize the DAO layer using our connection pooling utility
        this.contactDAO = new ContactDAOImpl(DBUtil.getDataSource());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/contacts/add".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/views/contact-form.jsp").forward(request, response);
        } else {
            try {
                String searchTerm = request.getParameter("search");
                List<Contact> contacts;

                // If a search query is present, pull filtered results; otherwise, pull everything
                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                    contacts = contactDAO.searchContacts(searchTerm.trim());
                } else {
                    contacts = contactDAO.getAllContacts();
                }

                // Attach to request scope for JSP viewing
                request.setAttribute("contacts", contacts);
                request.getRequestDispatcher("/WEB-INF/views/contact-list.jsp").forward(request, response);

            } catch (SQLException e) {
                // SQLException error logging and recovery
                log("Database query failure inside ContactServlet", e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                request.setAttribute("error", "Service Unavailable: Could not load the contact registry dashboard.");
                request.getRequestDispatcher("/WEB-INF/views/contact-form.jsp").forward(request, response);
            }
        }
    }
}