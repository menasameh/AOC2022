package day24;

import utils.FilesUtil;

import java.util.*;

public class Day24 {
    public void smallSol() {
        Game game = getInput();
        game.prepare();
        State start = new State(0, 1, 0);
        State end = new State(game.grid.length - 1, game.grid[0].length - 2, 0);

        System.out.println(game.bfs(start, end));
    }

    public void largeSol() {
        Game game = getInput();
        game.prepare();

        State start = new State(0, 1, 0);
        State end = new State(game.grid.length - 1, game.grid[0].length - 2, 0);

        int endOfPath1 = game.bfs(start, end);
        start.round = 0;
        end.round = endOfPath1;
        int endOfPath2 = game.bfs(end, start);
        end.round = 0;
        start.round = endOfPath2;
        int endOfPath3 = game.bfs(start, end);

        System.out.println(endOfPath1);
        System.out.println(endOfPath2);
        System.out.println(endOfPath3);

    }

    Game getInput() {
        return new Game(FilesUtil.getContentOf("src/day24/input"));
    }
}

class Game {
    char[][] grid;
    HashSet<String> positionIsOccupiedAtRound;

    Game(String in) {
        String[] parts = in.split("\n");
        grid = new char[parts.length][parts[0].length()];

        for (int i = 0; i < parts.length; i++) {
            for (int j = 0; j < parts[0].length(); j++) {
                grid[i][j] = parts[i].charAt(j);
            }
        }
    }

    void prepare() {
        positionIsOccupiedAtRound = new HashSet<>();

        for(int i=0;i<grid.length;i++) {
            for(int j=0;j<grid[0].length;j++) {
                int dx = 0;
                int dy = 0;
                if(grid[i][j] == '<') {
                    dy = -1;
                } else if(grid[i][j] == '>') {
                    dy = 1;
                } else if(grid[i][j] == '^') {
                    dx = -1;
                } else if(grid[i][j] == 'v') {
                    dx = 1;
                } else {
                    continue;
                }
                int nextX = i;
                int nextY = j;
                positionIsOccupiedAtRound.add(label(nextX, nextY, 0));
                for(int round=1;round<100;round++) {
                    nextX = nextX + dx;
                    nextY = nextY + dy;
                    if(nextX == grid.length - 1) {
                        nextX = 1;
                    }
                    if(nextY == grid[0].length - 1) {
                        nextY = 1;
                    }
                    if(nextX == 0) {
                        nextX = grid.length - 2;
                    }
                    if(nextY == 0) {
                        nextY = grid[0].length - 2;
                    }
                    positionIsOccupiedAtRound.add(label(nextX, nextY, round));
                }
            }
        }
    }

    String label(int x, int y, int round) {
        return x + ";" + y + ";" + round;
    }
    String label(State s) {
        return label(s.x, s.y, s.round % 100);
    }

    String visitedLabel(State s) {
        return s.x + ";" + s.y;
    }

    int[] dx = {0, 1, 0, -1, 0};
    int[] dy = {1, 0, -1 , 0, 0};

    int bfs(State start, State end) {
        Queue<State> q = new LinkedList<>();
        HashMap<String, Integer> visited = new HashMap<>();
        List<Integer> ans = new ArrayList<>();
        q.add(start);

        while(!q.isEmpty()) {
            State s = q.poll();
            if(s.equals(end)) {
                ans.add(s.round);
            }

            for(int i=0;i<5;i++) {
                int nextX = s.x + dx[i];
                int nextY = s.y + dy[i];
                int nextRound = s.round + 1;
                State next = new State(nextX, nextY, nextRound);


                if(!((nextX >= 1 && nextX < grid.length - 1 && nextY >= 1 && nextY < grid[0].length - 1) || next.equals(start) || next.equals(end))) {
                    continue;
                }

                if(visited.getOrDefault(visitedLabel(next), 0) == next.round ) {
                    continue;
                }

                if(!ans.isEmpty() && next.round > ans.stream().mapToInt(Integer::intValue).min().getAsInt()) {
                    continue;
                }

                if(!positionIsOccupiedAtRound.contains(label(next))) {
                    next.h = next.manDistance(end);
                    q.add(next);
                    visited.put(visitedLabel(next), next.round);
                }
            }
        }

        return ans.stream().mapToInt(Integer::intValue).min().getAsInt();
    }
}

class State {
    int x, y, round ,h;


    public State(int x, int y, int round) {
        this.x = x;
        this.y = y;
        this.round = round;
    }

    int manDistance(State s) {
        return Math.abs(x - s.x) + Math.abs(y - s.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return x == state.x && y == state.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, round);
    }
}