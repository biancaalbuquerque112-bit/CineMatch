import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmeDAO {
    public void sincronizarCatalogo() {
        List<Filme> oficiais=new Catalogo().getFilmes();
        String busca="SELECT id FROM filme WHERE LOWER(titulo)=LOWER(?) ORDER BY id";
        String insere="INSERT INTO filme(titulo,genero,ano,capa,diretor,curtidas,nota) VALUES(?,?,?,?,?,0,0)";
        String atualiza="UPDATE filme SET genero=?,ano=?,capa=?,diretor=? WHERE id=?";
        try(Connection c=ConexaoBanco.getConexao();
            PreparedStatement b=c.prepareStatement(busca);
            PreparedStatement i=c.prepareStatement(insere);
            PreparedStatement u=c.prepareStatement(atualiza);
            PreparedStatement ajustarComentarios=c.prepareStatement(
                 "UPDATE comentario SET filme_id=? WHERE LOWER(titulo_filme)=LOWER(?) " +
                "AND (filme_id IS NULL OR filme_id=0 OR filme_id<>?)")){
            for(Filme f:oficiais){
                b.setString(1,f.getTitulo());
                int id=0;
                try(ResultSet r=b.executeQuery()){if(r.next())id=r.getInt(1);}
                if(id==0) {
                    i.setString(1,f.getTitulo());i.setString(2,f.getGenero());
                    i.setInt(3,f.getAno());i.setString(4,f.getCapa());
                    i.setString(5,f.getDiretor());i.executeUpdate();
                    try(ResultSet r=b.executeQuery()){if(r.next())id=r.getInt(1);}
                } else {
                    u.setString(1,f.getGenero());u.setInt(2,f.getAno());
                    u.setString(3,f.getCapa());u.setString(4,f.getDiretor());u.setInt(5,id);
                    u.executeUpdate();
                }
                ajustarComentarios.setInt(1,id);
                ajustarComentarios.setString(2,f.getTitulo());
                ajustarComentarios.setInt(3,id);
                ajustarComentarios.executeUpdate();
            }
        }catch(SQLException e){throw new IllegalStateException("Catálogo não sincronizado: "+e.getMessage(),e);}
    }
    public void popularBancoSeVazio(){sincronizarCatalogo();}
    public List<Filme> listarTodos() {
        List<Filme> lista=new ArrayList<>();
        Catalogo oficiais = new Catalogo();
        java.util.Set<String> incluidos = new java.util.HashSet<>();
        try(Connection c=ConexaoBanco.getConexao();
            Statement s=c.createStatement();
            ResultSet r=s.executeQuery("SELECT * FROM filme ORDER BY titulo,id")){
            while(r.next()) {
                String titulo = r.getString("titulo");
                if(oficiais.buscarPorTitulo(titulo)!=null &&
                        incluidos.add(titulo.toLowerCase(java.util.Locale.ROOT)))
                    lista.add(ler(r));
            }
        }catch(SQLException e){System.err.println("Filmes: "+e.getMessage());}
        return lista;
    }
    public List<Filme> buscarPorGenero(String busca){
        List<Filme> lista=new ArrayList<>();
        Catalogo oficiais = new Catalogo();
        java.util.Set<String> incluidos = new java.util.HashSet<>();
        // Pesquisa TÍTULOS e gêneros para a barra do catálogo.
        try(Connection c=ConexaoBanco.getConexao();
            PreparedStatement p=c.prepareStatement(
                "SELECT * FROM filme WHERE LOWER(genero) LIKE LOWER(?) OR LOWER(titulo) LIKE LOWER(?) ORDER BY titulo,id")){
            p.setString(1,"%"+busca+"%");p.setString(2,"%"+busca+"%");
            try(ResultSet r=p.executeQuery()){
                while(r.next()){
                    String titulo=r.getString("titulo");
                    if(oficiais.buscarPorTitulo(titulo)!=null &&
                        incluidos.add(titulo.toLowerCase(java.util.Locale.ROOT)))
                        lista.add(ler(r));
                }
            }
        }catch(SQLException e){System.err.println("Pesquisa: "+e.getMessage());}
        return lista;
    }
    public Filme buscarPorTitulo(String titulo){
        try(Connection c=ConexaoBanco.getConexao();
            PreparedStatement p=c.prepareStatement("SELECT * FROM filme WHERE LOWER(titulo)=LOWER(?) ORDER BY id LIMIT 1")){
            p.setString(1,titulo);
            try(ResultSet r=p.executeQuery()){return r.next()?ler(r):null;}
        }catch(SQLException e){return null;}
    }
    public boolean atualizarAvaliacao(Filme f){
        try(Connection c=ConexaoBanco.getConexao();
            PreparedStatement p=c.prepareStatement("UPDATE filme SET nota=? WHERE id=?")){
            p.setInt(1,f.getNota());p.setInt(2,f.getId());
            return p.executeUpdate()>0;
        }catch(SQLException e){return false;}
    }
    public boolean salvar(Filme f){
        try(Connection c=ConexaoBanco.getConexao();
            PreparedStatement p=c.prepareStatement(
                "INSERT INTO filme(titulo,genero,ano,capa,diretor,curtidas,nota) VALUES(?,?,?,?,?,?,?)")){
            p.setString(1,f.getTitulo());p.setString(2,f.getGenero());p.setInt(3,f.getAno());
            p.setString(4,f.getCapa());p.setString(5,f.getDiretor());
            p.setInt(6,f.getCurtidas());p.setInt(7,f.getNota());
            return p.executeUpdate()==1;
        }catch(SQLException e){return false;}
    }
    private Filme ler(ResultSet r)throws SQLException{
        Filme f=new Filme(r.getInt("id"),r.getString("titulo"),r.getString("genero"),
            r.getInt("ano"),r.getString("capa"),r.getString("diretor"));
        f.setCurtidas(r.getInt("curtidas"));f.setNota(r.getInt("nota"));return f;
    }
}
