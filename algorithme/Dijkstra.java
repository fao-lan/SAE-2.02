package algorithme;

import graph.interfaces.IGraph;
import graph.interfaces.IShortestPath;
import java.util.*;

public class Dijkstra<T> implements IShortestPath<T> {

	@Override
	public Distances<T> compute(IGraph<T> g, T src, Animator<T> animator) throws IllegalArgumentException {
		// Vérification que le graphe et le sommet source ne sont pas nuls
		if (g == null || src == null) {
			throw new IllegalArgumentException("Graphe ou sommet source null.");
		}

		// Initialisation des structures de données :
		Map<T, Integer> dist = new HashMap<>(); // Stocke la distance minimale de la source à chaque sommet
		Map<T, T> pred = new HashMap<>();       // Stocke le prédécesseur de chaque sommet dans le plus court chemin
		PriorityQueue<T> queue = new PriorityQueue<>(Comparator.comparingInt(dist::get)); // File de priorité pour sélectionner les sommets les plus proches

		// Initialisation de la distance de la source à elle-même et ajout dans la file de priorité
		dist.put(src, 0);       // La distance de la source à elle-même est 0
		pred.put(src, null);    // Aucun prédécesseur pour la source
		queue.add(src);         // Ajout de la source dans la file de priorité
		animator.accept(src, 0); // Animation initiale de la source

		// Boucle principale de l'algorithme de Dijkstra
		while (!queue.isEmpty()) {
			// Extraction du sommet ayant la plus petite distance (priorité la plus élevée)
			T u = queue.poll();

			// Parcours de tous les successeurs (voisins) du sommet courant
			for (IGraph.Arc<T> arc : g.getSucc(u)) {
				// Vérification de la présence d'un arc négatif (non supporté par Dijkstra)
				if (arc.val() < 0) {
					throw new IllegalArgumentException("Arc négatif détecté : " + u + " -> " + arc.dst() + " (" + arc.val() + ")");
				}

				// Récupération du voisin et calcul de la distance alternative
				T v = arc.dst();            // Sommet voisin
				int alt = dist.get(u) + arc.val(); // Distance entre la source et le voisin via u

				// Si la nouvelle distance est plus courte que celle connue
				if (!dist.containsKey(v) || alt < dist.get(v)) {
					dist.put(v, alt);  // Mise à jour de la distance pour v
					pred.put(v, u);    // Mise à jour du prédécesseur de v
					queue.remove(v);   // Retirer l'élément s'il est déjà présent pour mise à jour
					queue.add(v);      // Réinsertion dans la file pour mise à jour de la priorité
					animator.accept(v, alt); // Animation de la mise à jour de la distance pour v
				}
			}
		}

		// Retourne l'objet contenant les distances minimales et les prédécesseurs calculés
		return new Distances<>(dist, pred);
	}
}
