public class Filme implements Comparable<Filme> {

    private int id;
    private String titulo;
    private String genero;
    private int ano;
    private String capa;
    private String diretor;

    private int curtidas;
    private int rejeicoes;
    private int nota;

    // Construtor completo com ID
    public Filme(int id, String titulo, String genero, int ano, String capa, String diretor) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.ano = ano;
        this.capa = capa;
        this.diretor = diretor;
        this.curtidas = 0;
        this.rejeicoes = 0;
        this.nota = 0;
    }

    // Construtor com o nome do diretor (mantido)
    public Filme(String titulo, String genero, int ano, String capa, String diretor) {
        this(0, titulo, genero, ano, capa, diretor);
    }

    // Mantém compatibilidade com o construtor antigo
    public Filme(String titulo, String genero, int ano, String capa) {
        this(titulo, genero, ano, capa, "Não informado");
    }

    public Filme() {}

    // Getters e Setters para o banco de dados
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDiretor() {
        return diretor;
    }

    public int getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(int curtidas) {
        this.curtidas = curtidas;
    }

    public int getRejeicoes() {
        return rejeicoes;
    }

    public void setRejeicoes(int rejeicoes) {
        this.rejeicoes = rejeicoes;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    // Curtidas
    public void curtir() {
        curtidas++;
    }

    // Rejeições
    public void rejeitar() {
        rejeicoes++;
    }

    // Avaliação de 1 a 5 estrelas
    public void avaliar(int nota) {
        if (nota >= 1 && nota <= 5) {
            this.nota = nota;
        }
    }

    @Override
    public String toString() {
        return titulo + " (" + ano + ") | Gênero: " + genero
                + " | Direção: " + diretor
                + " | Nota: " + nota;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Filme outro = (Filme) obj;

        return ano == outro.ano
                && titulo.equalsIgnoreCase(outro.titulo);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(
                titulo.toLowerCase(), ano
        );
    }

    @Override
    public int compareTo(Filme outroFilme) {
        return this.titulo.compareToIgnoreCase(
                outroFilme.getTitulo()
        );
    }
}