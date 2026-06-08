package com.softnexist.dao;

import com.softnexist.model.Contact;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAOImpl implements ContactDAO {
    private final DataSource dataSource;

    // Injecting the JNDI DataSource pool via the constructor
    public ContactDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addContact(Contact contact) throws SQLException {
        String sql = "INSERT INTO contacts (name, email, phone) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getEmail());
            stmt.setString(3, contact.getPhone());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Contact> getAllContacts() throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT id, name, email, phone, created_at FROM contacts ORDER BY name ASC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getTimestamp("created_at")
                ));
            }
        }
        return contacts;
    }

    @Override
    public List<Contact> searchContacts(String keyword) throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        // Implementing Case-insensitive partial match queries using SQL LOWER
        String sql = "SELECT id, name, email, phone, created_at FROM contacts " +
                "WHERE LOWER(name) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?) ORDER BY name ASC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contacts.add(new Contact(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        }
        return contacts;
    }
}