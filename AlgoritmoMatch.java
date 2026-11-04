import java.util.*;

public class AlgoritmoMatch {
    public static class Resultado {
        public final Usuario usuario;
        public final int percentual, filmesEmComum, generosEmComum;
        public final String motivo;
        Resultado(Usuario u,int p,int f,int g,String m) {
            usuario=u;percentual=p;filmesEmComum=f;generosEmComum=g;motivo=m;
        }
    }
    public static Resultado calcular(Usuario a, Usuario b, Set<Integer> filmesA, Set<Integer> filmesB) {
        if(a==null||b==null||a.getId()==b.getId()||!a.maiorDeIdade()||!b.maiorDeIdade() ||
                !a.isPermitirSugestoes()||!b.isPermitirSugestoes())return null;
        boolean amizade=incluiAmizade(a.getIntencao())&&incluiAmizade(b.getIntencao());
        boolean namoro=incluiRelacionamento(a.getIntencao())&&incluiRelacionamento(b.getIntencao())
                && interesseCompativel(a,b)&&interesseCompativel(b,a);
        if(!amizade&&!namoro)return null;

        Set<Integer> comumFilmes=new HashSet<>(filmesA);comumFilmes.retainAll(filmesB);
        if(comumFilmes.isEmpty())return null;
        Set<Integer> uniaoFilmes=new HashSet<>(filmesA);uniaoFilmes.addAll(filmesB);

        Set<String> genA=generos(a),genB=generos(b);
        Set<String> genComuns=new HashSet<>(genA);genComuns.retainAll(genB);
        Set<String> genUniao=new HashSet<>(genA);genUniao.addAll(genB);

        double filmes=100.0*comumFilmes.size()/Math.max(1,uniaoFilmes.size());
        double generos=genUniao.isEmpty()?0:100.0*genComuns.size()/genUniao.size();
        int pontuacao=(int)Math.round(0.70*filmes+0.30*generos);
        String tipo=(amizade&&namoro)?"Amizade ou relacionamento":(amizade?"Amizade":"Relacionamento");
        return new Resultado(b,pontuacao,comumFilmes.size(),genComuns.size(),tipo);
    }
    private static boolean incluiAmizade(String v){
        return v==null||v.contains("Amizade");
    }
    private static boolean incluiRelacionamento(String v){
        return v!=null&&v.contains("relacionamento")||"Relacionamento".equals(v);
    }
    private static Set<String> generos(Usuario u) {
        Set<String> s=new HashSet<>();
        if(u.getGenerosFavoritos()!=null)
            for(String g:u.getGenerosFavoritos().split("\\|"))
                if(!g.isBlank())s.add(g.trim().toLowerCase(java.util.Locale.ROOT));
        return s;
    }
    private static boolean interesseCompativel(Usuario interessado,Usuario candidato){
        String preferencia=interessado.getInteresse();
        if(preferencia==null||preferencia.equals("Sem preferência")||
            preferencia.equals("Todos os gêneros")||preferencia.equals("Prefiro não informar"))return true;
        String identidade=candidato.getIdentidadeGenero();
        if(identidade==null||identidade.equals("Prefiro não informar")||identidade.equals("Outra identidade"))return false;
        if(preferencia.equals("Homens"))return identidade.equals("Homem");
        if(preferencia.equals("Mulheres"))return identidade.equals("Mulher");
        if(preferencia.equals("Homens e mulheres"))
            return identidade.equals("Homem")||identidade.equals("Mulher");
        return true;
    }
}
