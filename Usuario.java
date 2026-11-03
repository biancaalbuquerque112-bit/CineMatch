import java.util.Objects;

public class Usuario {

    private String nome;
    private String email;
    private String senha;
    private int idade;
    private String bio;
    private String intencao;

    // 1. Construtor padrão sem argumentos (necessário para Usuario u = new Usuario(); no DAO)
    public Usuario() {
    }

    // 2. Construtor com 3 parâmetros (mantido do seu código original)
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // 3. Construtor completo com todos os campos
    public Usuario(String nome, String email, String senha, int idade, String bio, String intencao) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.idade = idade;
        this.bio = bio;
        this.intencao = intencao;
    }

    // --- GETTERS E SETTERS ---

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getIntencao() {
        return intencao;
    }

    public void setIntencao(String intencao) {
        this.intencao = intencao;
    }

    // --- MÉTODOS DE REGRA DE NEGÓCIO E POO ---

    public boolean verificarSenha(String senhaInformada) {
        return senha != null && senha.equals(senhaInformada);
    }

    @Override
    public String toString() {
        return nome + " <" + email + ">";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario outro = (Usuario) obj;
        return email != null && email.equalsIgnoreCase(outro.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email != null ? email.toLowerCase() : null);
    }
}