package dijkstra;

import graph.Graph;
import graph.ShortestPath;
import java.util.*;

public class Dijkstra<T> implements ShortestPath<T> {

	@Override
	public Distances<T> compute(Graph<T> g, T src, Animator<T> animator) throws IllegalArgumentException {
		if (g == null || src == null) {
			throw new IllegalArgumentException("Graphe ou sommet source null.");
		}

		Map<T, Integer> dist = new HashMap<>();
		Map<T, T> pred = new HashMap<>();
		PriorityQueue<T> queue = new PriorityQueue<>(Comparator.comparingInt(dist::get));

		dist.put(src, 0);
		pred.put(src, null);
		queue.add(src);
		animator.accept(src, 0);

		while (!queue.isEmpty()) {
			T u = queue.poll();

			for (Graph.Arc<T> arc : g.getSucc(u)) {
				if (arc.val() < 0) {
					throw new IllegalArgumentException("Arc négatif détecté : " + u + " -> " + arc.dst() + " (" + arc.val() + ")");
				}

				T v = arc.dst();
				int alt = dist.get(u) + arc.val();

				if (!dist.containsKey(v) || alt < dist.get(v)) {
					dist.put(v, alt);
					pred.put(v, u);
					queue.remove(v);
					queue.add(v);
					animator.accept(v, alt);
				}
			}
		}

		return new Distances<>(dist, pred);
	}
}
