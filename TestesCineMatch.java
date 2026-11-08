import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class TestesCineMatch {
    private static int ok=0;
    private static void validar(boolean cond,String descricao){
        if(!cond)throw new AssertionError("FALHOU: "+descricao);
        System.out.println("OK: "+descricao);ok++;
    }
    public static void main(String[] args)throws Exception{
        System.setProperty("java.awt.headless","true");
        // Banco TEMPORÁRIO novo a cada execução: nunca altera dados pessoais,
        // nem acumula contas e curtidas de testes anteriores.
        java.nio.file.Path pastaTeste = java.nio.file.Files.createTempDirectory("cinematch-poo-testes-");
        String bancoTeste = pastaTeste.resolve("dados").toString();
        System.setProperty("cinematch.banco", bancoTeste);
        ConexaoBanco.inicializarBanco();
        List<Filme> filmes=new FilmeDAO().listarTodos();
        validar(new Catalogo().getFilmes().size()==53,"Catálogo contém os 50 originais e a trilogia Batman");
        validar(filmes.size()==53,"Banco apresenta 53 filmes oficiais");
        for (String titulo : new String[]{"Batman Begins", "Batman: O Cavaleiro das Trevas",
                 "Batman: O Cavaleiro das Trevas Ressurge"}) {
            validar(new FilmeDAO().buscarPorTitulo(titulo)!=null,
                    "Batman sincronizado no H2: "+titulo);
        }
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
        validar(pref.listarPendentes(a.getId()).stream().noneMatch(
                f -> f.getId()==filmeId),
                "Filme curtido não reaparece em Descobrir após recarregar");
        validar(pref.listarPendentes(a.getId()).size()==52,
                "Conta com uma curtida recebe 52 filmes pendentes");
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
        int conexaoId = recebidas.get(0).id;
        MensagemDAO mensagens = new MensagemDAO();
        validar(!mensagens.enviar(conexaoId,a.getId(),"Ainda sem aceitação"),
                "Chat bloqueado antes de aceitar conexão");
        validar(matchesDao.responder(conexaoId,b.getId(),true),"Conexão aceita");
        validar(matchesDao.statusEntre(a.getId(),b.getId()).startsWith("ACEITA"),
                "Status de conexão persistido");
        validar(mensagens.podeConversar(conexaoId, a.getId()),
                "Conectado autorizado ao chat");
        validar(mensagens.podeConversar(conexaoId, b.getId()),
                "Destinatário autorizado ao chat");
        validar(mensagens.enviar(conexaoId,a.getId(),"Oi! Você gosta de Interestelar?"),
                "Primeira mensagem gravada");
        validar(mensagens.enviar(conexaoId,b.getId(),"Sim, gosto muito!"),
                "Resposta no chat gravada");
        validar(new MensagemDAO().listar(conexaoId,a.getId()).size()==2,
                "Mensagens persistem após abrir novo DAO");
        validar(new MensagemDAO().listar(conexaoId,b.getId()).get(0).getRemetenteId()==a.getId(),
                "Mensagens ordenadas pelo envio");
        validar(!mensagens.enviar(conexaoId,999_999,"Tentativa não autorizada"),
                "Pessoa externa não envia mensagem");
        validar(mensagens.listar(conexaoId,999_999).isEmpty(),
                "Pessoa externa não lê mensagens");
        validar(!mensagens.enviar(conexaoId,a.getId()," "),
                "Mensagem vazia não é enviada");

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
        Sessao.entrar(a);
        validar(Sessao.ativa(),"Login inicia sessão");
        Sessao.ajustarInicioParaTeste(java.time.Instant.now().minus(
                java.time.Duration.ofHours(23)));
        Sessao.atualizarUsuario(a);
        validar(Sessao.ativa(),"Atualização do perfil não encerra sessão antes de 24h");
        Sessao.ajustarInicioParaTeste(java.time.Instant.now().minus(
                java.time.Duration.ofHours(25)));
        validar(!Sessao.ativa(),"Sessão expira após 24h");
        validar(Sessao.consumirExpiracao(),"Aviso de sessão expirada identificado");
        validar(!Sessao.consumirExpiracao(),"Aviso de expiração ocorre uma só vez");
        Sessao.entrar(a);
        validar(Sessao.ativa(),"Novo login reabre sessão");
        Sessao.sair();
        System.out.println("\nTESTES APROVADOS: "+ok);
        java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(bancoTeste + ".mv.db"));
        java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(bancoTeste + ".lock.db"));
        java.nio.file.Files.deleteIfExists(pastaTeste);
    }
}
