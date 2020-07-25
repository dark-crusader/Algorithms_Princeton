import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {
    private static final int SOURCE_VERTEX = 0;
    private static final int SINK_VERTEX = 1;
    private final int totalTeams;
    private final Map<String, Integer> teamIdMap;
    private final Map<Integer, String> idTeamMap;
    private final TeamStats[] teamStats;
    private final int[][] remainingFixtures;

    // Class to store win/lose stats for a team
    private class TeamStats {
        private final int wins;
        private final int losses;
        private final int remainingGames;

        public TeamStats(int wins, int losses, int remainingGames) {
            this.wins = wins;
            this.losses = losses;
            this.remainingGames = remainingGames;
        }
    }

    // create a baseball division from given filename in format specified
    public BaseballElimination(String filename) {
        In in = new In(filename);
        totalTeams = in.readInt();
        teamStats = new TeamStats[totalTeams];
        remainingFixtures = new int[totalTeams][totalTeams];
        teamIdMap = new HashMap<>();
        idTeamMap = new HashMap<>();
        for (int i = 0; i < totalTeams; i++) {
            String team = in.readString();
            int wins = in.readInt();
            int losses = in.readInt();
            int remainingGames = in.readInt();
            teamIdMap.put(team, i);
            idTeamMap.put(i, team);
            teamStats[i] = new TeamStats(wins, losses, remainingGames);
            for (int j = 0; j < totalTeams; j++) {
                remainingFixtures[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return totalTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teamIdMap.keySet();
    }

    // Check for validity of team name
    private void validateTeam(String team) {
        if (team == null || !teamIdMap.containsKey(team))
            throw new IllegalArgumentException(team + " is not a valid argument for team name.");
    }

    // number of wins for given team
    public int wins(String team) {
        validateTeam(team);
        return teamStats[teamIdMap.get(team)].wins;
    }

    // number of losses for given team
    public int losses(String team) {
        validateTeam(team);
        return teamStats[teamIdMap.get(team)].losses;
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validateTeam(team);
        return teamStats[teamIdMap.get(team)].remainingGames;
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        return remainingFixtures[teamIdMap.get(team1)][teamIdMap.get(team2)];
    }

    // Creates a flow network from given list of edges
    private FlowNetwork getFlowNetwork(List<FlowEdge> edges, int totalVertices) {
        FlowNetwork fn = new FlowNetwork(totalVertices);
        for (FlowEdge edge: edges) {
            fn.addEdge(edge);
        }
        return fn;
    }

    // Checks for trivial elimination of a team
    private List<String> trivialElimination(int teamId) {
        int maxWins = teamStats[teamId].wins + teamStats[teamId].remainingGames;
        for (int i = 0; i < totalTeams; i++) {
            if (maxWins < teamStats[i].wins) {
                List<String> res = new ArrayList<>();
                res.add(idTeamMap.get(i));
                return res;
            }
        }
        return null;
    }

    // Find if team is eliminated
    private List<String> evaluateElimination(int teamId) {
        // Check for trivial elimination
        List<String> trivial = trivialElimination(teamId);
        if (trivial != null)
            return trivial;
        // If no trivial elimination
        int totalGamesLeft = 0;
        int fixtureId = totalTeams + 2;                 // Used to count total vertices and assign id to team pairs
        ArrayList<FlowEdge> edges = new ArrayList<>();  // Stores all edges to insert into the Flow Network
        Set<Integer> vertexSet = new HashSet<>();
        for (int i = 0; i < totalTeams; i++) {
            for (int j = i + 1; j < totalTeams; j++) {
                int toBePlayed = remainingFixtures[i][j];
                if (toBePlayed > 0 && i != teamId && j != teamId) {
                    edges.add(new FlowEdge(SOURCE_VERTEX, fixtureId, toBePlayed));
                    edges.add(new FlowEdge(fixtureId, i + 2, Double.POSITIVE_INFINITY));
                    edges.add(new FlowEdge(fixtureId, j + 2, Double.POSITIVE_INFINITY));
                    vertexSet.add(i);
                    vertexSet.add(j);
                    fixtureId++;
                    totalGamesLeft += toBePlayed;
                }
            }
        }
        // Insert edges from teams to sink
        int maxWins = teamStats[teamId].wins + teamStats[teamId].remainingGames;
        for (int i: vertexSet) {
            edges.add(new FlowEdge(i + 2, SINK_VERTEX, maxWins - teamStats[i].wins));
        }
        // Check for feasibility of condition using flow network
        FordFulkerson fn = new FordFulkerson(getFlowNetwork(edges, fixtureId), SOURCE_VERTEX, SINK_VERTEX);
        if (fn.value() == totalGamesLeft) {
            return null;
        }
        // Return all vertices in the cut
        ArrayList<String> res = new ArrayList<>();
        for (int i: vertexSet) {
            if (fn.inCut(i + 2))
                res.add(idTeamMap.get(i));
        }
        return res;
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validateTeam(team);
        return evaluateElimination(teamIdMap.get(team)) != null;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        validateTeam(team);
        return evaluateElimination(teamIdMap.get(team));
    }
}
