import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Catalogo {

    private ArrayList<Filme> filmes;

    public Catalogo() {

        filmes = new ArrayList<>();

        // ========================================
        // FILMES ORIGINAIS DO CINEMATCH (1 A 10)
        // ========================================

        filmes.add(new Filme(
                "Interestelar",
                "Ficção Científica",
                2014,
                "imagens/Interestelar.jpg",
                "Christopher Nolan"
        ));

        filmes.add(new Filme(
                "Matrix",
                "Ficção Científica",
                1999,
                "imagens/Matrix.jpg",
                "Lana Wachowski e Lilly Wachowski"
        ));

        filmes.add(new Filme(
                "Avatar",
                "Aventura",
                2009,
                "imagens/Avatar.jpg",
                "James Cameron"
        ));

        filmes.add(new Filme(
                "Titanic",
                "Romance",
                1997,
                "imagens/Titanic.jpg",
                "James Cameron"
        ));

        filmes.add(new Filme(
                "Vingadores",
                "Ação",
                2012,
                "imagens/Vingadores.jpg",
                "Joss Whedon"
        ));

        filmes.add(new Filme(
                "Barbie",
                "Comédia",
                2023,
                "imagens/Barbie.jpg",
                "Greta Gerwig"
        ));

        filmes.add(new Filme(
                "Coraline",
                "Fantasia",
                2009,
                "imagens/Coraline.jpg",
                "Henry Selick"
        ));

        filmes.add(new Filme(
                "Divertida Mente",
                "Animação",
                2015,
                "imagens/DivertidaMente.jpg",
                "Pete Docter e Ronnie del Carmen"
        ));

        filmes.add(new Filme(
                "Jurassic Park",
                "Aventura",
                1993,
                "imagens/JurassicPark.jpg",
                "Steven Spielberg"
        ));

        filmes.add(new Filme(
                "A noiva do Chucky",
                "Terror",
                1998,
                "imagens/AnoivadoChucky.jpg",
                "Ronny Yu"
        ));

        // ========================================
        // ROMANCE E COMÉDIA ROMÂNTICA (11 A 15)
        // ========================================

        filmes.add(new Filme(
                "La La Land",
                "Musical",
                2016,
                "imagens/LaLaLand.jpg",
                "Damien Chazelle"
        ));

        filmes.add(new Filme(
                "Diário de uma Paixão",
                "Romance",
                2004,
                "imagens/DiarioDeUmaPaixao.jpg",
                "Nick Cassavetes"
        ));

        filmes.add(new Filme(
                "10 Coisas que Eu Odeio em Você",
                "Comédia Romântica",
                1999,
                "imagens/10CoisasQueEuOdeioEmVoce.jpg",
                "Gil Junger"
        ));

        filmes.add(new Filme(
                "Como Perder um Homem em 10 Dias",
                "Comédia Romântica",
                2003,
                "imagens/ComoPerderUmHomemEm10Dias.jpg",
                "Donald Petrie"
        ));

        filmes.add(new Filme(
                "Questão de Tempo",
                "Romance",
                2013,
                "imagens/QuestaoDeTempo.jpg",
                "Richard Curtis"
        ));

        // ========================================
        // TERROR, SUSPENSE E MISTÉRIO (16 A 20)
        // ========================================

        filmes.add(new Filme(
                "Invocação do Mal",
                "Terror",
                2013,
                "imagens/InvocacaoDoMal.jpg",
                "James Wan"
        ));

        filmes.add(new Filme(
                "Hereditário",
                "Terror",
                2018,
                "imagens/Hereditario.jpg",
                "Ari Aster"
        ));

        filmes.add(new Filme(
                "Corra!",
                "Suspense",
                2017,
                "imagens/Corra.jpg",
                "Jordan Peele"
        ));

        filmes.add(new Filme(
                "Um Lugar Silencioso",
                "Suspense",
                2018,
                "imagens/UmLugarSilencioso.jpg",
                "John Krasinski"
        ));

        filmes.add(new Filme(
                "Ilha do Medo",
                "Mistério",
                2010,
                "imagens/IlhaDoMedo.jpg",
                "Martin Scorsese"
        ));

        // ========================================
        // FICÇÃO CIENTÍFICA E AÇÃO (21 A 25)
        // ========================================

        filmes.add(new Filme(
                "Duna: Parte Dois",
                "Ficção Científica",
                2024,
                "imagens/DunaParteDois.jpg",
                "Denis Villeneuve"
        ));

        filmes.add(new Filme(
                "A Origem",
                "Ficção Científica",
                2010,
                "imagens/AOrigem.jpg",
                "Christopher Nolan"
        ));

        filmes.add(new Filme(
                "Blade Runner 2049",
                "Ficção Científica",
                2017,
                "imagens/BladeRunner2049.jpg",
                "Denis Villeneuve"
        ));

        filmes.add(new Filme(
                "John Wick",
                "Ação",
                2014,
                "imagens/JohnWick.jpg",
                "Chad Stahelski"
        ));

        filmes.add(new Filme(
                "Mad Max: Estrada da Fúria",
                "Ação",
                2015,
                "imagens/MadMaxEstradaDaFuria.jpg",
                "George Miller"
        ));

        // ========================================
        // ANIMAÇÃO E FANTASIA (26 A 30)
        // ========================================

        filmes.add(new Filme(
                "Shrek",
                "Animação",
                2001,
                "imagens/Shrek.jpg",
                "Andrew Adamson e Vicky Jenson"
        ));

        filmes.add(new Filme(
                "Toy Story",
                "Animação",
                1995,
                "imagens/ToyStory.jpg",
                "John Lasseter"
        ));

        filmes.add(new Filme(
                "Viva: A Vida é uma Festa",
                "Animação",
                2017,
                "imagens/VivaAVidaEUmaFesta.jpg",
                "Lee Unkrich e Adrian Molina"
        ));

        filmes.add(new Filme(
                "Homem-Aranha no Aranhaverso",
                "Animação",
                2018,
                "imagens/HomemAranhaNoAranhaverso.jpg",
                "Bob Persichetti, Peter Ramsey e Rodney Rothman"
        ));

        filmes.add(new Filme(
                "A Viagem de Chihiro",
                "Fantasia",
                2001,
                "imagens/AViagemDeChihiro.jpg",
                "Hayao Miyazaki"
        ));

        // ========================================
        // COMÉDIA E AVENTURA (31 A 35)
        // ========================================

        filmes.add(new Filme(
                "As Branquelas",
                "Comédia",
                2004,
                "imagens/AsBranquelas.jpg",
                "Keenen Ivory Wayans"
        ));

        filmes.add(new Filme(
                "Gente Grande",
                "Comédia",
                2010,
                "imagens/GenteGrande.jpg",
                "Dennis Dugan"
        ));

        filmes.add(new Filme(
                "Se Beber, Não Case!",
                "Comédia",
                2009,
                "imagens/SeBeberNaoCase.jpg",
                "Todd Phillips"
        ));

        filmes.add(new Filme(
                "Piratas do Caribe: A Maldição do Pérola Negra",
                "Aventura",
                2003,
                "imagens/PiratasDoCaribe.jpg",
                "Gore Verbinski"
        ));

        filmes.add(new Filme(
                "Jumanji: Bem-Vindo à Selva",
                "Aventura",
                2017,
                "imagens/Jumanji.jpg",
                "Jake Kasdan"
        ));

        // ========================================
        // DRAMA, CRIME E BIOGRAFIA (36 A 40)
        // ========================================

        filmes.add(new Filme(
                "Forrest Gump",
                "Drama",
                1994,
                "imagens/ForrestGump.jpg",
                "Robert Zemeckis"
        ));

        filmes.add(new Filme(
                "À Procura da Felicidade",
                "Drama",
                2006,
                "imagens/AProcuraDaFelicidade.jpg",
                "Gabriele Muccino"
        ));

        filmes.add(new Filme(
                "O Poderoso Chefão",
                "Crime",
                1972,
                "imagens/OPoderosoChefao.jpg",
                "Francis Ford Coppola"
        ));

        filmes.add(new Filme(
                "Um Sonho de Liberdade",
                "Drama",
                1994,
                "imagens/UmSonhoDeLiberdade.jpg",
                "Frank Darabont"
        ));

        filmes.add(new Filme(
                "Oppenheimer",
                "Biografia",
                2023,
                "imagens/Oppenheimer.jpg",
                "Christopher Nolan"
        ));

        // ========================================
        // CINEMA NACIONAL (41 A 45)
        // ========================================

        filmes.add(new Filme(
                "Central do Brasil",
                "Cinema Nacional",
                1998,
                "imagens/CentralDoBrasil.jpg",
                "Walter Salles"
        ));

        filmes.add(new Filme(
                "Cidade de Deus",
                "Cinema Nacional",
                2002,
                "imagens/CidadeDeDeus.jpg",
                "Fernando Meirelles e Kátia Lund"
        ));

        filmes.add(new Filme(
                "O Auto da Compadecida",
                "Cinema Nacional",
                2000,
                "imagens/OAutoDaCompadecida.jpg",
                "Guel Arraes"
        ));

        filmes.add(new Filme(
                "Bacurau",
                "Cinema Nacional",
                2019,
                "imagens/Bacurau.jpg",
                "Kleber Mendonça Filho e Juliano Dornelles"
        ));

        filmes.add(new Filme(
                "Que Horas Ela Volta?",
                "Cinema Nacional",
                2015,
                "imagens/QueHorasElaVolta.jpg",
                "Anna Muylaert"
        ));

        // ========================================
        // MUSICAL, FAMÍLIA E DOCUMENTÁRIO (46 A 50)
        // ========================================

        filmes.add(new Filme(
                "Wicked",
                "Musical",
                2024,
                "imagens/Wicked.jpg",
                "Jon M. Chu"
        ));

        filmes.add(new Filme(
                "Mamma Mia!",
                "Musical",
                2008,
                "imagens/MammaMia.jpg",
                "Phyllida Lloyd"
        ));

        filmes.add(new Filme(
                "O Rei Leão",
                "Família",
                1994,
                "imagens/OReiLeao.jpg",
                "Roger Allers e Rob Minkoff"
        ));

        filmes.add(new Filme(
                "A Marcha dos Pinguins",
                "Documentário",
                2005,
                "imagens/AMarchaDosPinguins.jpg",
                "Luc Jacquet"
        ));

        filmes.add(new Filme(
                "Amy",
                "Documentário",
                2015,
                "imagens/Amy.jpg",
                "Asif Kapadia"
        ));
        // Trilogia Batman de Christopher Nolan (51 a 53).
        filmes.add(new Filme(
                "Batman Begins", "Ação", 2005,
                "imagens/Batman_Begins.jpg", "Christopher Nolan"
        ));
        filmes.add(new Filme(
                "Batman: O Cavaleiro das Trevas", "Ação", 2008,
                "imagens/Batman_O_Cavaleiro_das_Trevas.jpg", "Christopher Nolan"
        ));
        filmes.add(new Filme(
                "Batman: O Cavaleiro das Trevas Ressurge", "Ação", 2012,
                "imagens/Batman_O_Cavaleiro_das_Trevas_Ressurge.jpg", "Christopher Nolan"
        ));

    }

    // ========================================
    // MÉTODOS EXISTENTES DO CATÁLOGO
    // ========================================

    // Retorna todos os filmes
    public ArrayList<Filme> getFilmes() {
        return new ArrayList<>(filmes);
    }

    // Adiciona um filme, evitando duplicações
    public void adicionarFilme(Filme filme) {

        if (filme != null && !filmes.contains(filme)) {
            filmes.add(filme);
        }
    }

    // Busca um filme pelo título
    public Filme buscarPorTitulo(String titulo) {

        for (Filme filme : filmes) {

            if (filme.getTitulo().equalsIgnoreCase(titulo)) {
                return filme;
            }
        }

        return null;
    }

    // Busca os filmes de um gênero
    public List<Filme> buscarPorGenero(String genero) {

        List<Filme> resultado = new ArrayList<>();

        for (Filme filme : filmes) {

            if (filme.getGenero().equalsIgnoreCase(genero)) {
                resultado.add(filme);
            }
        }

        return resultado;
    }

    // Organiza os filmes em ordem alfabética
    public List<Filme> getFilmesOrdenadosPorTitulo() {

        List<Filme> listaOrdenada =
                new ArrayList<>(filmes);

        Collections.sort(listaOrdenada);

        return listaOrdenada;
    }
}
