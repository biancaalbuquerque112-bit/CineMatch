public class Usuario {

    private String nome;
    private String email;
    private String senha;

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public boolean verificarSenha(String senhaInformada) {
        return senha != null && senha.equals(senhaInformada);
    }

    // --- METODOS DE POO ---

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
        return java.util.Objects.hash(email != null ? email.toLowerCase() : null);
    }
}