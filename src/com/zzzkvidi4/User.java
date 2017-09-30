package com.zzzkvidi4;

import java.util.regex.Pattern;

public class User {
    public User(){
        this.id = 0;
        this.name = "";
        this.surname = "";
        this.login = "";
        this.email = "";
    }

    public User(int id, String name, String surname, String login, String email){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
    }

    private int id;
    private String login;
    private String name;
    private String surname;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) throws IllegalArgumentException {
        if (id <= 0) throw new IllegalArgumentException();
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) throws IllegalArgumentException {
        if (login.trim().equals("")) throw new IllegalArgumentException();
        this.login = login.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) throws IllegalArgumentException {
        if (surname.trim().equals("")) throw new IllegalArgumentException();
        this.surname = surname.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws IllegalArgumentException {
        Pattern ptrn = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        if (!ptrn.matcher(email).matches()) throw new IllegalArgumentException();
        this.email = email;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + "\nИмя: " + getName() + "\nФамилия: " + getSurname() + "\nЛогин: " + getLogin() + "\nEmail: " + getEmail();
    }

    public String toCSVFormat() {
        return "" + getId() + "," + getName() + "," + getSurname() + "," + getLogin() + "," + getEmail();
    }

    public static User fromCSVFormat(String csv) throws IllegalArgumentException {
        User user = new User();
        String[] values = csv.split(",");
        if (values.length != 5) throw new IllegalArgumentException();
        user.setId(Integer.parseInt(values[0]));
        user.setName(values[1]);
        user.setSurname(values[2]);
        user.setLogin(values[3]);
        user.setEmail(values[4]);
        return user;
    }
}
