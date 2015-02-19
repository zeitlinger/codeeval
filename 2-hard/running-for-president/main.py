from collections import namedtuple
import sys

Issue = namedtuple("Issue", "name cost")
State = namedtuple("State", "name votes totalRelevance issueRelevance")

def get_cost(issues):
    return sum(map(lambda x: x.cost, issues))

def can_win_state(issues, state):
    relevance = sum([state.issueRelevance[issue] for issue in issues])
    return relevance * 2 > state.totalRelevance

def can_win(states, issues):
    return sum([state.votes for state in states if can_win_state(issues, state)]) >= 270

def get_minimal_issues(allIssues, states, taken, best):
    if can_win(states, taken):
        return taken

    for issue in allIssues:
        if issue in taken:
            continue

        next_taken = list(taken)
        next_taken.append(issue)

        if best is not None and get_cost(best) <= get_cost(next_taken):
            continue

        min = get_minimal_issues(allIssues, states, next_taken, best)
        if min is not None:
            best = min

    return best

def main():
    lines = [i.strip().split(": ") for i in open(sys.argv[1], 'r') if len(i.strip()) > 0]

    issueCount = int(lines.pop(0)[1])

    issues = {}
    for _ in range(issueCount):
        line = lines.pop(0)
        name = line[0]
        issues[name] = Issue(name=name, cost=int(line[1]))

    states = []

    while len(lines) > 0:
        name = lines.pop(0)[0]
        votes = int(lines.pop(0)[1])
        issueRelevance = {}
        totalRelevance = 0

        for _ in range(issueCount):
            s = lines.pop(0)
            relevance = int(s[1])
            totalRelevance += relevance
            issueRelevance[issues[s[0]]] = relevance

        states.append(State(name=name, votes=votes, totalRelevance=totalRelevance, issueRelevance=issueRelevance))

    for x in get_minimal_issues(sorted(issues.values(), key=lambda x: x.cost), states, [], None):
        print(x.name)

if __name__ == "__main__":
    main()