package day22;

import utils.FilesUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day22 {
    public void smallSol() {
        Game game = getInput();

        game.play();

        System.out.println(game.score());

    }

    public void largeSol() {
        Game game = getInput();

        game.playLarge();

        System.out.println(game.score());
    }

    Game getInput() {
        return new Game(Arrays.stream(FilesUtil.getContentOf("src/day22/input").split("\n\n")).toList());
    }
}

class Game {
    char[][] grid;
    List<String> actions;

    int x = 1, y = 1;
    int facing = 0; // right
    boolean isLarge = false;

    Game(List<String> in) {
        String[] parts = in.get(0).split("\n");
        grid = new char[parts.length + 2][parts[0].length() + 2];
        for (int i = 0; i < parts.length + 2; i++) {
            grid[i][0] = ' ';
            grid[i][parts[0].length() + 1] = ' ';
        }

        for (int i = 0; i < parts[0].length() + 2; i++) {
            grid[0][i] = ' ';
            grid[parts.length + 1][i] = ' ';
        }

        for (int i = 0; i < parts.length; i++) {
            for (int j = 0; j < parts[0].length(); j++) {
                if(parts[i].length() <= j) {
                    grid[i + 1][j + 1] = ' ';
                } else {
                    grid[i + 1][j + 1] = parts[i].charAt(j);
                }
            }
        }

        for(int i=1;i<grid.length;i++) {
            if(grid[1][i] == '.') {
                y = i;
                break;
            }
        }

        actions = new ArrayList<>();
        Pattern p = Pattern.compile("\\d+|R|L");
        Matcher m = p.matcher(in.get(1));

        while (m.find()) {
            actions.add(m.group());
        }
    }

    void play() {
        for(String action: actions) {
            if(action.equals("L") || action.equals("R")) {
                changeFacing(action);
            } else {
                move(Integer.parseInt(action));
            }
        }
    }

    void playLarge() {
        isLarge = true;
        play();
    }

    void changeFacing(String to) {
        if(to.equals("L")) {
            facing = ((facing - 1) + 4) % 4;
        } else {
            facing = ((facing + 1) + 4) % 4;
        }
    }

    int[] dy = {1, 0, -1, 0};
    int[] dx = {0, 1, 0, -1};
    int[] reverseFacing = {2, 3, 0, 1};

    void move(int steps) {
        while(steps-- > 0) {
            int nextX = x + dx[facing];
            int nextY = y + dy[facing];

            if(grid[nextX][nextY] == '#') {
                break;
            } else if(grid[nextX][nextY] == ' ') {
                if (isLarge) {
                    wrapAroundCube(nextX, nextY);
                } else {
                    wrapAround();
                }
            } else {
                x = nextX;
                y = nextY;
            }
        }
    }

    void wrapAround() {
        int nextX = x + dx[reverseFacing[facing]];
        int nextY = y + dy[reverseFacing[facing]];

        while(grid[nextX][nextY] != ' ') {
            nextX = nextX + dx[reverseFacing[facing]];
            nextY = nextY + dy[reverseFacing[facing]];
        }
        nextX = nextX - dx[reverseFacing[facing]];
        nextY = nextY - dy[reverseFacing[facing]];

        if(grid[nextX][nextY] != '#') {
            x = nextX;
            y = nextY;
        }
    }

    void wrapAroundCube(int x, int y) {
        int nextX = 0;
        int nextY = 0;
        int nextDir = 0;

        // Cube 2 -> cube 4
        if(x == 151) {
            nextX = 100;
            nextY = 150 - y + 1;
            nextDir = 2;
        }
        // cube 4 -> cube 2
        if(x == 101 && y > 100 && y <= 150) {
            nextX = 150;
            nextY = 150 - y + 1;
            nextDir = 2;
        }

        // Cube 2 -> Cube 3
        if(y == 51 && x > 100 && x <= 150) {
            nextX = 100;
            nextY = x - 50;
            nextDir = 2;
        }
        // Cube 3 -> Cube 2
        if(x == 101 && y > 50 && y <= 100) {
            nextX = y + 50;
            nextY = 50;
            nextDir = 3;
        }

        // Cube 2 -> Cube 6
        if(y == 0 && x > 100 && x <= 150) {
            nextX = x - 100;
            nextY = 200;
            nextDir = 3;
        }
        // Cube 6 -> Cube 2
        if(y == 201 && x > 0 && x <= 50) {
            nextX = x + 100;
            nextY = 1;
            nextDir = 1;
        }

        // Cube 1 -> Cube 5
        if(x == 50 && y > 0 && y <= 50) {
            nextX = 1;
            nextY = 150 - y + 1;
            nextDir = 0;
        }
        // Cube 5 -> Cube 1
        if(x == 0 && y > 100 && y <= 150) {
            nextX = 50;
            nextY = 150 - y + 1;
            nextDir = 0;
        }

        // Cube 1 -> Cube 6
        if(y == 0 && x > 50 && x <= 100) {
            nextX = 1;
            nextY = x + 100;
            nextDir = 0;
        }
        // Cube 6 -> Cube 1
        if(x == 0 && y > 150 && y <= 200) {
            nextX = y - 100;
            nextY = 1;
            nextDir = 1;
        }

        // Cube 4 -> Cube 6
        if(y == 151 && x > 50 && x <= 100) {
            nextX = 50;
            nextY = x + 100;
            nextDir = 2;
        }
        // Cube 6 -> Cube 4
        if(x == 51 && y > 150 && y <= 200) {
            nextX = y - 100;
            nextY = 150;
            nextDir = 3;
        }

        // Cube 3 -> Cube 5
        if(x == 50 && y > 50 && y <= 100) {
            nextX = y - 50;
            nextY = 100;
            nextDir = 1;
        }
        // Cube 5 -> Cube 3
        if(y == 100 && x > 0 && x <= 50) {
            nextX = 50;
            nextY = x + 50;
            nextDir = 0;
        }

        if(grid[nextX][nextY] != '#') {
            this.x = nextX;
            this.y = nextY;
            this.facing = nextDir;
        }
    }

    int score() {
        return 1000 * x + 4 * y + facing;
    }
}
