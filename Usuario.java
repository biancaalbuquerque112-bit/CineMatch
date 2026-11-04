import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Usuario {
    private int id;
    private String nome, email, senha, bio, intencao;
    private LocalDate nascimento;
    private String identidadeGenero = "Prefiro não informar";
    private String orientacao = "Prefiro não informar";
    private String interesse = "Sem preferência";
    private String generosFavoritos = "";
    private String foto = "";
    private boolean mostrarInformacoes, permitirSugestoes = true;

    public Usuario() {}
    public Usuario(String nome, String email, String senha) {
        this.nome=nome; this.email=email; this.senha=senha;
    }
    public Usuario(String nome, String email, String senha, int idade, String bio, String intencao) {
        this(nome,email,senha); this.bio=bio; this.intencao=intencao;
        // Idade antiga não permite descobrir a data real de nascimento.
    }
    public int getId(){return id;}
    public void setId(int v){id=v;}
    public String getNome(){return nome;}
    public void setNome(String v){nome=v;}
    public String getEmail(){return email;}
    public void setEmail(String v){email=v;}
    public String getSenha(){return senha;}
    public void setSenha(String v){senha=v;}
    public String getBio(){return bio;}
    public void setBio(String v){bio=v;}
    public String getIntencao(){return intencao;}
    public void setIntencao(String v){intencao=v;}
    public LocalDate getNascimento(){return nascimento;}
    public void setNascimento(LocalDate v){nascimento=v;}
    public int getIdade(){return nascimento==null?0:Period.between(nascimento,LocalDate.now()).getYears();}
    /** Mantido somente para compatibilidade com código anterior. */
    public void setIdade(int idade) { /* Não inferir data de nascimento a partir da idade. */ }
    public boolean maiorDeIdade() {
        return nascimento!=null && !nascimento.isAfter(LocalDate.now().minusYears(18));
    }
    public String getIdentidadeGenero(){return identidadeGenero;}
    public void setIdentidadeGenero(String v){identidadeGenero=v;}
    public String getOrientacao(){return orientacao;}
    public void setOrientacao(String v){orientacao=v;}
    public String getInteresse(){return interesse;}
    public void setInteresse(String v){interesse=v;}
    public String getGenerosFavoritos(){return generosFavoritos;}
    public void setGenerosFavoritos(String v){generosFavoritos=v;}
    public String getFoto(){return foto;}
    public void setFoto(String v){foto=v;}
    public boolean isMostrarInformacoes(){return mostrarInformacoes;}
    public void setMostrarInformacoes(boolean v){mostrarInformacoes=v;}
    public boolean isPermitirSugestoes(){return permitirSugestoes;}
    public void setPermitirSugestoes(boolean v){permitirSugestoes=v;}
    public boolean verificarSenha(String informada){return senha!=null && senha.equals(informada);}
    @Override public String toString(){return nome+" <"+email+">";}
    @Override public boolean equals(Object obj){
        if(this==obj)return true;
        if(!(obj instanceof Usuario))return false;
        Usuario outro=(Usuario)obj;
        return email!=null&&email.equalsIgnoreCase(outro.email);
    }
    @Override public int hashCode(){return Objects.hash(email==null?null:email.toLowerCase());}
}
