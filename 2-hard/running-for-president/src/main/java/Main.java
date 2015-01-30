import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {

    public static class Issue {
        private final String name;
        private final long cost;

        public Issue(String name, long cost) {
            this.name = name;
            this.cost = cost;
        }

        public String getName() {
            return name;
        }

        public long getCost() {
            return cost;
        }
    }

    public static class State {
        private final String name;
        private final int votes;
        private final Map<Issue, Integer> issueRelevance;
        private final int totalRelevance;

        public State(String name, int votes, Map<Issue, Integer> issueRelevance, int totalRelevance) {
            this.name = name;
            this.votes = votes;
            this.issueRelevance = issueRelevance;
            this.totalRelevance = totalRelevance;
        }

        public String getName() {
            return name;
        }

        public int getVotes() {
            return votes;
        }

        public Map<Issue, Integer> getIssueRelevance() {
            return issueRelevance;
        }

        public int getTotalRelevance() {
            return totalRelevance;
        }
    }

    public static void main (String[] args) throws IOException {
        Iterator<String> iterator = getLines(args[0]);

        int issueCount = Integer.parseInt(readLine(iterator.next())[1]);

        Map<String, Issue> issues = new HashMap<>();

        for (int i = 0; i < issueCount; i++) {
            String[] strings = readLine(iterator.next());
            String name = strings[0];
            issues.put(name, new Issue(name, Long.parseLong(strings[1])));
        }

        List<State> states = new LinkedList<>();

        while (iterator.hasNext()) {
            String state = iterator.next();
            int votes = Integer.parseInt(readLine(iterator.next())[1]);
            Map<Issue, Integer> issueRelevance = new HashMap<>();
            int totalRelevance = 0;

            for (int i = 0; i < issues.size(); i++) {
                String[] strings = readLine(iterator.next());

                int relevance = Integer.parseInt(strings[1]);
                issueRelevance.put(issues.get(strings[0]), relevance);
                totalRelevance += relevance;
            }
            states.add(new State(state, votes, issueRelevance, totalRelevance));
        }

        List<Issue> values = new ArrayList<>(issues.values());
        Collections.sort(values, new Comparator<Issue>() {
            @Override
            public int compare(Issue o1, Issue o2) {
                if (o1.getCost() == o2.getCost()) {
                    return 0;
                }
                if (o1.getCost() < o2.getCost()) {
                    return -1;
                }
                return 1;
            }
        });

        List<Issue> list = getMinimalIssues(values, states, new LinkedList<Issue>(), null);
        for (Issue issue : list) {
            System.out.println(issue.getName());
        }
    }

    private static long getCost(List<Issue> issues) {
        long cost = 0;
        for (Issue issue : issues) {
            cost += issue.getCost();
        }
        return cost;
    }

    private static Iterator<String> getLines(String arg) throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(new File(arg)));
        List<String> lines = new LinkedList<>();
        String line;
        while ((line = buffer.readLine()) != null) {
            String trim = line.trim();
            if (!trim.isEmpty()) {
                lines.add(trim);
            }
        }

        return lines.iterator();
    }

    private static List<Issue> getMinimalIssues(Collection<Issue> allIssues, List<State> states, List<Issue> taken, List<Issue> best) {
        if (canWin(states, taken)) {
            return taken;
        }

        for (Issue issue : allIssues) {
            if (taken.contains(issue)) {
                continue;
            }

            List<Issue> nextTaken = new ArrayList<>(taken);
            nextTaken.add(issue);

            if (best != null && getCost(best) <= getCost(nextTaken)) {
                continue;
            }

            List<Issue> minimalIssues = getMinimalIssues(allIssues, states, nextTaken, best);
            if (minimalIssues != null) {
                best = minimalIssues;
            }
        }
        return best;
    }

    private static boolean canWin(List<State> states, List<Issue> issues) {
        int votes = 0;
        for (State state : states) {
            if (canWin(issues, state)) {
                votes += state.getVotes();
            }
        }
        return votes >= 270;
    }

    private static boolean canWin(List<Issue> issues, State state) {
        int relevance = 0;

        Map<Issue, Integer> issueRelevance = state.getIssueRelevance();

        for (Issue issue : issues) {
            relevance += issueRelevance.get(issue);
        }

        return relevance * 2 > state.getTotalRelevance();
    }

    private static String[] readLine(String line) {
        return line.split(": *", 2);
    }
    
}
