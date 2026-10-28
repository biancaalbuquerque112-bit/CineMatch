
import java.util.ArrayList;

public class Catalogo {

    private ArrayList<Filme> filmes;

    public Catalogo() {

        filmes = new ArrayList<>();

        filmes.add(new Filme(
            "Interestelar", "Ficcao Cientifica", 2014,
            "imagens/Interestelar.jpg"
        ));

        filmes.add(new Filme(
            "Matrix", "Ficcao Cientifica", 1999,
            "imagens/matrix.jpg"
        ));

        filmes.add(new Filme(
            "Avatar", "Aventura", 2009,
            "imagens/Avatar.jpg"
        ));

        filmes.add(new Filme(
            "Titanic", "Romance", 1997,
            "imagens/Titanic.jpg"
        ));

        filmes.add(new Filme(
            "Vingadores", "Acao", 2012,
            "imagens/Vingadores.jpg"
        ));

        filmes.add(new Filme(
            "Barbie", "Comedia", 2023,
            "imagens/Barbie.jpg"
        ));

        filmes.add(new Filme(
            "Coraline", "Fantasia", 2009,
            "imagens/Coraline.jpg"
        ));

        filmes.add(new Filme(
            "Divertida Mente", "Animacao", 2015,
            "imagens/DivertidaMente.jpg"
        ));

        filmes.add(new Filme(
            "Jurassic Park", "Aventura", 1993,
            "imagens/JurassicPark.jpg"
        ));

        filmes.add(new Filme(
            "A noiva do Chucky", "Terror/Comedia", 1998,
            "imagens/AnoivadoChucky.jpg"
        ));
    }

    public ArrayList<Filme> getFilmes() {
        return new ArrayList<>(filmes);
    }
}
