public class Comentario {
    private int id;
    private int filmeId;
    private int respostaA;
    private int curtidas;
    private String tituloFilme;
    private String autor;
    private String texto;

    public Comentario() {}

    public Comentario(int id, int respostaA, int curtidas, String tituloFilme, String autor, String texto) {
        this.id = id;
        this.respostaA = respostaA;
        this.curtidas = curtidas;
        this.tituloFilme = tituloFilme;
        this.autor = autor;
        this.texto = texto;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFilmeId() { return filmeId; }
    public void setFilmeId(int filmeId) { this.filmeId = filmeId; }

    public int getRespostaA() { return respostaA; }
    public void setRespostaA(int respostaA) { this.respostaA = respostaA; }

    public int getCurtidas() { return curtidas; }
    public void setCurtidas(int curtidas) { this.curtidas = curtidas; }

    public String getTituloFilme() { return tituloFilme; }
    public void setTituloFilme(String tituloFilme) { this.tituloFilme = tituloFilme; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}