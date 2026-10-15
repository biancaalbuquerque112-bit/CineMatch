
public class Filme {

    private String titulo;
    private String genero;
    private int ano;
    private String capa;
    private int curtidas;
    private int rejeicoes;
    private int nota;

    public Filme(String titulo, String genero,
                 int ano, String capa) {

        this.titulo = titulo;
        this.genero = genero;
        this.ano = ano;
        this.capa = capa;
        this.curtidas = 0;
        this.rejeicoes = 0;
        this.nota = 0;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public int getAno() {
        return ano;
    }

    public String getCapa() {
        return capa;
    }

    public int getCurtidas() {
        return curtidas;
    }

    public int getRejeicoes() {
        return rejeicoes;
    }

    public int getNota() {
        return nota;
    }

    public void curtir() {
        curtidas++;
    }

    public void rejeitar() {
        rejeicoes++;
    }

    public void avaliar(int nota) {
        if (nota >= 1 && nota <= 5) {
            this.nota = nota;
        }
    }
}
