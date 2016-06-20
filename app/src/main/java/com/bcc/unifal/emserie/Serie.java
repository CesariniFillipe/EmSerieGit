package com.bcc.unifal.emserie;


public class Serie {
    public static final String COD = "cod";
    public static final String COD_CANAL = "cod_canal";
    public static final String TITULO= "titulo";
    public static final String ANOLANCAMENTO = "anoLancamento";
    public static final String IMAGEM = "img";
    public static final String TABLE = "Serie";

    private String cod, cod_canal, titulo, anoLancamento, img;

    public Serie(String cod, String cod_canal, String titulo, String anoLancamento, String img) {
        this.cod = cod;
        this.cod_canal = cod_canal;
        this.titulo = titulo;
        this.anoLancamento = anoLancamento;
        this.img = img;
    }

    public String getCod_canal() {
        return cod_canal;
    }

    public void setCod_canal(String cod_canal) {
        this.cod_canal = cod_canal;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(String anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Serie(){
    }
}
