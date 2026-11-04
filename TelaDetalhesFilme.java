import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaDetalhesFilme extends JPanel {

    private final Color FUNDO = new Color(43, 22, 70);
    private final Color CARD = new Color(56, 32, 82);
    private final Color CORAL = new Color(255, 98, 92);
    private final Color ROXO = new Color(65, 37, 93);
    private final Color TEXTO = new Color(247, 243, 250);
    private final Color SUAVE = new Color(220, 204, 232);

    private final Filme filme;
    private final Runnable voltar;
    private final ComentarioDAO comentarioDAO;

    private final ArrayList<Comentario> comentarios = new ArrayList<>();

    private final JPanel lista = new JPanel();
    private final JTextArea campoComentario = new JTextArea(4, 30);

    private JLabel indicadorResposta;
    private int respondendoA = 0;
    private String autor;

    // Largura única para todas as seções da tela.
    private static final int LARGURA = 720;

    // =========================================
    // CONSTRUTOR
    // =========================================

    public TelaDetalhesFilme(Filme filme, Runnable voltar) {
        this.filme = filme;
        this.voltar = voltar;
        this.comentarioDAO = new ComentarioDAO();

        setLayout(new BorderLayout());
        setBackground(FUNDO);

        montarTela();
        carregarComentarios();
        atualizarComentarios();
    }

    // =========================================
    // COMPONENTES
    // =========================================

    private JLabel rotulo(String texto, int tamanho, boolean negrito) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Times New Roman", negrito ? Font.BOLD : Font.PLAIN, tamanho));
        label.setForeground(TEXTO);
        return label;
    }

    private JPanel painel(Color cor) {
        JPanel p = new JPanel();
        p.setBackground(cor);
        return p;
    }

    private JButton botao(String texto, Color cor) {
        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? cor.brighter() : cor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        b.setFont(new Font("Times New Roman", Font.BOLD, 16));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setBorder(new EmptyBorder(10, 15, 10, 15));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return b;
    }

    private JTextArea textoLongo(String texto, int tamanho, Color fundo) {
        JTextArea area = new JTextArea(texto);
        area.setFont(new Font("Times New Roman", Font.PLAIN, tamanho));
        area.setForeground(SUAVE);
        area.setBackground(fundo);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setFocusable(false);
        area.setBorder(null);

        return area;
    }

    private void ajustarLargura(JComponent componente) {
        componente.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension tamanho = componente.getPreferredSize();
        componente.setMaximumSize(new Dimension(LARGURA, tamanho.height));
    }

    // =========================================
    // CAPA PROPORCIONAL
    // =========================================

    private JLabel criarCapa(int largura, int altura) {
        JLabel imagem = new JLabel("Capa indisponível", SwingConstants.CENTER);
        imagem.setForeground(TEXTO);
        imagem.setPreferredSize(new Dimension(largura, altura));

        File arquivo = new File(filme.getCapa());
        if (!arquivo.isFile()) {
            arquivo = new File("src", filme.getCapa());
        }

        if (arquivo.isFile()) {
            ImageIcon original = new ImageIcon(arquivo.getAbsolutePath());
            int w = original.getIconWidth();
            int h = original.getIconHeight();

            if (w > 0 && h > 0) {
                double escala = Math.min((double) largura / w, (double) altura / h);
                int novaLargura = Math.max(1, (int) Math.round(w * escala));
                int novaAltura = Math.max(1, (int) Math.round(h * escala));

                Image ajustada = original.getImage().getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);
                imagem.setIcon(new ImageIcon(ajustada));
                imagem.setText("");
            }
        }

        return imagem;
    }

    // =========================================
    // MONTAGEM DA TELA
    // =========================================

    private void montarTela() {
        // CABEÇALHO
        JPanel cabecalho = painel(FUNDO);
        cabecalho.setLayout(new BorderLayout());
        cabecalho.setBorder(new EmptyBorder(12, 22, 12, 22));

        JLabel logo = rotulo("CineMatch", 29, true);
        logo.setFont(new Font("Nimbus Sans", Font.BOLD, 29));
        cabecalho.add(logo, BorderLayout.WEST);

        JButton voltarBotao = botao("Voltar ao filme", ROXO);
        voltarBotao.addActionListener(e -> voltar.run());
        cabecalho.add(voltarBotao, BorderLayout.EAST);

        add(cabecalho, BorderLayout.NORTH);

        // COLUNA PRINCIPAL CENTRALIZADA
        JPanel coluna = painel(FUNDO);
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));
        coluna.setBorder(new EmptyBorder(15, 0, 30, 0));

        // CAPA
        JPanel painelCapa = painel(FUNDO);
        painelCapa.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        painelCapa.add(criarCapa(200, 290));
        painelCapa.setPreferredSize(new Dimension(LARGURA, 300));
        ajustarLargura(painelCapa);

        coluna.add(painelCapa);
        coluna.add(Box.createVerticalStrut(12));

        // TÍTULO DO FILME
        JLabel titulo = rotulo(filme.getTitulo(), 28, true);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        coluna.add(titulo);
        coluna.add(Box.createVerticalStrut(18));

        String[] dados = fichaExtra(filme.getTitulo());

        // FICHA TÉCNICA
        JPanel ficha = painel(CARD);
        ficha.setLayout(new BoxLayout(ficha, BoxLayout.Y_AXIS));
        ficha.setBorder(new EmptyBorder(18, 20, 18, 20));

        adicionarLinha(ficha, "Ano: " + filme.getAno());
        adicionarLinha(ficha, "Gênero: " + filme.getGenero());
        adicionarLinha(ficha, "Direção: " + filme.getDiretor());
        adicionarLinha(ficha, "Duração: " + dados[0]);

        ficha.add(Box.createVerticalStrut(8));

        JLabel tituloElenco = rotulo("Elenco principal", 19, true);
        tituloElenco.setAlignmentX(Component.LEFT_ALIGNMENT);

        ficha.add(tituloElenco);
        ficha.add(Box.createVerticalStrut(6));

        JTextArea elenco = textoLongo(dados[1], 17, CARD);
        elenco.setRows(3);
        elenco.setColumns(55);
        elenco.setAlignmentX(Component.LEFT_ALIGNMENT);

        ficha.add(elenco);
        ficha.setPreferredSize(new Dimension(LARGURA, 235));
        ajustarLargura(ficha);

        coluna.add(ficha);
        coluna.add(Box.createVerticalStrut(15));

        // SINOPSE
        JPanel painelSinopse = painel(CARD);
        painelSinopse.setLayout(new BorderLayout(0, 10));
        painelSinopse.setBorder(new EmptyBorder(18, 20, 18, 20));

        painelSinopse.add(rotulo("Sinopse", 22, true), BorderLayout.NORTH);

        JTextArea sinopse = textoLongo(dados[2], 18, CARD);
        sinopse.setRows(5);
        sinopse.setColumns(55);

        painelSinopse.add(sinopse, BorderLayout.CENTER);
        painelSinopse.setPreferredSize(new Dimension(LARGURA, 185));
        ajustarLargura(painelSinopse);

        coluna.add(painelSinopse);
        coluna.add(Box.createVerticalStrut(12));

        // ROTTEN TOMATOES
        JPanel painelRotten = painel(FUNDO);
        painelRotten.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton rotten = botao("Consultar Rotten Tomatoes", CORAL);
        rotten.addActionListener(e -> abrirRotten());

        painelRotten.add(rotten);
        painelRotten.setPreferredSize(new Dimension(LARGURA, 58));
        ajustarLargura(painelRotten);

        coluna.add(painelRotten);
        coluna.add(Box.createVerticalStrut(18));

        // SEÇÃO DE COMENTÁRIOS
        JPanel secaoComentarios = painel(FUNDO);
        secaoComentarios.setLayout(new BoxLayout(secaoComentarios, BoxLayout.Y_AXIS));
        secaoComentarios.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tituloComentarios = rotulo("Comentários sobre o filme", 24, true);
        tituloComentarios.setAlignmentX(Component.LEFT_ALIGNMENT);

        secaoComentarios.add(tituloComentarios);
        secaoComentarios.add(Box.createVerticalStrut(12));

        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(FUNDO);
        lista.setAlignmentX(Component.LEFT_ALIGNMENT);
        lista.setMaximumSize(new Dimension(LARGURA, Integer.MAX_VALUE));

        secaoComentarios.add(lista);
        secaoComentarios.add(Box.createVerticalStrut(15));

        indicadorResposta = rotulo("Escreva seu comentário:", 18, true);
        indicadorResposta.setAlignmentX(Component.LEFT_ALIGNMENT);

        secaoComentarios.add(indicadorResposta);
        secaoComentarios.add(Box.createVerticalStrut(8));

        // CAMPO DE COMENTÁRIO
        campoComentario.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        campoComentario.setLineWrap(true);
        campoComentario.setWrapStyleWord(true);
        campoComentario.setBackground(CARD);
        campoComentario.setForeground(TEXTO);
        campoComentario.setCaretColor(TEXTO);
        campoComentario.setBorder(new EmptyBorder(12, 12, 12, 12));

        JScrollPane editor = new JScrollPane(
                campoComentario,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        editor.setPreferredSize(new Dimension(LARGURA, 110));
        editor.setMaximumSize(new Dimension(LARGURA, 110));
        editor.setAlignmentX(Component.LEFT_ALIGNMENT);

        secaoComentarios.add(editor);
        secaoComentarios.add(Box.createVerticalStrut(10));

        // AÇÕES DOS COMENTÁRIOS
        JPanel acoes = painel(FUNDO);
        acoes.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));

        JButton cancelar = botao("Cancelar resposta", ROXO);
        cancelar.addActionListener(e -> {
            respondendoA = 0;
            indicadorResposta.setText("Escreva seu comentário:");
        });

        JButton publicar = botao("Publicar", CORAL);
        publicar.addActionListener(e -> publicar());

        JButton limpar = botao("Apagar meus comentários deste filme", ROXO);
        limpar.addActionListener(e -> limparComentariosFilme());

        acoes.add(limpar);
        acoes.add(cancelar);
        acoes.add(publicar);
        acoes.setAlignmentX(Component.LEFT_ALIGNMENT);

        secaoComentarios.add(acoes);
        secaoComentarios.setMaximumSize(new Dimension(LARGURA, Integer.MAX_VALUE));

        coluna.add(secaoComentarios);

        // PAINEL CENTRAL E ROLAGEM
        JPanel pagina = painel(FUNDO);
        pagina.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pagina.add(coluna);

        JScrollPane rolagem = new JScrollPane(
                pagina,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        rolagem.setBorder(null);
        rolagem.getViewport().setBackground(FUNDO);
        rolagem.getVerticalScrollBar().setUnitIncrement(20);

        add(rolagem, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> rolagem.getVerticalScrollBar().setValue(0));
    }

    private void adicionarLinha(JPanel painel, String texto) {
        JLabel linha = rotulo(texto, 18, false);
        linha.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.add(linha);
        painel.add(Box.createVerticalStrut(6));
    }

    // =========================================
    // AS 50 FICHAS DO CATÁLOGO
    // =========================================

    private String[] fichaExtra(String titulo) {
        if (titulo.equalsIgnoreCase("Interestelar"))
            return ficha("2h48min",
                    "Matthew McConaughey, Anne Hathaway, Jessica Chastain e Michael Caine",
                    "Em uma Terra ameaçada por uma crise ambiental, o ex-piloto Cooper viaja pelo espaço com uma equipe de cientistas para encontrar um planeta onde a humanidade possa sobreviver.");

        if (titulo.equalsIgnoreCase("Matrix"))
            return ficha("2h16min",
                    "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss e Hugo Weaving",
                    "Neo é um programador que descobre uma verdade perturbadora sobre o mundo em que vive. Com a ajuda de Morpheus, começa a enfrentar as forças responsáveis por manter a humanidade em uma realidade simulada.");

        if (titulo.equalsIgnoreCase("Avatar"))
            return ficha("2h42min",
                    "Sam Worthington, Zoe Saldaña, Sigourney Weaver e Stephen Lang",
                    "Jake Sully chega ao planeta Pandora e participa de um programa científico que permite interagir com os Na'vi. Ao conhecer a cultura local, ele se vê dividido entre a missão humana e a defesa daquele mundo.");

        if (titulo.equalsIgnoreCase("Titanic"))
            return ficha("3h14min",
                    "Leonardo DiCaprio, Kate Winslet, Billy Zane e Kathy Bates",
                    "Durante a viagem inaugural do Titanic, Rose e Jack se apaixonam apesar das diferenças sociais. O romance é colocado à prova quando o navio encontra um destino trágico.");

        if (titulo.equalsIgnoreCase("Vingadores"))
            return ficha("2h23min",
                    "Robert Downey Jr., Chris Evans, Chris Hemsworth, Scarlett Johansson e Mark Ruffalo",
                    "Loki ameaça a Terra com um exército alienígena. Para impedir a invasão, Nick Fury reúne heróis poderosos que precisam aprender a trabalhar juntos.");

        if (titulo.equalsIgnoreCase("Barbie"))
            return ficha("1h54min",
                    "Margot Robbie, Ryan Gosling, America Ferrera e Kate McKinnon",
                    "Barbie vive em um mundo aparentemente perfeito. Quando começa a questionar sua existência, viaja ao mundo real e descobre novas possibilidades de compreender sua identidade.");

        if (titulo.equalsIgnoreCase("Coraline"))
            return ficha("1h40min",
                    "Dakota Fanning, Teri Hatcher, John Hodgman e Keith David (vozes)",
                    "Ao se mudar para uma casa antiga, Coraline encontra uma passagem para um mundo alternativo que parece ideal. Logo percebe que sua outra família esconde um segredo assustador.");

        if (titulo.equalsIgnoreCase("Divertida Mente"))
            return ficha("1h35min",
                    "Amy Poehler, Phyllis Smith, Bill Hader e Lewis Black (vozes)",
                    "Após uma mudança de cidade, Riley precisa lidar com novas emoções. Dentro de sua mente, Alegria, Tristeza, Medo, Raiva e Nojinho tentam ajudá-la a enfrentar essa fase.");

        if (titulo.equalsIgnoreCase("Jurassic Park"))
            return ficha("2h07min",
                    "Sam Neill, Laura Dern, Jeff Goldblum e Richard Attenborough",
                    "Um parque com dinossauros recriados geneticamente recebe seus primeiros visitantes. Quando o sistema de segurança falha, a aventura se transforma em uma luta pela sobrevivência.");

        if (titulo.equalsIgnoreCase("A noiva do Chucky"))
            return ficha("1h29min",
                    "Jennifer Tilly, Brad Dourif, Katherine Heigl e Nick Stabile",
                    "Tiffany recupera o boneco Chucky e acaba envolvida em seus planos violentos. Os dois embarcam em uma viagem macabra que mistura terror e humor.");

        if (titulo.equalsIgnoreCase("La La Land"))
            return ficha("2h08min",
                    "Emma Stone, Ryan Gosling, John Legend e Rosemarie DeWitt",
                    "Uma atriz iniciante e um pianista de jazz se apaixonam em Los Angeles. Enquanto buscam seus sonhos profissionais, descobrem os desafios de conciliar carreira e relacionamento.");

        if (titulo.equalsIgnoreCase("Diário de uma Paixão"))
            return ficha("2h03min",
                    "Ryan Gosling, Rachel McAdams, James Garner e Gena Rowlands",
                    "Noah e Allie vivem um romance durante a juventude, mas são separados pelas circunstâncias. Anos depois, sua história mostra o poder das lembranças e do amor.");

        if (titulo.equalsIgnoreCase("10 Coisas que Eu Odeio em Você"))
            return ficha("1h37min",
                    "Julia Stiles, Heath Ledger, Joseph Gordon-Levitt e Larisa Oleynik",
                    "Uma regra familiar impede Bianca de namorar antes da irmã Kat. Para contornar a situação, colegas tentam aproximar Kat do misterioso Patrick.");

        if (titulo.equalsIgnoreCase("Como Perder um Homem em 10 Dias"))
            return ficha("1h58min",
                    "Kate Hudson, Matthew McConaughey, Kathryn Hahn e Adam Goldberg",
                    "Uma jornalista tenta afastar um namorado em dez dias para escrever uma matéria. Ao mesmo tempo, um publicitário aposta que conseguirá conquistar uma mulher no mesmo período.");

        if (titulo.equalsIgnoreCase("Questão de Tempo"))
            return ficha("2h03min",
                    "Domhnall Gleeson, Rachel McAdams, Bill Nighy e Lydia Wilson",
                    "Tim descobre que pode viajar para momentos do próprio passado. Ele tenta usar essa habilidade para encontrar o amor, mas percebe que cada escolha tem consequências.");

        if (titulo.equalsIgnoreCase("Invocação do Mal"))
            return ficha("1h52min",
                    "Vera Farmiga, Patrick Wilson, Lili Taylor e Ron Livingston",
                    "Os investigadores Ed e Lorraine Warren ajudam uma família que enfrenta fenômenos sobrenaturais em sua casa. A investigação revela uma ameaça cada vez mais perigosa.");

        if (titulo.equalsIgnoreCase("Hereditário"))
            return ficha("2h07min",
                    "Toni Collette, Alex Wolff, Milly Shapiro e Gabriel Byrne",
                    "Após a morte da avó, uma família enfrenta acontecimentos perturbadores e segredos antigos. O luto se mistura a eventos sobrenaturais que ameaçam todos.");

        if (titulo.equalsIgnoreCase("Corra!"))
            return ficha("1h44min",
                    "Daniel Kaluuya, Allison Williams, Catherine Keener e Bradley Whitford",
                    "Chris visita a família de sua namorada pela primeira vez. O encontro aparentemente tranquilo se transforma em um pesadelo quando ele começa a perceber comportamentos estranhos.");

        if (titulo.equalsIgnoreCase("Um Lugar Silencioso"))
            return ficha("1h30min",
                    "Emily Blunt, John Krasinski, Millicent Simmonds e Noah Jupe",
                    "Uma família precisa sobreviver em silêncio absoluto em um mundo dominado por criaturas que caçam pelo som. Qualquer ruído pode colocar todos em perigo.");

        if (titulo.equalsIgnoreCase("Ilha do Medo"))
            return ficha("2h18min",
                    "Leonardo DiCaprio, Mark Ruffalo, Ben Kingsley e Michelle Williams",
                    "Dois agentes investigam o desaparecimento de uma paciente de um hospital psiquiátrico localizado em uma ilha. Durante a investigação, fatos estranhos colocam suas certezas em dúvida.");

        if (titulo.equalsIgnoreCase("Duna: Parte Dois"))
            return ficha("2h46min",
                    "Timothée Chalamet, Zendaya, Rebecca Ferguson, Javier Bardem e Florence Pugh",
                    "Paul Atreides se une aos Fremen em Arrakis e busca vingança pela destruição de sua família. Suas escolhas podem mudar o destino de diferentes povos.");

        if (titulo.equalsIgnoreCase("A Origem"))
            return ficha("2h28min",
                    "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page e Tom Hardy",
                    "Cobb é especialista em entrar nos sonhos das pessoas para roubar informações. Uma missão incomum exige que ele implante uma ideia em vez de roubá-la.");

        if (titulo.equalsIgnoreCase("Blade Runner 2049"))
            return ficha("2h44min",
                    "Ryan Gosling, Harrison Ford, Ana de Armas e Sylvia Hoeks",
                    "O agente K descobre um segredo capaz de transformar a relação entre humanos e replicantes. Sua investigação o leva a procurar um antigo blade runner desaparecido.");

        if (titulo.equalsIgnoreCase("John Wick"))
            return ficha("1h41min",
                    "Keanu Reeves, Michael Nyqvist, Alfie Allen e Willem Dafoe",
                    "Um assassino aposentado tenta reconstruir sua vida após a morte da esposa. Um ataque violento o faz retornar ao submundo do crime em busca de vingança.");

        if (titulo.equalsIgnoreCase("Mad Max: Estrada da Fúria"))
            return ficha("2h00min",
                    "Tom Hardy, Charlize Theron, Nicholas Hoult e Hugh Keays-Byrne",
                    "Em um deserto pós-apocalíptico, Max se une a Furiosa para escapar de um tirano. Uma perseguição intensa coloca os dois diante de grandes perigos.");

        if (titulo.equalsIgnoreCase("Shrek"))
            return ficha("1h30min",
                    "Mike Myers, Eddie Murphy, Cameron Diaz e John Lithgow (vozes)",
                    "Um ogro que gosta de viver isolado precisa resgatar uma princesa para recuperar seu pântano. Ao lado de um burro falante, vive uma aventura cheia de humor.");

        if (titulo.equalsIgnoreCase("Toy Story"))
            return ficha("1h21min",
                    "Tom Hanks, Tim Allen, Don Rickles e Jim Varney (vozes)",
                    "Os brinquedos de Andy ganham vida quando ninguém está olhando. Woody fica preocupado quando Buzz Lightyear chega e ameaça sua posição como favorito.");

        if (titulo.equalsIgnoreCase("Viva: A Vida é uma Festa"))
            return ficha("1h45min",
                    "Anthony Gonzalez, Gael García Bernal, Benjamin Bratt e Alanna Ubach (vozes)",
                    "Miguel sonha em ser músico, apesar da proibição de sua família. Durante o Dia dos Mortos, ele chega ao mundo dos antepassados e descobre segredos de sua história.");

        if (titulo.equalsIgnoreCase("Homem-Aranha no Aranhaverso"))
            return ficha("1h57min",
                    "Shameik Moore, Jake Johnson, Hailee Steinfeld e Mahershala Ali (vozes)",
                    "Miles Morales desenvolve poderes de Homem-Aranha e conhece heróis vindos de outras dimensões. Juntos, precisam impedir uma ameaça aos diferentes universos.");

        if (titulo.equalsIgnoreCase("A Viagem de Chihiro"))
            return ficha("2h05min",
                    "Rumi Hiiragi, Miyu Irino, Mari Natsuki e Bunta Sugawara (vozes)",
                    "Chihiro entra em um mundo habitado por espíritos e criaturas mágicas. Para salvar os pais, precisa encontrar coragem e uma maneira de voltar para casa.");

        if (titulo.equalsIgnoreCase("As Branquelas"))
            return ficha("1h49min",
                    "Shawn Wayans, Marlon Wayans, Jaime King e Busy Philipps",
                    "Dois agentes do FBI assumem a identidade de duas herdeiras para protegê-las. O disfarce provoca situações cada vez mais inesperadas.");

        if (titulo.equalsIgnoreCase("Gente Grande"))
            return ficha("1h42min",
                    "Adam Sandler, Kevin James, Chris Rock, David Spade e Rob Schneider",
                    "Amigos de infância se reencontram após a morte de seu antigo treinador. Um fim de semana com suas famílias revive lembranças e cria muitas confusões.");

        if (titulo.equalsIgnoreCase("Se Beber, Não Case!"))
            return ficha("1h40min",
                    "Bradley Cooper, Ed Helms, Zach Galifianakis e Justin Bartha",
                    "Três amigos acordam em Las Vegas sem lembrar dos acontecimentos da noite anterior. Eles precisam encontrar o noivo desaparecido antes do casamento.");

        if (titulo.equalsIgnoreCase("Piratas do Caribe: A Maldição do Pérola Negra"))
            return ficha("2h23min",
                    "Johnny Depp, Orlando Bloom, Keira Knightley e Geoffrey Rush",
                    "Jack Sparrow e Will Turner partem para resgatar Elizabeth Swann. A missão os coloca diante de uma tripulação de piratas afetada por uma antiga maldição.");

        if (titulo.equalsIgnoreCase("Jumanji: Bem-Vindo à Selva"))
            return ficha("1h59min",
                    "Dwayne Johnson, Kevin Hart, Jack Black e Karen Gillan",
                    "Quatro adolescentes são transportados para dentro de um videogame misterioso. Para voltar ao mundo real, precisam completar uma missão perigosa.");

        if (titulo.equalsIgnoreCase("Forrest Gump"))
            return ficha("2h22min",
                    "Tom Hanks, Robin Wright, Gary Sinise e Sally Field",
                    "Forrest relembra sua vida, marcada por encontros inesperados e acontecimentos históricos. Apesar das dificuldades, segue seus sentimentos e o amor por Jenny.");

        if (titulo.equalsIgnoreCase("À Procura da Felicidade"))
            return ficha("1h57min",
                    "Will Smith, Jaden Smith e Thandiwe Newton",
                    "Chris Gardner enfrenta problemas financeiros enquanto cuida do filho pequeno. Determinado a construir um futuro melhor, busca uma oportunidade profissional.");

        if (titulo.equalsIgnoreCase("O Poderoso Chefão"))
            return ficha("2h55min",
                    "Marlon Brando, Al Pacino, James Caan e Diane Keaton",
                    "A família Corleone controla um império do crime organizado. Quando seu patriarca é ameaçado, Michael começa a se envolver nos negócios familiares.");

        if (titulo.equalsIgnoreCase("Um Sonho de Liberdade"))
            return ficha("2h22min",
                    "Tim Robbins, Morgan Freeman, Bob Gunton e William Sadler",
                    "Andy Dufresne é condenado por um crime que afirma não ter cometido. Na prisão, desenvolve uma amizade importante e luta para preservar sua esperança.");

        if (titulo.equalsIgnoreCase("Oppenheimer"))
            return ficha("3h00min",
                    "Cillian Murphy, Emily Blunt, Robert Downey Jr. e Matt Damon",
                    "O filme acompanha a trajetória do físico J. Robert Oppenheimer, seu trabalho no desenvolvimento da bomba atômica e os conflitos morais e políticos decorrentes.");

        if (titulo.equalsIgnoreCase("Central do Brasil"))
            return ficha("1h53min",
                    "Fernanda Montenegro, Vinícius de Oliveira, Marília Pêra e Othon Bastos",
                    "Dora trabalha escrevendo cartas para passageiros de uma estação ferroviária. Ao conhecer Josué, inicia uma viagem pelo interior do Brasil para ajudá-lo.");

        if (titulo.equalsIgnoreCase("Cidade de Deus"))
            return ficha("2h10min",
                    "Alexandre Rodrigues, Leandro Firmino, Douglas Silva e Seu Jorge",
                    "A história acompanha moradores da Cidade de Deus durante décadas marcadas pelo crescimento da violência. Buscapé tenta construir outro caminho por meio da fotografia.");

        if (titulo.equalsIgnoreCase("O Auto da Compadecida"))
            return ficha("1h44min",
                    "Matheus Nachtergaele, Selton Mello, Fernanda Montenegro e Marco Nanini",
                    "No sertão nordestino, João Grilo e Chicó tentam sobreviver usando esperteza e imaginação. Suas aventuras envolvem figuras religiosas e situações cheias de humor.");

        if (titulo.equalsIgnoreCase("Bacurau"))
            return ficha("2h11min",
                    "Sônia Braga, Udo Kier, Bárbara Colen e Thomas Aquino",
                    "Após a morte de uma moradora importante, uma comunidade do sertão enfrenta acontecimentos misteriosos. Ao perceber que está ameaçada, a população se organiza.");

        if (titulo.equalsIgnoreCase("Que Horas Ela Volta?"))
            return ficha("1h52min",
                    "Regina Casé, Camila Márdila, Karine Teles e Lourenço Mutarelli",
                    "Val trabalha há anos como empregada doméstica em São Paulo. A chegada de sua filha Jéssica provoca mudanças e questiona diferenças sociais.");

        if (titulo.equalsIgnoreCase("Wicked"))
            return ficha("2h40min",
                    "Cynthia Erivo, Ariana Grande, Jonathan Bailey e Michelle Yeoh",
                    "Na Terra de Oz, Elphaba e Glinda desenvolvem uma amizade inesperada. Suas diferenças e decisões políticas transformam o destino das duas.");

        if (titulo.equalsIgnoreCase("Mamma Mia!"))
            return ficha("1h48min",
                    "Meryl Streep, Amanda Seyfried, Pierce Brosnan e Colin Firth",
                    "Antes do casamento em uma ilha grega, Sophie convida três homens que podem ser seu pai. A chegada deles desperta lembranças e confusões.");

        if (titulo.equalsIgnoreCase("O Rei Leão"))
            return ficha("1h28min",
                    "Matthew Broderick, James Earl Jones, Jeremy Irons e Moira Kelly (vozes)",
                    "Simba é um jovem leão destinado a assumir o lugar de seu pai. Após uma tragédia, precisa enfrentar o passado e descobrir seu verdadeiro papel.");

        if (titulo.equalsIgnoreCase("A Marcha dos Pinguins"))
            return ficha("1h20min",
                    "Pinguins-imperadores; narração de Morgan Freeman na versão em inglês",
                    "O documentário acompanha a jornada anual dos pinguins-imperadores na Antártida, enfrentando temperaturas extremas para se reproduzir e proteger seus filhotes.");

        if (titulo.equalsIgnoreCase("Amy"))
            return ficha("2h08min",
                    "Amy Winehouse, familiares, amigos e profissionais ligados à cantora",
                    "Por meio de gravações e entrevistas, o documentário acompanha a carreira de Amy Winehouse, seu talento e as dificuldades que enfrentou.");

        return ficha("Não informada", "Não informado", "Sinopse ainda não cadastrada.");
    }

    private String[] ficha(String duracao, String elenco, String sinopse) {
        return new String[]{duracao, elenco, sinopse};
    }

    // =========================================
    // ROTTEN TOMATOES
    // =========================================

    private void abrirRotten() {
        try {
            String busca = URLEncoder.encode(filme.getTitulo(), StandardCharsets.UTF_8.name());
            Desktop.getDesktop().browse(new URI("https://www.rottentomatoes.com/search?search=" + busca));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Não foi possível abrir o Rotten Tomatoes.");
        }
    }

    // =========================================
    // PERSISTÊNCIA VIA H2 (ComentarioDAO)
    // =========================================

    private String limpar(String texto) {
        return texto.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ').trim();
    }

    private void carregarComentarios() {
        comentarios.clear();
        List<Comentario> listaBanco = comentarioDAO.listarPorFilme(filme.getId());
        if (listaBanco != null) {
            comentarios.addAll(listaBanco);
        }
    }

    private void limparComentariosFilme() {
        int escolha = JOptionPane.showConfirmDialog(
                this,
                "Apagar os comentários que você publicou em " + filme.getTitulo()
                + " e as respostas a eles?\nEssa ação não pode ser desfeita.",
                "Confirmar limpeza",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (escolha != JOptionPane.YES_OPTION) {
            return;
        }

        if (!Sessao.adulta()) {
            JOptionPane.showMessageDialog(this, "Apenas contas verificadas como 18+ podem comentar.");
            return;
        }
        if (!comentarioDAO.limparMeusPorFilme(filme.getId(), Sessao.getUsuario().getId())) {
            JOptionPane.showMessageDialog(this, "Não foi possível apagar seus comentários.");
            return;
        }
        carregarComentarios();
        respondendoA = 0;
        indicadorResposta.setText("Escreva seu comentário:");
        atualizarComentarios();
        JOptionPane.showMessageDialog(this, "Seus comentários de teste deste filme foram apagados.");
    }

    // =========================================
    // PUBLICAR COMENTÁRIO OU RESPOSTA
    // =========================================

    private void publicar() {
        String mensagem = limpar(campoComentario.getText());

        if (mensagem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um comentário antes de publicar.");
            return;
        }

        if (!Sessao.adulta()) {
            JOptionPane.showMessageDialog(this, "Complete a data de nascimento (18+) em Meu Perfil antes de comentar.");
            return;
        }
        autor = Sessao.getUsuario().getNome();

        Comentario novo = new Comentario(
                0,
                respondendoA,
                0,
                filme.getTitulo(),
                autor,
                mensagem
        );
        novo.setFilmeId(filme.getId());
        novo.setAutorId(Sessao.getUsuario().getId());

        if (!comentarioDAO.inserir(novo)) {
            JOptionPane.showMessageDialog(this, "Não foi possível publicar o comentário.");
            return;
        }

        campoComentario.setText("");
        respondendoA = 0;
        indicadorResposta.setText("Escreva seu comentário:");

        carregarComentarios();
        atualizarComentarios();
    }

    // =========================================
    // EXIBIÇÃO DOS COMENTÁRIOS
    // =========================================

    private void atualizarComentarios() {
        lista.removeAll();
        int encontrados = 0;

        for (Comentario c : comentarios) {
            if (c.getRespostaA() != 0) {
                continue;
            }

            lista.add(cartaoComentario(c, false));
            lista.add(Box.createVerticalStrut(7));
            encontrados++;

            for (Comentario resposta : comentarios) {
                if (resposta.getRespostaA() == c.getId()) {
                    lista.add(cartaoComentario(resposta, true));
                    lista.add(Box.createVerticalStrut(7));
                }
            }

            lista.add(Box.createVerticalStrut(10));
        }

        if (encontrados == 0) {
            JLabel vazio = rotulo("Seja a primeira pessoa a comentar.", 17, false);
            vazio.setAlignmentX(Component.LEFT_ALIGNMENT);
            lista.add(vazio);
        }

        lista.revalidate();
        lista.repaint();
        revalidate();
        repaint();
    }

    // =========================================
    // CARTÃO ALINHADO À ESQUERDA
    // =========================================

    private JPanel cartaoComentario(Comentario c, boolean resposta) {
        Color fundoCartao = resposta ? new Color(70, 44, 93) : CARD;
        JPanel cartao = painel(fundoCartao);

        cartao.setLayout(new BorderLayout(8, 8));
        cartao.setBorder(new EmptyBorder(12, resposta ? 25 : 12, 12, 12));
        cartao.setAlignmentX(Component.LEFT_ALIGNMENT);
        int linhasEstimadas = Math.max(2, 1 + c.getTexto().length() / 75);
        int alturaCartao = 108 + linhasEstimadas * 22;
        cartao.setPreferredSize(new Dimension(LARGURA, alturaCartao));
        cartao.setMaximumSize(new Dimension(LARGURA, alturaCartao));

        String titulo = resposta ? "Resposta de " + c.getAutor() : c.getAutor();
        cartao.add(rotulo(titulo, 18, true), BorderLayout.NORTH);

        JTextArea mensagem = textoLongo(c.getTexto(), 17, fundoCartao);
        cartao.add(mensagem, BorderLayout.CENTER);

        JPanel botoes = painel(fundoCartao);
        botoes.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));

        JButton curtir = botao("Curtir (" + c.getCurtidas() + ")", CORAL);
        curtir.addActionListener(e -> {
            if (!Sessao.adulta()) {
                JOptionPane.showMessageDialog(this, "Complete sua data de nascimento (18+) antes de curtir.");
                return;
            }
            if (!comentarioDAO.curtir(c.getId(), Sessao.getUsuario().getId())) {
                JOptionPane.showMessageDialog(this, "Você já curtiu este comentário.");
            }
            carregarComentarios();
            atualizarComentarios();
        });

        botoes.add(curtir);

        if (!resposta) {
            JButton responder = botao("Responder", ROXO);
            responder.addActionListener(e -> {
                respondendoA = c.getId();
                indicadorResposta.setText("Respondendo a " + c.getAutor() + ":");
                campoComentario.requestFocusInWindow();
            });

            botoes.add(responder);
        }

        cartao.add(botoes, BorderLayout.SOUTH);
        return cartao;
    }
}