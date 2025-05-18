package graph.test;

import graph.Graph;
import graph.GrapheHHAdj;

import java.util.List;

public class TestGrapheHHAdj {
    public static void main(String[] args) {
        GrapheHHAdj graphe = new GrapheHHAdj();

        // Test 1 : Ajouter sommets
        graphe.ajouterSommet("A");
        graphe.ajouterSommet("B");
        System.out.println("Test 1 - Sommets ajoutés : OK");

        // Test 2 : Ajouter arc simple
        graphe.ajouterArc("A", "B", 5);
        List<Graph.Arc<String>> succA = graphe.getSucc("A");
        assert succA.size() == 1 : "Erreur : arc non ajouté.";
        assert succA.get(0).dst().equals("B") : "Erreur : mauvaise destination.";
        assert succA.get(0).val() == 5 : "Erreur : mauvaise valeur.";
        System.out.println("Test 2 - Ajouter un arc : OK");

        // Test 3 : Ajouter un arc déjà existant -> exception attendue
        try {
            graphe.ajouterArc("A", "B", 10);
            System.out.println("Test 3 - Échec : exception non levée");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 3 - Ajout d'arc existant : OK");
        }

        // Test 4 : Utiliser peupler
        GrapheHHAdj graphe2 = new GrapheHHAdj();
        graphe2.peupler("A-B(2), A-C(4), B-D(1), C-D(3)");
        assert graphe2.getSucc("A").size() == 2 : "Erreur : arcs de A mal ajoutés.";
        assert graphe2.getSucc("B").size() == 1 : "Erreur : arcs de B mal ajoutés.";
        assert graphe2.getSucc("C").get(0).dst().equals("D") : "Erreur : arc C-D manquant.";
        System.out.println("Test 4 - Méthode peupler : OK");

        System.out.println("✅ Tous les tests terminés avec succès.");
    }
}
