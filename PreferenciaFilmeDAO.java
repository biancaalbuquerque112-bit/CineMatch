import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Uma curtida e um favorito são registrados por CONTA e por FILME. */
public class PreferenciaFilmeDAO {
    public boolean marcarCurtido(int usuarioId,int filmeId){
        return marcar(usuarioId,filmeId,"curtiu");
    }
    public boolean marcarSalvo(int usuarioId,int filmeId){
        return marcar(usuarioId,filmeId,"salvo");
    }
    private boolean marcar(int uid,int fid,String coluna){
        if(uid<=0||fid<=0)return false;
        try(Connection c=ConexaoBanco.getConexao()){
            c.setAutoCommit(false);
            try(PreparedStatement p=c.prepareStatement(
                "MERGE INTO preferencia_filme(usuario_id,filme_id,curtiu,salvo) KEY(usuario_id,filme_id) " +
                "SELECT ?,?, " +
                "COALESCE((SELECT curtiu FROM preferencia_filme WHERE usuario_id=? AND filme_id=?),FALSE), " +
                "COALESCE((SELECT salvo FROM preferencia_filme WHERE usuario_id=? AND filme_id=?),FALSE)")){
                p.setInt(1,uid);p.setInt(2,fid);p.setInt(3,uid);p.setInt(4,fid);
                p.setInt(5,uid);p.setInt(6,fid);p.executeUpdate();
            }
            try(PreparedStatement p=c.prepareStatement(
                "UPDATE preferencia_filme SET "+coluna+"=TRUE WHERE usuario_id=? AND filme_id=?")){
                p.setInt(1,uid);p.setInt(2,fid);p.executeUpdate();
            }
            if ("curtiu".equals(coluna)) {
                try(PreparedStatement p=c.prepareStatement(
                    "UPDATE filme SET curtidas=(SELECT COUNT(*) FROM preferencia_filme "+
                    "WHERE filme_id=? AND curtiu=TRUE) WHERE id=?")) {
                    p.setInt(1,fid);p.setInt(2,fid);p.executeUpdate();
                }
            }
            c.commit();
            return true;
        }catch(SQLException e){System.err.println("Preferência: "+e.getMessage());return false;}
    }
    public int contarCurtidas(int filmeId) {
        try (Connection c=ConexaoBanco.getConexao();
             PreparedStatement p=c.prepareStatement(
                 "SELECT COUNT(*) FROM preferencia_filme WHERE filme_id=? AND curtiu=TRUE")) {
            p.setInt(1,filmeId);
            try(ResultSet r=p.executeQuery()){return r.next()?r.getInt(1):0;}
        }catch(SQLException e){return 0;}
    }
    public Set<Integer> idsCurtidos(int uid){return ids(uid,"curtiu");}
    public Set<Integer> idsSalvos(int uid){return ids(uid,"salvo");}
    private Set<Integer> ids(int uid,String coluna){
        Set<Integer> ids=new HashSet<>();
        if(uid<=0)return ids;
        try(Connection c=ConexaoBanco.getConexao();
            PreparedStatement p=c.prepareStatement(
                "SELECT filme_id FROM preferencia_filme WHERE usuario_id=? AND "+coluna+"=TRUE")){
            p.setInt(1,uid);
            try(ResultSet r=p.executeQuery()){while(r.next())ids.add(r.getInt(1));}
        }catch(SQLException e){System.err.println(e.getMessage());}
        return ids;
    }
    public List<Filme> listarCurtidos(int uid){return listar(uid,"curtiu");}
    public List<Filme> listarSalvos(int uid){return listar(uid,"salvo");}
    private List<Filme> listar(int uid,String coluna) {
        List<Filme> resultado=new ArrayList<>();
        Set<Integer> ids=ids(uid,coluna);
        for(Filme f:new FilmeDAO().listarTodos())if(ids.contains(f.getId()))resultado.add(f);
        return resultado;
    }
}
