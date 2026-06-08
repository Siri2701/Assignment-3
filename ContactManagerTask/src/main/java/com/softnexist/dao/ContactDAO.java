package com.softnexist.dao;

import com.softnexist.model.Contact;
import java.sql.SQLException;
import java.util.List;

public interface ContactDAO {
    void addContact(Contact contact) throws SQLException;
    List<Contact> getAllContacts() throws SQLException;
    List<Contact> searchContacts(String keyword) throws SQLException;
}