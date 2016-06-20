package com.bcc.unifal.emserie;

public class User {

    public static final String COD = "cod";
    public static final String LOGIN = "login";
    public static final String SENHA = "senha";
    public static final String TABLE = "Usuario";

    private String cod, login;

    public User(String cod, String login) {
        this.cod = cod;
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCOD() {
        return COD;
    }

    public void setCod (String cod){
        this.cod = cod;
    }

    public User(){

    }
}