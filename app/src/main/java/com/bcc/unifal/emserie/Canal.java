package com.bcc.unifal.emserie;


public class Canal {
    public static final String COD = "cod";
    public static final String NOME = "nome";
    public static final String TABLE = "Canal";

    private String cod, nome;

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Canal(String cod, String nome) {

        this.cod = cod;
        this.nome = nome;
    }

    public Canal() {
    }
}
