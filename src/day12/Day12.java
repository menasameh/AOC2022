package day12;

import utils.FilesUtil;

import java.math.BigInteger;
import java.util.*;

public class Day12 {
    public void smallSol() {
        List<String> game = getInput();

        for(int i=0;i<game.size();i++) {
            for(int j=0;j<game.get(0).length();j++) {
                if(game.get(i).charAt(j) == 'S') {
                    System.out.println(bfs(i, j, game));
                    return;
                }
            }
        }

    }

    public void largeSol() {
        List<String> game = getInput();
        List<Long> sp = new ArrayList<>();

        for(int i=0;i<game.size();i++) {
            for(int j=0;j<game.get(0).length();j++) {
                if(game.get(i).charAt(j) == 'S' || game.get(i).charAt(j) == 'a') {
                    sp.add(bfs(i, j, game));
                }
            }
        }

        System.out.println(sp.stream().mapToLong(Long::longValue).filter(i -> i>=0).min());
    }

    int[] dx = {1, -1, 0, 0};
    int[] dy = {0, 0, -1, 1};

    long bfs(int i, int j, List<String> grid) {
        boolean[][] visited = new boolean[grid.size()][grid.get(0).length()];
        Queue<State> q = new LinkedList<>();
        visited[i][j] = true;
        q.add(new State(i, j, 0));

        while(!q.isEmpty()) {
            State s = q.poll();

            if(grid.get(s.x).charAt(s.y) == 'E') {
                return s.level;
            }

            for(int k=0;k<4;k++) {
                int newX = s.x + dx[k];
                int newY = s.y + dy[k];

                if(newX < 0 || newX >= grid.size() || newY < 0 || newY >= grid.get(0).length()) {
                    continue;
                }

                if(visited[newX][newY]) {
                    continue;
                }

                char n = grid.get(newX).charAt(newY);

                if(grid.get(s.x).charAt(s.y) == 'S') {
                    if(n == 'a' || n == 'b') {
                        visited[newX][newY] = true;
                        q.add(new State(newX, newY, s.level + 1));
                    }
                } else if(grid.get(s.x).charAt(s.y) == 'z' || grid.get(s.x).charAt(s.y) == 'y') {
                    if(n == 'E') {
                        visited[newX][newY] = true;
                        q.add(new State(newX, newY, s.level + 1));
                    } else {
                        if(n - grid.get(s.x).charAt(s.y) <= 1 ) {
                            visited[newX][newY] = true;
                            q.add(new State(newX, newY, s.level + 1));
                        }
                    }
                } else {
                    if(n - grid.get(s.x).charAt(s.y) <= 1 ) {
                        visited[newX][newY] = true;
                        q.add(new State(newX, newY, s.level + 1));
                    }
                }
            }
        }


        return -1;
    }

    List<String> getInput() {
        List<String> list = Arrays.stream(FilesUtil.getContentOf("src/day12/input").split("\n")).toList();
        return list;
    }

    class State {
        int x, y, level;

        State(int x, int y, int level) {
            this.x = x;
            this.y = y;
            this.level = level;
        }
    }
}
