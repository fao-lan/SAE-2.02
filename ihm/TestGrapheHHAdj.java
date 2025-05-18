package ihm;

import graph.interfaces.IGraph;
import graph.GrapheHHAdj;

import java.util.List;

public class TestGrapheHHAdj {
    public static void main(String[] args) {
        GrapheHHAdj graphe = new GrapheHHAdj();

        // Test 1 : Ajouter des sommets
        graphe.ajouterSommet("A");
        graphe.ajouterSommet("B");
        System.out.println("Test 1 - Sommets ajoutés : OK");

        // Test 2 : Ajouter un arc simple entre deux sommets
        graphe.ajouterArc("A", "B", 5);
        List<IGraph.Arc<String>> succA = graphe.getSucc("A");

        // Vérification de l'ajout de l'arc
        assert succA.size() == 1 : "Erreur : arc non ajouté.";
        assert succA.get(0).dst().equals("B") : "Erreur : mauvaise destination.";
        assert succA.get(0).val() == 5 : "Erreur : mauvaise valeur.";
        System.out.println("Test 2 - Ajouter un arc : OK");

        // Test 3 : Tenter d'ajouter un arc déjà existant (doit lever une exception)
        try {
            graphe.ajouterArc("A", "B", 10); // L'arc existe déjà, une exception doit être levée
            System.out.println("Test 3 - Échec : exception non levée");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 3 - Ajout d'arc existant : OK");
        }

        // Test 4 : Utiliser la méthode peupler pour ajouter plusieurs arcs à la fois
        GrapheHHAdj graphe2 = new GrapheHHAdj();
        graphe2.peupler("A-B(2), A-C(4), B-D(1), C-D(3)");

        // Vérification des successeurs de chaque sommet
        assert graphe2.getSucc("A").size() == 2 : "Erreur : arcs de A mal ajoutés.";
        assert graphe2.getSucc("B").size() == 1 : "Erreur : arcs de B mal ajoutés.";
        assert graphe2.getSucc("C").get(0).dst().equals("D") : "Erreur : arc C-D manquant.";
        System.out.println("Test 4 - Méthode peupler : OK");

        // Test 5 : Vérifier qu'un sommet sans successeurs ne provoque pas d'erreur
        graphe2.ajouterSommet("E");
        assert graphe2.getSucc("E").isEmpty() : "Erreur : sommet E devrait être sans successeurs.";
        System.out.println("Test 5 - Sommet sans successeurs : OK");

        // Test 6 : Ajouter un arc vers un nouveau sommet (doit ajouter le sommet automatiquement)
        graphe2.ajouterArc("D", "E", 7);
        assert graphe2.getSucc("D").size() == 1 : "Erreur : arc D-E non ajouté.";
        assert graphe2.getSucc("D").get(0).dst().equals("E") : "Erreur : mauvaise destination pour l'arc D-E.";
        System.out.println("Test 6 - Ajouter un arc vers un nouveau sommet : OK");

        // Test 7 : Utiliser peupler avec un format incorrect (doit lever une exception)
        try {
            graphe2.peupler("X-Y(), Z-W(abc)");
            System.out.println("Test 7 - Échec : exception non levée pour format incorrect");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 7 - Format incorrect détecté dans peupler : OK");
        }


    }
}
