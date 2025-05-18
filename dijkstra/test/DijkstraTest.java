package dijkstra.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import graph.Graph;
import graph.GrapheHHAdj;
import graph.ShortestPath.Distances;
import graph.VarGraph;
import org.junit.jupiter.api.Test;

import dijkstra.Dijkstra;

class DijkstraTest {
	private static final String GRAPH1 = "A-B(6), A-C(1), A-D(2), B-E(1), C-E(4), D-B(1), E-F(1)";
	private static final String GRAPH_NEG = "A-B(6), A-C(1), A-D(2), B-E(-3), C-E(4), D-B(1), E-F(1)"; // B-E negatif !
	private static final String FROM = "A";
	private static final String TO = "F";
	private static final int EXPECTED_DIST = 5;
	private static final List<String> EXPECTED_PATH = List.of("F", "E", "B", "D", "A"); // in pred order
	private static final Dijkstra<String> dijkstra = new Dijkstra<>();

	@Test
	void test() {
		VarGraph g = new GrapheHHAdj();
		g.peupler(GRAPH1);
		tester(g);
	}

	void tester(Graph<String> g) {
		Distances<String> dst = dijkstra.compute(g, FROM);
		assertEquals(EXPECTED_DIST, dst.dist().get(TO));
		String c = EXPECTED_PATH.get(0);
		for (String s : EXPECTED_PATH) {
			assertEquals(s, c);
			c = dst.pred().get(c);
		}
		assertNull(c);
	}

	@Test
	void pasDeValuationNegative() {
		VarGraph g = new GrapheHHAdj();
		g.peupler(GRAPH_NEG);
		assertThrows(IllegalArgumentException.class,
				()->  dijkstra.compute(g, FROM));
	}
	@Test
	void utilisationDuResultat() {
		 VarGraph g = new GrapheHHAdj();
		 g.peupler(GRAPH1);
		 Distances<String> dst = dijkstra.compute(g, FROM);
		 System.out.println("Graphe : " + g);
		 System.out.println("Distances de A : " + dst.dist());
		 System.out.println("Predecesseurs : " + dst.pred());
		 System.out.println("Distance de " + FROM + " à " + TO + " : " + dst.dist().get(TO));
		 System.out.print("Chemin de " + FROM + " à " + TO + " : ");
		 String sommet = TO;
		 Deque<String> pile = new ArrayDeque<>();
		 while (sommet != null) {
			 pile.push(sommet);
			 sommet = dst.pred().get(sommet);
		 }
		while(!pile.isEmpty()) {
			System.out.print(pile.pop() + " ");
		}
		 System.out.println();
	}

	//Other tests
	@Test
	void grapheUnSeulSommet() {
		VarGraph g = new GrapheHHAdj();
		g.ajouterArc("A", "A", 0); // boucle sur soi-même

		Distances<String> dst = dijkstra.compute(g, "A");
		assertEquals(0, dst.dist().get("A"));
		assertNull(dst.pred().get("A"));
	}

	@Test
	void grapheAvecCycle() {
		VarGraph g = new GrapheHHAdj();
		g.peupler("A-B(1), B-C(1), C-A(1), C-D(2)");

		Distances<String> dst = dijkstra.compute(g, "A");

		assertEquals(0, dst.dist().get("A"));
		assertEquals(1, dst.dist().get("B"));
		assertEquals(2, dst.dist().get("C"));
		assertEquals(4, dst.dist().get("D"));
	}

	@Test
	void sommetInaccessible() {
		VarGraph g = new GrapheHHAdj();
		g.peupler("A-B(1), B-C(2), D-E(3)");

		Distances<String> dst = dijkstra.compute(g, "A");

		assertNull(dst.dist().get("D"));
		assertNull(dst.dist().get("E"));
		assertNull(dst.pred().get("D"));
		assertNull(dst.pred().get("E"));
	}

	@Test
	void grapheEnEtoile() {
		VarGraph g = new GrapheHHAdj();
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= 100; i++) {
			sb.append("C-").append(i).append("(1), ");
		}
		g.peupler(sb.toString());

		Distances<String> dst = dijkstra.compute(g, "C");

		for (int i = 1; i <= 100; i++) {
			assertEquals(1, dst.dist().get(String.valueOf(i)));
			assertEquals("C", dst.pred().get(String.valueOf(i)));
		}
	}
}
