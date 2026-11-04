import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    public boolean cadastrar(Usuario u) {
        if(u==null || u.getNome()==null || u.getNome().isBlank() ||
                u.getEmail()==null || !u.getEmail().contains("@") ||
                u.getSenha()==null || u.getSenha().length()<6 || !u.maiorDeIdade())return false;
        String sql="INSERT INTO usuario(nome,email,senha,idade,bio,intencao,nascimento) VALUES (?,?,?,?,?,?,?)";
        try(Connection c=ConexaoBanco.getConexao();
            PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            p.setString(1,u.getNome().trim()); p.setString(2,u.getEmail().trim().toLowerCase());
            p.setString(3,SenhaSegura.criar(u.getSenha()));
            p.setInt(4,u.getIdade());
            p.setString(5,u.getBio()); p.setString(6,u.getIntencao()==null?"Amizades":u.getIntencao());
            p.setDate(7,Date.valueOf(u.getNascimento()));
            if(p.executeUpdate()!=1)return false;
            try(ResultSet rs=p.getGeneratedKeys()){if(rs.next())u.setId(rs.getInt(1));}
            return true;
        }catch(SQLException e){System.err.println("Cadastro: "+e.getMessage());return false;}
    }
    public Usuario autenticar(String email,String senha) {
        if(email==null||senha==null)return null;
        String sql="SELECT * FROM usuario WHERE LOWER(email)=LOWER(?)";
        try(Connection c=ConexaoBanco.getConexao();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,email.trim());
            try(ResultSet r=p.executeQuery()){
                if(!r.next())return null;
                String antiga=r.getString("senha");
                if(!SenhaSegura.verificar(senha,antiga))return null;
                Usuario u=ler(r);
                if(!antiga.startsWith("pbkdf2$")){
                    try(PreparedStatement atual=c.prepareStatement("UPDATE usuario SET senha=? WHERE id=?")){
                        atual.setString(1,SenhaSegura.criar(senha));atual.setInt(2,u.getId());atual.executeUpdate();
                    }
                }
                return u;
            }
        }catch(SQLException e){System.err.println("Login: "+e.getMessage());return null;}
    }
    public boolean atualizarPerfil(Usuario u) {
        if(u==null||u.getId()<=0||!u.maiorDeIdade())return false;
        String sql="UPDATE usuario SET nome=?, idade=?, nascimento=?, bio=?, intencao=?, " +
                "identidade_genero=?, orientacao=?, interesse=?, mostrar_informacoes=?, " +
                "permitir_sugestoes=?, generos_favoritos=?, foto=? WHERE id=?";
        try(Connection c=ConexaoBanco.getConexao();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,u.getNome());p.setInt(2,u.getIdade());
            p.setDate(3,Date.valueOf(u.getNascimento()));p.setString(4,u.getBio());
            p.setString(5,u.getIntencao());p.setString(6,u.getIdentidadeGenero());
            p.setString(7,u.getOrientacao());p.setString(8,u.getInteresse());
            p.setBoolean(9,u.isMostrarInformacoes());p.setBoolean(10,u.isPermitirSugestoes());
            p.setString(11,u.getGenerosFavoritos());p.setString(12,u.getFoto());
            p.setInt(13,u.getId());
            return p.executeUpdate()==1;
        }catch(SQLException e){System.err.println("Perfil: "+e.getMessage());return false;}
    }
    public boolean atualizarPerfil(String email,String bio,String intencao) {
        try(Connection c=ConexaoBanco.getConexao();
            PreparedStatement p=c.prepareStatement("UPDATE usuario SET bio=?, intencao=? WHERE email=?")){
            p.setString(1,bio);p.setString(2,intencao);p.setString(3,email);return p.executeUpdate()==1;
        }catch(SQLException e){return false;}
    }
    public Usuario buscarPorId(int id) {
        try(Connection c=ConexaoBanco.getConexao();PreparedStatement p=c.prepareStatement("SELECT * FROM usuario WHERE id=?")){
            p.setInt(1,id);
            try(ResultSet r=p.executeQuery()){return r.next()?ler(r):null;}
        }catch(SQLException e){return null;}
    }
    public List<Usuario> listarTodos() {
        List<Usuario> lista=new ArrayList<>();
        try(Connection c=ConexaoBanco.getConexao();Statement s=c.createStatement();
            ResultSet r=s.executeQuery("SELECT * FROM usuario ORDER BY nome")){
            while(r.next())lista.add(ler(r));
        }catch(SQLException e){System.err.println("Usuários: "+e.getMessage());}
        return lista;
    }
    private Usuario ler(ResultSet r)throws SQLException {
        Usuario u=new Usuario();
        u.setId(r.getInt("id"));u.setNome(r.getString("nome"));
        u.setEmail(r.getString("email")); // Senha NÃO é divulgada em listagens.
        u.setBio(r.getString("bio"));u.setIntencao(r.getString("intencao"));
        Date n=r.getDate("nascimento");if(n!=null)u.setNascimento(n.toLocalDate());
        u.setIdentidadeGenero(valor(r.getString("identidade_genero"),"Prefiro não informar"));
        u.setOrientacao(valor(r.getString("orientacao"),"Prefiro não informar"));
        u.setInteresse(valor(r.getString("interesse"),"Sem preferência"));
        u.setMostrarInformacoes(r.getBoolean("mostrar_informacoes"));
        Object sugestoes=r.getObject("permitir_sugestoes");
        u.setPermitirSugestoes(sugestoes==null||r.getBoolean("permitir_sugestoes"));
        u.setGenerosFavoritos(valor(r.getString("generos_favoritos"),""));
        u.setFoto(valor(r.getString("foto"),""));
        return u;
    }
    private String valor(String v,String padrao){return v==null?padrao:v;}
}
