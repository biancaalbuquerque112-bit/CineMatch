import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/** Testes de integração sem abrir janelas, para a disciplina de POO. */
public class TestesCineMatch {
    private static int ok=0;
    private static void validar(boolean cond,String descricao){
        if(!cond)throw new AssertionError("FALHOU: "+descricao);
        System.out.println("OK: "+descricao);ok++;
    }
    public static void main(String[] args)throws Exception{
        System.setProperty("java.awt.headless","true");
        // O banco de testes é SEPARADO do banco real: não apaga dados pessoais.
        System.setProperty("cinematch.banco","./banco/testes_cinematch");
        ConexaoBanco.inicializarBanco();
        List<Filme> filmes=new FilmeDAO().listarTodos();
        validar(new Catalogo().getFilmes().size()==50,"Catálogo original contém 50 títulos");
        validar(filmes.size()==50,"Banco apresenta 50 filmes oficiais");
        for(Filme f:filmes){
            validar(f.getId()>0,"ID positivo: "+f.getTitulo());
            validar(new File(f.getCapa()).isFile(),"Capa: "+f.getTitulo());
            validar(f.getDiretor()!=null&&!f.getDiretor().isBlank(),"Diretor: "+f.getTitulo());
        }
        TelaDetalhesFilme detalhes=new TelaDetalhesFilme(filmes.get(0),()->{});
        Method ficha=TelaDetalhesFilme.class.getDeclaredMethod("fichaExtra",String.class);
        ficha.setAccessible(true);
        for(Filme f:filmes){
            String[] dados=(String[])ficha.invoke(detalhes,f.getTitulo());
            validar(dados.length==3 && !dados[0].startsWith("Não") &&
                !dados[2].contains("não cadastrada"),"Ficha completa: "+f.getTitulo());
        }

        UsuarioDAO usuarios=new UsuarioDAO();
        String suffix=Long.toString(System.nanoTime());
        String senha="senhaExcelente123";
        Usuario a=new Usuario("Teste Ana","ana"+suffix+"@example.test",senha);
        Usuario b=new Usuario("Teste Bia","bia"+suffix+"@example.test",senha);
        Usuario menor=new Usuario("Menor","menor"+suffix+"@example.test",senha);
        a.setNascimento(LocalDate.now().minusYears(22));
        b.setNascimento(LocalDate.now().minusYears(23));
        menor.setNascimento(LocalDate.now().minusYears(17));
        validar(!usuarios.cadastrar(menor),"Cadastro de menor de 18 bloqueado");
        validar(usuarios.cadastrar(a)&&usuarios.cadastrar(b),"Cadastro persistido de duas contas adultas");
        validar(usuarios.autenticar(a.getEmail(),"errada")==null,"Senha incorreta bloqueada");
        validar(usuarios.autenticar(a.getEmail(),senha)!=null,"Autenticação correta");
        a.setIntencao("Amizades e relacionamento");
        b.setIntencao("Amizades e relacionamento");
        a.setGenerosFavoritos("Ficção Científica|Drama");
        b.setGenerosFavoritos("Ficção Científica|Terror");
        validar(usuarios.atualizarPerfil(a)&&usuarios.atualizarPerfil(b),"Perfis separados no banco");
        validar(usuarios.buscarPorId(a.getId()).getGenerosFavoritos().contains("Drama"),"Perfil A próprio");
        validar(!usuarios.buscarPorId(b.getId()).getGenerosFavoritos().contains("Drama"),"Perfil B próprio");

        int filmeId=filmes.get(0).getId();
        PreferenciaFilmeDAO pref=new PreferenciaFilmeDAO();
        validar(pref.marcarCurtido(a.getId(),filmeId),"Curtir conta A");
        validar(pref.marcarCurtido(b.getId(),filmeId),"Curtir conta B");
        validar(pref.marcarCurtido(b.getId(),filmeId),"Curtida repetida não dá erro");
        validar(pref.contarCurtidas(filmeId)==2,"Curtidas únicas por conta");
        validar(pref.marcarSalvo(a.getId(),filmeId),"Favorito persistido");
        validar(pref.idsSalvos(a.getId()).contains(filmeId),"Favorito carregado");
        validar(!pref.idsSalvos(b.getId()).contains(filmeId),"Favorito isolado por conta");
        List<AlgoritmoMatch.Resultado> matches=new MatchDAO().sugerir(a.getId(),filmeId);
        validar(matches.stream().anyMatch(r->r.usuario.getId()==b.getId()),"Match real baseado em filme compartilhado");
        validar(matches.stream().noneMatch(r->r.usuario.getId()==a.getId()),"Usuário não dá match consigo");
        b.setPermitirSugestoes(false);usuarios.atualizarPerfil(b);
        validar(new MatchDAO().sugerir(a.getId(),filmeId).isEmpty(),"Opt-out de sugestões respeitado");
        b.setPermitirSugestoes(true);usuarios.atualizarPerfil(b);
        MatchDAO matchesDao=new MatchDAO();
        validar(matchesDao.solicitar(a.getId(),b.getId()),"Solicitação real de conexão");
        validar(!matchesDao.solicitar(a.getId(),b.getId()),"Solicitação duplicada bloqueada");
        List<MatchDAO.Conexao> recebidas=matchesDao.listar(b.getId());
        validar(recebidas.size()==1 && recebidas.get(0).recebida,"Conexão chega ao destinatário");
        validar(matchesDao.responder(recebidas.get(0).id,b.getId(),true),"Conexão aceita");
        validar(matchesDao.statusEntre(a.getId(),b.getId()).startsWith("ACEITA"),"Status de conexão persistido");

        ComentarioDAO cd=new ComentarioDAO();
        Comentario c=new Comentario(0,0,0,filmes.get(0).getTitulo(),a.getNome(),"Teste de comentário");
        c.setFilmeId(filmeId);c.setAutorId(a.getId());
        validar(cd.inserir(c),"Comentário autenticado salvo no banco");
        Comentario inserido=cd.listarPorFilme(filmeId).stream()
                .filter(x->x.getAutorId()==a.getId()).findFirst().orElseThrow();
        validar(cd.curtir(inserido.getId(),b.getId()),"Comentário curtido por outra conta");
        validar(!cd.curtir(inserido.getId(),b.getId()),"Curtida de comentário duplicada impedida");
        validar(cd.limparMeusPorFilme(filmeId,a.getId()),"Comentários do autor apagados");
        validar(cd.listarPorFilme(filmeId).stream().noneMatch(x->x.getAutorId()==a.getId()),"Limpeza concluída");
        System.out.println("\nTESTES APROVADOS: "+ok);
    }
}
