import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaConexoes extends JFrame {
    private final Color FUNDO = new Color(43, 22, 70);
    private final Color CARD = new Color(56, 32, 82);
    private final Color CORAL = new Color(255, 98, 92);
    private final Color ROXO = new Color(65, 37, 93);
    private final Color TEXTO = new Color(247, 243, 250);
    private final Color SUAVE = new Color(220, 204, 232);

    private final MatchDAO dao = new MatchDAO();
    private final JPanel recebidas = coluna();
    private final JPanel enviadas = coluna();
    private final JPanel conectadas = coluna();
    private final CardLayout organizador = new CardLayout();
    private final JPanel conteudoAbas = new JPanel(organizador);
    private final JButton[] botoesAbas = new JButton[3];

    public TelaConexoes() {
        setTitle("CineMatch - Minhas Conexões");
        setSize(890, 690);
        setMinimumSize(new Dimension(640, 510));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(0, 18));
        principal.setBackground(FUNDO);
        principal.setBorder(new EmptyBorder(22, 26, 22, 26));

        JPanel cab = new JPanel(new BorderLayout(12, 8));
        cab.setOpaque(false);
        JPanel nomes = coluna();
        JLabel marca = texto("CineMatch", 24, CORAL, true);
        marca.setFont(new Font("Nimbus Sans", Font.BOLD, 24));
        nomes.add(marca);
        nomes.add(Box.createVerticalStrut(4));
        nomes.add(texto("Minhas Conexões", 28, TEXTO, true));
        cab.add(nomes, BorderLayout.WEST);
        JButton fechar = botao("Voltar", ROXO);
        fechar.addActionListener(e -> dispose());
        cab.add(fechar, BorderLayout.EAST);
        principal.add(cab, BorderLayout.NORTH);

        // Três abas desenhadas nas cores do CineMatch, sem tema azul do SO.
        JPanel zonaCentral = new JPanel(new BorderLayout(0, 12));
        zonaCentral.setOpaque(false);
        JPanel guia = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        guia.setOpaque(false);
        String[] nomesAbas = {"Recebidas", "Enviadas", "Conectadas"};
        for (int i = 0; i < nomesAbas.length; i++) {
            final int indice = i;
            botoesAbas[i] = botao(nomesAbas[i], ROXO);
            botoesAbas[i].addActionListener(e -> mudarAba(indice));
            guia.add(botoesAbas[i]);
        }
        zonaCentral.add(guia, BorderLayout.NORTH);
        conteudoAbas.setBackground(FUNDO);
        conteudoAbas.add(rolagem(recebidas), "0");
        conteudoAbas.add(rolagem(enviadas), "1");
        conteudoAbas.add(rolagem(conectadas), "2");
        zonaCentral.add(conteudoAbas, BorderLayout.CENTER);
        principal.add(zonaCentral, BorderLayout.CENTER);
        mudarAba(0);

        JButton atualizar = botao("Atualizar conexões", CORAL);
        atualizar.addActionListener(e -> recarregar());
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setOpaque(false);
        rodape.add(atualizar);
        principal.add(rodape, BorderLayout.SOUTH);

        setContentPane(principal);
        recarregar();
        if (recebidas.getComponentCount() == 1 &&
                conectadas.getComponentCount() > 1) mudarAba(2);
        setVisible(true);
    }

    private void mudarAba(int indice) {
        organizador.show(conteudoAbas, String.valueOf(indice));
        for (int i = 0; i < botoesAbas.length; i++) {
            botoesAbas[i].setBackground(i == indice ? CORAL : ROXO);
        }
    }

    private JPanel coluna() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(FUNDO);
        return p;
    }

    private JScrollPane rolagem(JPanel lista) {
        JPanel central = new JPanel(new GridBagLayout());
        central.setBackground(FUNDO);
        GridBagConstraints pos = new GridBagConstraints();
        pos.gridx = 0; pos.gridy = 0;
        pos.anchor = GridBagConstraints.NORTH;
        pos.weightx = 1;
        pos.weighty = 1;
        pos.insets = new Insets(12, 8, 12, 8);
        central.add(lista, pos);
        JScrollPane scroll = new JScrollPane(central,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(FUNDO);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.setBorder(null);
        return scroll;
    }

    private JLabel texto(String conteudo, int fonte, Color cor, boolean negrito) {
        JLabel l = new JLabel(conteudo);
        l.setForeground(cor);
        l.setFont(new Font("Times New Roman", negrito ? Font.BOLD : Font.PLAIN, fonte));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JButton botao(String nome, Color cor) {
        JButton b = new JButton(nome);
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Times New Roman", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(11, 17, 11, 17));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void recarregar() {
        recebidas.removeAll();
        enviadas.removeAll();
        conectadas.removeAll();

        if (!Sessao.ativa()) {
            recebidas.add(texto("Entre novamente para consultar suas conexões.", 17, SUAVE, false));
        } else {
            int minhaConta = Sessao.getUsuario().getId();
            List<MatchDAO.Conexao> lista = dao.listar(minhaConta);
            int quantRecebidas = 0, quantEnviadas = 0, quantConectadas = 0;
            for (MatchDAO.Conexao c : lista) {
                if ("ACEITA".equals(c.status)) {
                    conectadas.add(cartao(c, "Vocês estão conectados!", 2));
                    conectadas.add(Box.createVerticalStrut(12));
                    quantConectadas++;
                } else if (c.recebida && "PENDENTE".equals(c.status)) {
                    recebidas.add(cartao(c, "Quer se conectar com você", 0));
                    recebidas.add(Box.createVerticalStrut(12));
                    quantRecebidas++;
                } else if (!c.recebida) {
                    String estado = "RECUSADA".equals(c.status)
                            ? "Solicitação recusada" : "Aguardando resposta";
                    enviadas.add(cartao(c, estado, 1));
                    enviadas.add(Box.createVerticalStrut(12));
                    quantEnviadas++;
                }
            }
            if (quantRecebidas == 0) vazio(recebidas, "Nenhuma solicitação recebida.");
            if (quantEnviadas == 0) vazio(enviadas, "Você ainda não enviou solicitações.");
            if (quantConectadas == 0) vazio(conectadas,
                    "Quando uma solicitação for aceita, seu chat aparecerá aqui.");
        }
        recebidas.revalidate(); recebidas.repaint();
        enviadas.revalidate(); enviadas.repaint();
        conectadas.revalidate(); conectadas.repaint();
    }

    private void vazio(JPanel destino, String mensagem) {
        JPanel cartao = new JPanel(new BorderLayout());
        cartao.setBackground(CARD);
        cartao.setBorder(new EmptyBorder(30, 25, 30, 25));
        cartao.add(texto(mensagem, 17, SUAVE, false), BorderLayout.CENTER);
        cartao.setAlignmentX(Component.CENTER_ALIGNMENT);
        destino.add(cartao);
    }

    // tipo 0=recebida; 1=enviada; 2=conexão aceita.
    private JPanel cartao(MatchDAO.Conexao c, String status, int tipo) {
        JPanel card = new JPanel(new BorderLayout(14, 12));
        card.setBackground(CARD);
        card.setBorder(new EmptyBorder(17, 20, 17, 20));
        card.setPreferredSize(new Dimension(625, 108));
        card.setMaximumSize(new Dimension(625, 108));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel info = coluna();
        info.setBackground(CARD);
        info.add(texto(c.nome, 21, TEXTO, true));
        info.add(Box.createVerticalStrut(6));
        info.add(texto(status, 15, SUAVE, false));
        card.add(info, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 0));
        botoes.setOpaque(false);
        if (tipo == 0) {
            JButton aceitar = botao("Aceitar", CORAL);
            JButton recusar = botao("Recusar", ROXO);
            aceitar.addActionListener(e -> responder(c.id, true));
            recusar.addActionListener(e -> responder(c.id, false));
            botoes.add(aceitar);
            botoes.add(recusar);
        } else if (tipo == 2) {
            JButton conversar = botao("Conversar", CORAL);
            conversar.addActionListener(e -> {
                if (!Sessao.ativa() || !new MensagemDAO().podeConversar(
                        c.id, Sessao.getUsuario().getId())) {
                    Avisos.erro(this, "Esta conversa não está disponível.");
                    recarregar();
                    return;
                }
                new TelaChat(c.id, c.nome);
            });
            botoes.add(conversar);
        }
        if (botoes.getComponentCount() > 0) card.add(botoes, BorderLayout.EAST);
        return card;
    }

    private void responder(int id, boolean aceitar) {
        if (Sessao.ativa() && dao.responder(id, Sessao.getUsuario().getId(), aceitar)) {
            recarregar();
            Avisos.sucesso(this, aceitar
                    ? "Vocês agora estão conectados! Que tal começar uma conversa?"
                    : "Solicitação recusada.");
            if (aceitar) mudarAba(2);
        } else {
            Avisos.erro(this, "Não foi possível responder à solicitação.");
        }
    }
}
