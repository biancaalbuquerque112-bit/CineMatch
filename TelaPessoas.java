import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaPessoas extends JFrame {
    public TelaPessoas(Filme filme,TelaDescobrir anterior) {
        setTitle("CineMatch - Pessoas que curtiram "+filme.getTitulo());
        setSize(700,600);setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel raiz=new JPanel(new BorderLayout(10,10));
        raiz.setBackground(new Color(43,22,70));
        raiz.setBorder(new EmptyBorder(20,22,20,22));
        JLabel titulo=new JLabel("Pessoas que curtiram "+filme.getTitulo());
        titulo.setForeground(new Color(247,243,250));
        titulo.setFont(new Font("Times New Roman",Font.BOLD,24));
        raiz.add(titulo,BorderLayout.NORTH);
        JPanel pessoas=new JPanel();pessoas.setLayout(new BoxLayout(pessoas,BoxLayout.Y_AXIS));
        pessoas.setBackground(new Color(43,22,70));
        if(Sessao.ativa()) {
            for(AlgoritmoMatch.Resultado r:new MatchDAO().sugerir(
                    Sessao.getUsuario().getId(),filme.getId())){
                JButton ver=new JButton(r.usuario.getNome()+" • "+r.percentual+"% • Ver perfil");
                ver.setAlignmentX(Component.LEFT_ALIGNMENT);
                ver.addActionListener(e->new TelaPerfilPessoa(r.usuario,filme,r));
                pessoas.add(ver);pessoas.add(Box.createVerticalStrut(10));
            }
        }
        if(pessoas.getComponentCount()==0){
            JLabel vazio=new JLabel("Ainda não há matches reais neste filme.");
            vazio.setForeground(Color.WHITE);pessoas.add(vazio);
        }
        JScrollPane rolagem=new JScrollPane(pessoas);rolagem.setBorder(null);
        raiz.add(rolagem,BorderLayout.CENTER);
        JButton fechar=new JButton("Voltar");fechar.addActionListener(e->dispose());
        raiz.add(fechar,BorderLayout.SOUTH);add(raiz);setVisible(true);
    }
}
