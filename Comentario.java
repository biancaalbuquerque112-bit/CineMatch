/** Comentário ou resposta de um usuário sobre um filme. */
public class Comentario {
    private int id, filmeId, respostaA, curtidas, autorId;
    private String tituloFilme, autor, texto;
    public Comentario() {}
    public Comentario(int id,int respostaA,int curtidas,String tituloFilme,String autor,String texto){
        this.id=id;this.respostaA=respostaA;this.curtidas=curtidas;
        this.tituloFilme=tituloFilme;this.autor=autor;this.texto=texto;
    }
    public int getId(){return id;} public void setId(int v){id=v;}
    public int getFilmeId(){return filmeId;} public void setFilmeId(int v){filmeId=v;}
    public int getRespostaA(){return respostaA;} public void setRespostaA(int v){respostaA=v;}
    public int getCurtidas(){return curtidas;} public void setCurtidas(int v){curtidas=v;}
    public int getAutorId(){return autorId;} public void setAutorId(int v){autorId=v;}
    public String getTituloFilme(){return tituloFilme;} public void setTituloFilme(String v){tituloFilme=v;}
    public String getAutor(){return autor;} public void setAutor(String v){autor=v;}
    public String getTexto(){return texto;} public void setTexto(String v){texto=v;}
}
