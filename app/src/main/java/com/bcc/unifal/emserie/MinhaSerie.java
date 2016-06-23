package com.bcc.unifal.emserie;


public class MinhaSerie {
    public static final String COD_USUARIO = "cod_usuario";
    public static final String COD_SERIE = "cod_serie";
    public static final String TABLE = "MinhaSerie";

    private String cod_usuario, cod_serie;

    public MinhaSerie(String cod_usuario, String cod_serie) {
        this.cod_usuario = cod_usuario;
        this.cod_serie = cod_serie;
    }

    public String getCod_usuario() {
        return cod_usuario;
    }

    public void setCod_usuario(String cod_usuario) {
        this.cod_usuario = cod_usuario;
    }

    public String getCod_serie() {
        return cod_serie;
    }

    public void setCod_serie(String cod_serie) {
        this.cod_serie = cod_serie;
    }

    public MinhaSerie(){

    }
}
