
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TelaCatalogo extends JFrame {

    private final Color FUNDO = new Color(18, 18, 30);
    private final Color CARD = new Color(32, 32, 48);
    private final Color ROSA = new Color(255, 75, 100);

    private ArrayList<Filme> filmes;
    private JPanel painelFilmes;
    private JTextField campoPesquisa;

    public TelaCatalogo() {

        setTitle("CineMatch - Catalogo");
        setSize(1100, 750);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Catalogo catalogo = new Catalogo();
        filmes = catalogo.getFilmes();

        JPanel principal = new JPanel(
            new BorderLayout(15, 15)
        );

        principal.setBackground(FUNDO);
        principal.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JPanel cabecalho = new JPanel(
            new BorderLayout(15, 10)
        );
        cabecalho.setBackground(FUNDO);

        JButton voltar = criarBotao(
            "← Voltar", new Color(65, 65, 80)
        );

        voltar.addActionListener(e -> {
            new TelaInicial();
            dispose();
        });

        JLabel titulo = new JLabel("CineMatch - Filmes");
        titulo.setFont(
            new Font("Arial", Font.BOLD, 28)
        );
        titulo.setForeground(ROSA);

        JPanel esquerda = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 15, 0)
        );
        esquerda.setBackground(FUNDO);
        esquerda.add(voltar);
        esquerda.add(titulo);

        campoPesquisa = new JTextField(18);
        campoPesquisa.setFont(
            new Font("Arial", Font.PLAIN, 16)
        );
        campoPesquisa.setToolTipText(
            "Pesquise por titulo ou genero"
        );

        cabecalho.add(esquerda, BorderLayout.WEST);
        cabecalho.add(campoPesquisa, BorderLayout.EAST);

        principal.add(cabecalho, BorderLayout.NORTH);

        painelFilmes = new JPanel(
            new GridLayout(0, 3, 18, 18)
        );
        painelFilmes.setBackground(FUNDO);

        JScrollPane rolagem = new JScrollPane(
            painelFilmes
        );
        rolagem.setBorder(null);
        rolagem.getVerticalScrollBar()
                .setUnitIncrement(18);

        principal.add(rolagem, BorderLayout.CENTER);

        add(principal);

        mostrarFilmes("");

        campoPesquisa.getDocument().addDocumentListener(
            new DocumentListener() {

                public void insertUpdate(DocumentEvent e) {
                    pesquisar();
                }

                public void removeUpdate(DocumentEvent e) {
                    pesquisar();
                }

                public void changedUpdate(DocumentEvent e) {
                    pesquisar();
                }
            }
        );

        setVisible(true);
    }

    private JButton criarBotao(
            String texto, Color cor) {

        JButton botao = new JButton(texto);

        botao.setFont(
            new Font("Arial", Font.BOLD, 13)
        );
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        return botao;
    }

    private void pesquisar() {
        mostrarFilmes(campoPesquisa.getText());
    }

    private void mostrarFilmes(String pesquisa) {

        painelFilmes.removeAll();

        String termo = pesquisa.toLowerCase().trim();

        for (Filme filme : filmes) {

            if (filme.getTitulo().toLowerCase()
                    .contains(termo)
                    || filme.getGenero().toLowerCase()
                    .contains(termo)) {

                painelFilmes.add(criarCard(filme));
            }
        }

        painelFilmes.revalidate();
        painelFilmes.repaint();
    }

    private JLabel criarImagem(Filme filme) {

        JLabel imagem = new JLabel();
        imagem.setHorizontalAlignment(
            SwingConstants.CENTER
        );
        imagem.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        File arquivo = new File(
            "src/" + filme.getCapa()
        );

        if (arquivo.exists()) {

            ImageIcon original = new ImageIcon(
                arquivo.getAbsolutePath()
            );

            Image redimensionada = original.getImage()
                .getScaledInstance(
                    170, 235, Image.SCALE_SMOOTH
                );

            imagem.setIcon(
                new ImageIcon(redimensionada)
            );

        } else {

            imagem.setText("SEM CAPA");
            imagem.setForeground(Color.LIGHT_GRAY);
            imagem.setFont(
                new Font("Arial", Font.BOLD, 16)
            );
        }

        imagem.setPreferredSize(
            new Dimension(180, 240)
        );

        return imagem;
    }

    private JPanel criarCard(Filme filme) {

        JPanel card = new JPanel();
        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );
        card.setBackground(CARD);
        card.setBorder(
            BorderFactory.createEmptyBorder(
                15, 12, 15, 12
            )
        );

        JLabel imagem = criarImagem(filme);

        JLabel titulo = new JLabel(
            filme.getTitulo()
        );
        titulo.setFont(
            new Font("Arial", Font.BOLD, 18)
        );
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel genero = new JLabel(
            filme.getGenero() + " | " + filme.getAno()
        );
        genero.setForeground(Color.LIGHT_GRAY);
        genero.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel curtidas = new JLabel(
            "Curtidas: " + filme.getCurtidas()
        );
        curtidas.setForeground(Color.WHITE);
        curtidas.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel nota = new JLabel(
            filme.getNota() == 0
                ? "Sem avaliacao"
                : "Nota: " + filme.getNota() + "/5"
        );
        nota.setForeground(Color.WHITE);
        nota.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JButton gostar = criarBotao(
            "♥ Gostei", ROSA
        );

        JButton rejeitar = criarBotao(
            "Não gostei", new Color(65, 65, 80)
        );

        JButton avaliar = criarBotao(
            "★ Avaliar", new Color(65, 65, 80)
        );

        gostar.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );
        rejeitar.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );
        avaliar.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        gostar.addActionListener(e -> {
            filme.curtir();
            curtidas.setText(
                "Curtidas: " + filme.getCurtidas()
            );
        });

        rejeitar.addActionListener(e -> {
            filme.rejeitar();

            JOptionPane.showMessageDialog(
                this, "Preferencia registrada!"
            );
        });

        avaliar.addActionListener(e -> {

            String[] opcoes = {
                "1", "2", "3", "4", "5"
            };

            String escolha = (String)
                JOptionPane.showInputDialog(
                    this,
                    "Qual nota voce daria?",
                    "Avaliar filme",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[4]
                );

            if (escolha != null) {

                filme.avaliar(
                    Integer.parseInt(escolha)
                );

                nota.setText(
                    "Nota: " + filme.getNota() + "/5"
                );
            }
        });

        card.add(imagem);
        card.add(Box.createVerticalStrut(12));
        card.add(titulo);
        card.add(Box.createVerticalStrut(6));
        card.add(genero);
        card.add(Box.createVerticalStrut(12));
        card.add(curtidas);
        card.add(nota);
        card.add(Box.createVerticalStrut(12));
        card.add(gostar);
        card.add(Box.createVerticalStrut(6));
        card.add(rejeitar);
        card.add(Box.createVerticalStrut(6));
        card.add(avaliar);

        return card;
    }
}
