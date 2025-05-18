package ihm;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import graph.interfaces.IGraph;
import graph.GrapheHHAdj;
import graph.interfaces.IShortestPath.Distances;
import graph.interfaces.IVarGraph;
import org.junit.jupiter.api.Test;

import algorithme.Dijkstra;

public class DijkstraTest {
	// Définition de graphes pour les tests
	private static final String GRAPH1 = "A-B(6), A-C(1), A-D(2), B-E(1), C-E(4), D-B(1), E-F(1)";
	private static final String GRAPH_NEG = "A-B(6), A-C(1), A-D(2), B-E(-3), C-E(4), D-B(1), E-F(1)"; // Graphe avec un arc négatif
	private static final String FROM = "A"; // Sommet de départ pour les tests
	private static final String TO = "F";   // Sommet de destination pour les tests
	private static final int EXPECTED_DIST = 5; // Distance attendue entre A et F
	private static final List<String> EXPECTED_PATH = List.of("F", "E", "B", "D", "A"); // Chemin attendu dans l'ordre inverse
	private static final Dijkstra<String> dijkstra = new Dijkstra<>(); // Instance de l'algorithme de Dijkstra

	@Test
	void test() {
		// Test avec un graphe standard sans arcs négatifs
		IVarGraph g = new GrapheHHAdj();
		g.peupler(GRAPH1);
		tester(g);
	}

	// Méthode de test générique pour vérifier la distance et le chemin
	void tester(IGraph<String> g) {
		Distances<String> dst = dijkstra.compute(g, FROM);
		// Vérification de la distance
		assertEquals(EXPECTED_DIST, dst.dist().get(TO));

		// Vérification du chemin
		String c = EXPECTED_PATH.get(0);
		for (String s : EXPECTED_PATH) {
			assertEquals(s, c);
			c = dst.pred().get(c);
		}
		assertNull(c); // Le dernier prédécesseur doit être nul (fin du chemin)
	}

	@Test
	void pasDeValuationNegative() {
		// Test avec un graphe contenant un arc négatif (doit lever une exception)
		IVarGraph g = new GrapheHHAdj();
		g.peupler(GRAPH_NEG);
		assertThrows(IllegalArgumentException.class,
				() -> dijkstra.compute(g, FROM));
	}

	@Test
	void utilisationDuResultat() {
		// Test pour afficher les résultats du calcul de Dijkstra
		IVarGraph g = new GrapheHHAdj();
		g.peupler(GRAPH1);
		Distances<String> dst = dijkstra.compute(g, FROM);

		// Affichage des informations de distance et de prédécesseur
		System.out.println("Graphe : " + g);
		System.out.println("Distances de A : " + dst.dist());
		System.out.println("Prédecesseurs : " + dst.pred());
		System.out.println("Distance de " + FROM + " à " + TO + " : " + dst.dist().get(TO));

		// Affichage du chemin de A à F
		System.out.print("Chemin de " + FROM + " à " + TO + " : ");
		String sommet = TO;
		Deque<String> pile = new ArrayDeque<>();

		// Construction du chemin en remontant les prédécesseurs
		while (sommet != null) {
			pile.push(sommet);
			sommet = dst.pred().get(sommet);
		}

		// Affichage du chemin dans le bon ordre
		while (!pile.isEmpty()) {
			System.out.print(pile.pop() + " ");
		}
		System.out.println();
	}

	// Test pour un graphe avec un seul sommet (boucle sur lui-même)
	@Test
	void grapheUnSeulSommet() {
		IVarGraph g = new GrapheHHAdj();
		g.ajouterArc("A", "A", 0); // Boucle sur soi-même

		Distances<String> dst = dijkstra.compute(g, "A");
		assertEquals(0, dst.dist().get("A"));
		assertNull(dst.pred().get("A"));
	}

	// Test pour un graphe avec un cycle
	@Test
	void grapheAvecCycle() {
		IVarGraph g = new GrapheHHAdj();
		g.peupler("A-B(1), B-C(1), C-A(1), C-D(2)");

		Distances<String> dst = dijkstra.compute(g, "A");

		// Vérification des distances attendues
		assertEquals(0, dst.dist().get("A"));
		assertEquals(1, dst.dist().get("B"));
		assertEquals(2, dst.dist().get("C"));
		assertEquals(4, dst.dist().get("D"));
	}

	// Test pour un graphe avec des sommets inaccessibles
	@Test
	void sommetInaccessible() {
		IVarGraph g = new GrapheHHAdj();
		g.peupler("A-B(1), B-C(2), D-E(3)"); // D et E sont inaccessibles depuis A

		Distances<String> dst = dijkstra.compute(g, "A");

		// Les sommets inaccessibles doivent avoir des distances nulles
		assertNull(dst.dist().get("D"));
		assertNull(dst.dist().get("E"));
		assertNull(dst.pred().get("D"));
		assertNull(dst.pred().get("E"));
	}

	// Test pour un graphe en étoile (C connecté à 100 sommets)
	@Test
	void grapheEnEtoile() {
		IVarGraph g = new GrapheHHAdj();
		StringBuilder sb = new StringBuilder();

		// Création du graphe en étoile avec 100 sommets connectés à C
		for (int i = 1; i <= 100; i++) {
			sb.append("C-").append(i).append("(1), ");
		}
		g.peupler(sb.toString());

		Distances<String> dst = dijkstra.compute(g, "C");

		// Vérification que chaque sommet est à distance 1 de C
		for (int i = 1; i <= 100; i++) {
			assertEquals(1, dst.dist().get(String.valueOf(i)));
			assertEquals("C", dst.pred().get(String.valueOf(i)));
		}
	}
}
