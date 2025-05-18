package applications;

import graph.interfaces.IGraph;
import maze.Maze;

import java.util.*;
import java.util.List;


public class GraphMaze<C> implements IGraph<C> {
    private final Maze<C> maze;

    public GraphMaze(Maze<C> maze) {
        this.maze = maze;
    }

    @Override
    public List<Arc<C>> getSucc(C s) {
        List<Arc<C>> successeurs = new ArrayList<>();
        for (C voisin : maze.openedNeighbours(s)) {
            successeurs.add(new Arc<>(1, voisin)); // coût 1 pour chaque case accessible
        }
        return successeurs;
    }
}
