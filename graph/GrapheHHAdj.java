package graph;

import graph.interfaces.IVarGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrapheHHAdj implements IVarGraph {

	// Map pour stocker les successeurs de chaque sommet
	private Map<String, List<Arc<String>>> adj;

	// Constructeur qui initialise la map de successeurs
	public GrapheHHAdj() {
		this.adj = new HashMap<>();
	}

	// Récupère la liste des successeurs d'un sommet donné
	@Override
	public List<Arc<String>> getSucc(String s) {
		return adj.getOrDefault(s, new ArrayList<>());
	}

	// Ajoute un sommet au graphe s'il n'existe pas déjà
	@Override
	public void ajouterSommet(String noeud) {
		adj.putIfAbsent(noeud, new ArrayList<>());
	}

	// Ajoute un arc entre deux sommets avec une valeur associée
	@Override
	public void ajouterArc(String source, String destination, Integer valeur) {
		// S'assure que les sommets source et destination existent
		ajouterSommet(source);
		ajouterSommet(destination);

		// Vérifie si un arc vers destination existe déjà
		for (Arc<String> arc : adj.get(source)) {
			if (arc.dst().equals(destination)) {
				throw new IllegalArgumentException("Arc déjà présent de " + source + " vers " + destination);
			}
		}

		// Ajout de l'arc (avec la valeur et la destination)
		adj.get(source).add(new Arc<>(valeur, destination));
	}

	// Méthode toString pour afficher le graphe sous forme de chaîne
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		// Parcourt chaque sommet et ses arcs
		for (Map.Entry<String, List<Arc<String>>> entry : adj.entrySet()) {
			String src = entry.getKey();
			List<Arc<String>> arcs = entry.getValue();

			// Si aucun arc n'existe, on affiche simplement le sommet
			if (arcs.isEmpty()) {
				sb.append(src).append(":");
			} else {
				// Sinon, on affiche les arcs au format source-destination(valeur)
				for (Arc<String> arc : arcs) {
					sb.append(src)
							.append("-")
							.append(arc.dst())
							.append("(")
							.append(arc.val())
							.append("), ");
				}
			}
		}

		// Supprime la dernière virgule et l'espace superflus
		if (sb.length() >= 2 && sb.substring(sb.length() - 2).equals(", ")) {
			sb.setLength(sb.length() - 2);
		}

		return sb.toString();
	}
}
