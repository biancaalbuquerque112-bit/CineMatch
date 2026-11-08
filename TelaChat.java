import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaChat extends JFrame {
    private static final Color FUNDO = new Color(43, 22, 70);
    private static final Color CARD = new Color(56, 32, 82);
    private static final Color ROXO = new Color(65, 37, 93);
    private static final Color CORAL = new Color(255, 98, 92);
    private static final Color TEXTO = new Color(247, 243, 250);
    private static final Color SUAVE = new Color(220, 204, 232);
    private final MensagemDAO dao = new MensagemDAO();
    private final int conexaoId;
    private final int usuarioId;
    private final JPanel corpo = new JPanel();
    private final JTextArea escrever = new JTextArea(3, 30);
    private JScrollPane rolagem;
    private Timer atualizacao;
    private long ultimoId = -1;

    public TelaChat(int conexaoId, String nomeOutraPessoa) {
        this.conexaoId = conexaoId;
        if (!Sessao.adulta()) {
            throw new IllegalStateException("É necessário entrar na conta para conversar.");
        }
        this.usuarioId = Sessao.getUsuario().getId();
        if (!dao.podeConversar(conexaoId, usuarioId)) {
            throw new IllegalArgumentException("O chat exige uma conexão aceita.");
        }
        setTitle("CineMatch - Conversa com " + nomeOutraPessoa);
        setSize(710, 700);
        setMinimumSize(new Dimension(560, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel pagina = new JPanel(new BorderLayout(0, 12));
        pagina.setBackground(FUNDO);
        pagina.setBorder(new EmptyBorder(20, 23, 20, 23));

        JPanel cab = new JPanel(new BorderLayout());
        cab.setOpaque(false);
        JLabel titulo = new JLabel("Conversa com " + nomeOutraPessoa);
        titulo.setForeground(TEXTO);
        titulo.setFont(new Font("Times New Roman", Font.BOLD, 24));
        cab.add(titulo, BorderLayout.CENTER);
        JButton fechar = botao("Fechar", ROXO);
        fechar.addActionListener(e -> dispose());
        cab.add(fechar, BorderLayout.EAST);
        pagina.add(cab, BorderLayout.NORTH);

        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.setBackground(FUNDO);
        corpo.setBorder(new EmptyBorder(5, 5, 5, 5));
        rolagem = new JScrollPane(corpo,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rolagem.getViewport().setBackground(FUNDO);
        rolagem.setBorder(null);
        rolagem.getVerticalScrollBar().setUnitIncrement(18);
        pagina.add(rolagem, BorderLayout.CENTER);

        JPanel area = new JPanel(new BorderLayout(8, 10));
        area.setOpaque(false);
        escrever.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        escrever.setForeground(TEXTO);
        escrever.setBackground(CARD);
        escrever.setCaretColor(TEXTO);
        escrever.setLineWrap(true);
        escrever.setWrapStyleWord(true);
        escrever.setBorder(new EmptyBorder(10, 12, 10, 12));
        escrever.setToolTipText("Digite sua mensagem. Até 2000 caracteres.");
        area.add(new JScrollPane(escrever,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        JButton enviar = botao("Enviar", CORAL);
        enviar.addActionListener(e -> enviar());
        JPanel lado = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        lado.setOpaque(false);
        lado.add(enviar);
        area.add(lado, BorderLayout.EAST);
        pagina.add(area, BorderLayout.SOUTH);

        setContentPane(pagina);
        atualizar(true);

        atualizacao = new Timer(3500, e -> {
            if (!isDisplayable()) {
                atualizacao.stop();
                return;
            }
            if (!Sessao.ativa() || Sessao.getUsuario().getId() != usuarioId
                    || !dao.podeConversar(conexaoId, usuarioId)) {
                atualizacao.stop();
                dispose();
                return;
            }
            atualizar(false);
        });
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) {
                if (atualizacao != null) atualizacao.stop();
            }
        });
        atualizacao.start();
        setVisible(true);
    }

    private JButton botao(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Times New Roman", Font.BOLD, 17));
        b.setForeground(Color.WHITE);
        b.setBackground(cor);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(11, 17, 11, 17));
        return b;
    }

    private void enviar() {
        if (!Sessao.ativa() || Sessao.getUsuario().getId() != usuarioId) {
            Avisos.erro(this, "Sua sessão terminou. Entre novamente.");
            return;
        }
        String texto = escrever.getText().trim();
        if (texto.isEmpty()) {
            Avisos.informacao(this, "Digite uma mensagem antes de enviar.");
            return;
        }
        if (texto.length() > 2000) {
            Avisos.erro(this, "A mensagem pode ter no máximo 2000 caracteres.");
            return;
        }
        if (dao.enviar(conexaoId, usuarioId, texto)) {
            escrever.setText("");
            atualizar(true);
        } else {
            Avisos.erro(this, "Não foi possível enviar. Confira se a conexão continua ativa.");
        }
    }

    private void atualizar(boolean forcarRolagem) {
        List<Mensagem> mensagens = dao.listar(conexaoId, usuarioId);
        long novoUltimo = mensagens.isEmpty() ? 0 : mensagens.get(mensagens.size() - 1).getId();
        if (novoUltimo == ultimoId && !forcarRolagem) return;
        ultimoId = novoUltimo;
        JScrollBar barra = rolagem.getVerticalScrollBar();
        boolean estavaNoFim = barra.getValue() + barra.getVisibleAmount() >= barra.getMaximum() - 28;
        corpo.removeAll();
        if (mensagens.isEmpty()) {
            JLabel vazio = new JLabel("Vocês estão conectados! Envie a primeira mensagem.");
            vazio.setForeground(SUAVE);
            vazio.setFont(new Font("Times New Roman", Font.PLAIN, 17));
            corpo.add(vazio);
        }
        DateTimeFormatter hora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Mensagem m : mensagens) {
            boolean minha = m.getRemetenteId() == usuarioId;
            JPanel linha = new JPanel(new FlowLayout(minha ? FlowLayout.RIGHT : FlowLayout.LEFT, 9, 6));
            linha.setOpaque(false);
            JPanel bolha = new JPanel(new BorderLayout(0, 5));
            bolha.setBackground(minha ? CORAL : CARD);
            bolha.setBorder(new EmptyBorder(12, 15, 10, 15));
            JTextArea texto = new JTextArea(m.getTexto());
            texto.setFont(new Font("Times New Roman", Font.PLAIN, 17));
            texto.setForeground(TEXTO);
            texto.setBackground(minha ? CORAL : CARD);
            texto.setLineWrap(true);
            texto.setWrapStyleWord(true);
            texto.setEditable(false);
            texto.setFocusable(false);
            texto.setColumns(29);
            texto.setSize(new Dimension(330, Short.MAX_VALUE));
            Dimension alturaNecessaria = texto.getPreferredSize();
            texto.setPreferredSize(new Dimension(330,
                    Math.max(28, alturaNecessaria.height)));
            bolha.add(texto, BorderLayout.CENTER);
            JLabel data = new JLabel(m.getEnviadaEm().format(hora));
            data.setFont(new Font("Times New Roman", Font.PLAIN, 12));
            data.setForeground(TEXTO);
            bolha.add(data, BorderLayout.SOUTH);
            linha.add(bolha);
            linha.setAlignmentX(Component.LEFT_ALIGNMENT);
            linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, linha.getPreferredSize().height));
            corpo.add(linha);
        }
        corpo.revalidate();
        corpo.repaint();
        if (forcarRolagem || estavaNoFim) {
            SwingUtilities.invokeLater(() -> barra.setValue(barra.getMaximum()));
        }
    }
}
