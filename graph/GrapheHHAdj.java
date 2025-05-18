package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrapheHHAdj implements VarGraph {

	private Map<String, List<Arc<String>>> adj;

	public GrapheHHAdj() {
		this.adj = new HashMap<>();
	}

	@Override
	public List<Arc<String>> getSucc(String s) {
		return adj.getOrDefault(s, new ArrayList<>());
	}

	@Override
	public void ajouterSommet(String noeud) {
		adj.putIfAbsent(noeud, new ArrayList<>());
	}

	@Override
	public void ajouterArc(String source, String destination, Integer valeur) {
		ajouterSommet(source);
		ajouterSommet(destination);

		// Vérifie si un arc vers destination existe déjà
		for (Arc<String> arc : adj.get(source)) {
			if (arc.dst().equals(destination)) {
				throw new IllegalArgumentException("Arc déjà présent de " + source + " vers " + destination);
			}
		}

		// Ajout de l'arc (attention à l'ordre val, dst)
		adj.get(source).add(new Arc<>(valeur, destination));
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, List<Arc<String>>> entry : adj.entrySet()) {
			String src = entry.getKey();
			List<Arc<String>> arcs = entry.getValue();
			if (arcs.isEmpty()) {
				sb.append(src).append(":");
			} else {
				for (Arc<String> arc : arcs) {
					sb.append(src).append("-").append(arc.dst()).append("(").append(arc.val()).append("), ");
				}
			}
		}

		// Supprimer la dernière virgule et l'espace si présent
		if (sb.length() >= 2 && sb.substring(sb.length() - 2).equals(", ")) {
			sb.setLength(sb.length() - 2);
		}

		return sb.toString();
	}
}
