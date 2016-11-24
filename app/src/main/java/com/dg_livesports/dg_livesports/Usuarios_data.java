package com.dg_livesports.dg_livesports;

/**
 * Created by DARLIN on 01/11/2016.
 */
public class Usuarios_data {
    private String user, password, email;

    public Usuarios_data(String user, String password, String email) {

        this.user = user;
        this.password = password;
        this.email = email;
    }

    public Usuarios_data() {

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
