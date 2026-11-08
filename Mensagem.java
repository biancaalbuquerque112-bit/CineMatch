import java.sql.*;
import java.util.*;

public class MatchDAO {
    private final PreferenciaFilmeDAO preferencias=new PreferenciaFilmeDAO();
    public List<AlgoritmoMatch.Resultado> sugerir(int usuarioId,int filmeId) {
        List<AlgoritmoMatch.Resultado> saida=new ArrayList<>();
        Usuario atual=new UsuarioDAO().buscarPorId(usuarioId);
        if(atual==null||!preferencias.idsCurtidos(usuarioId).contains(filmeId))return saida;
        Set<Integer> meusFilmes=preferencias.idsCurtidos(usuarioId);
        for(Usuario pessoa:new UsuarioDAO().listarTodos()){
            if(pessoa.getId()==usuarioId)continue;
            Set<Integer> outros=preferencias.idsCurtidos(pessoa.getId());
            if(!outros.contains(filmeId))continue;
            AlgoritmoMatch.Resultado r=AlgoritmoMatch.calcular(atual,pessoa,meusFilmes,outros);
            if(r!=null)saida.add(r);
        }
        saida.sort((a,b)->Integer.compare(b.percentual,a.percentual));
        return saida;
    }
    public boolean solicitar(int origem,int destino){
        if(origem<=0||destino<=0||origem==destino)return false;
        Usuario a=new UsuarioDAO().buscarPorId(origem);
        Usuario b=new UsuarioDAO().buscarPorId(destino);
        if(a==null||b==null||!a.maiorDeIdade()||!b.maiorDeIdade()||
            !a.isPermitirSugestoes()||!b.isPermitirSugestoes())return false;
        if(!temFilmeComum(origem,destino))return false;
        if(statusEntre(origem,destino)!=null)return false;
        try(Connection c=ConexaoBanco.getConexao();
            PreparedStatement p=c.prepareStatement(
                "INSERT INTO conexao(solicitante_id,destinatario_id,status) VALUES(?,?,'PENDENTE')")){
            p.setInt(1,origem);p.setInt(2,destino);return p.executeUpdate()==1;
        }catch(SQLException e){return false;}
    }
    public boolean temFilmeComum(int a,int b){
        Set<Integer> x=preferencias.idsCurtidos(a);x.retainAll(preferencias.idsCurtidos(b));
        return !x.isEmpty();
    }
    public String statusEntre(int a,int b){
        try(Connection c=ConexaoBanco.getConexao();PreparedStatement p=c.prepareStatement(
            "SELECT status,solicitante_id FROM conexao WHERE (solicitante_id=? AND destinatario_id=?) OR (solicitante_id=? AND destinatario_id=?)")){
            p.setInt(1,a);p.setInt(2,b);p.setInt(3,b);p.setInt(4,a);
            try(ResultSet r=p.executeQuery()){
                if(r.next())return r.getString(1)+ (r.getInt(2)==a?" (enviada)":" (recebida)");
            }
        }catch(SQLException e){System.err.println(e.getMessage());}
        return null;
    }
    public static class Conexao {
        public final int id, outroId;
        public final String nome, status;
        public final boolean recebida;
        Conexao(int id,int outroId,String nome,String status,boolean recebida){
            this.id=id;this.outroId=outroId;this.nome=nome;this.status=status;this.recebida=recebida;
        }
    }
    public List<Conexao> listar(int uid) {
        List<Conexao> lista=new ArrayList<>();
        String sql="SELECT c.id,c.solicitante_id,c.destinatario_id,c.status, " +
            "u1.nome AS solicitante,u2.nome AS destinatario FROM conexao c " +
            "JOIN usuario u1 ON u1.id=c.solicitante_id JOIN usuario u2 ON u2.id=c.destinatario_id " +
            "WHERE c.solicitante_id=? OR c.destinatario_id=? ORDER BY c.id DESC";
        try(Connection c=ConexaoBanco.getConexao();PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,uid);p.setInt(2,uid);
            try(ResultSet r=p.executeQuery()){
                while(r.next()){
                    boolean rec=r.getInt("destinatario_id")==uid;
                    lista.add(new Conexao(r.getInt("id"),
                        r.getInt(rec?"solicitante_id":"destinatario_id"),
                        r.getString(rec?"solicitante":"destinatario"),
                        r.getString("status"),rec));
                }
            }
        }catch(SQLException e){System.err.println("Conexões: "+e.getMessage());}
        return lista;
    }
    public boolean responder(int conexaoId,int destinatarioId,boolean aceitar) {
        try(Connection c=ConexaoBanco.getConexao();PreparedStatement p=c.prepareStatement(
            "UPDATE conexao SET status=? WHERE id=? AND destinatario_id=? AND status='PENDENTE'")){
            p.setString(1,aceitar?"ACEITA":"RECUSADA");
            p.setInt(2,conexaoId);p.setInt(3,destinatarioId);
            return p.executeUpdate()==1;
        }catch(SQLException e){return false;}
    }
}
