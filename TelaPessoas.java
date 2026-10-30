
import java.awt.*;
import java.io.File;
import javax.swing.*;

public class TelaPessoas extends JFrame {

    private final Color FUNDO = new Color(18, 18, 30);
    private final Color ROSA = new Color(255, 75, 100);

    private TelaDescobrir telaAnterior;

    public TelaPessoas(Filme filme, TelaDescobrir anterior) {

        this.telaAnterior = anterior;

        setTitle("CineMatch - Pessoas");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(20, 20));
        principal.setBackground(FUNDO);
        principal.setBorder(
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        JLabel titulo = new JLabel(
            "Pessoas que também curtiram " + filme.getTitulo()
        );
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(ROSA);

        principal.add(titulo, BorderLayout.NORTH);

        JPanel conteudo = new JPanel(new BorderLayout(20, 0));
        conteudo.setBackground(FUNDO);

        JLabel capa = new JLabel();
        capa.setHorizontalAlignment(SwingConstants.CENTER);

        File arquivo = new File("src/" + filme.getCapa());

        if (arquivo.exists()) {
            ImageIcon original = new ImageIcon(
                arquivo.getAbsolutePath()
            );

            if (original.getIconWidth() > 0) {
                Image imagem = original.getImage()
                    .getScaledInstance(
                        210, 300, Image.SCALE_SMOOTH
                    );

                capa.setIcon(new ImageIcon(imagem));
            }
        }

        if (capa.getIcon() == null) {
            capa.setText("Sem capa");
            capa.setForeground(Color.WHITE);
        }

        conteudo.add(capa, BorderLayout.WEST);

        JPanel listaPessoas = new JPanel();
        listaPessoas.setBackground(FUNDO);
        listaPessoas.setLayout(
            new BoxLayout(listaPessoas, BoxLayout.Y_AXIS)
        );

        listaPessoas.add(criarPessoa(
            "Marina, 25", "Amizades e relacionamento"
        ));

        listaPessoas.add(criarPessoa(
            "Lucas, 27", "Amizades"
        ));

        listaPessoas.add(criarPessoa(
            "Camila, 24", "Relacionamento"
        ));

        listaPessoas.add(criarPessoa(
            "Rafael, 26", "Amizades e relacionamento"
        ));

        JScrollPane rolagem = new JScrollPane(listaPessoas);
        rolagem.setBorder(null);

        conteudo.add(rolagem, BorderLayout.CENTER);
        principal.add(conteudo, BorderLayout.CENTER);

        JButton continuar = new JButton(
            "Continuar descobrindo filmes"
        );
        continuar.setBackground(ROSA);
        continuar.setForeground(Color.WHITE);
        continuar.setFont(new Font("Arial", Font.BOLD, 15));

        continuar.addActionListener(e -> voltar());

        principal.add(continuar, BorderLayout.SOUTH);

        add(principal);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(
                    java.awt.event.WindowEvent e) {
                telaAnterior.voltarParaFilmes();
            }
        });

        setVisible(true);
    }

    private JPanel criarPessoa(
            String nome, String interesse) {

        JPanel painel = new JPanel(new BorderLayout(10, 5));
        painel.setBackground(new Color(35, 35, 50));
        painel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
        painel.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 100)
        );

        JLabel dados = new JLabel(
            "<html><b>" + nome + "</b><br>"
            + interesse + "</html>"
        );
        dados.setForeground(Color.WHITE);
        dados.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton conectar = new JButton("Conectar");
        conectar.setBackground(ROSA);
        conectar.setForeground(Color.WHITE);

        conectar.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                this,
                "Solicitação de conexão com "
                + nome + " (simulação)."
            );
        });

        painel.add(dados, BorderLayout.CENTER);
        painel.add(conectar, BorderLayout.EAST);

        JPanel espaco = new JPanel(new BorderLayout());
        espaco.setBackground(FUNDO);
        espaco.setBorder(
            BorderFactory.createEmptyBorder(0, 0, 10, 0)
        );
        espaco.add(painel);

        return espaco;
    }

    private void voltar() {
        dispose();
    }
}
