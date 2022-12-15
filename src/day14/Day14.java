package day14;

import utils.FilesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

public class Day14 {
    public void smallSol() {
        Game game = getInput();
        game.print(450, 0, 520, 50);
        System.out.println(game.maxSand());
    }

    public void largeSol() {
        Game game = getInput();
        game.applyLargeRules();
        game.print(450, 0, 520, 50);
        System.out.println(game.maxSandLarge());
    }

    Game getInput() {
        return new Game(Arrays.stream(FilesUtil.getContentOf("src/day14/input").split("\n")).map(Line::new).toList());
    }

    class Game {
        char[][] grid = new char[1000][200];

        int count = 0;
        int maxY = 0;

        Game(List<Line> lines) {
            for(int i=0;i<grid.length;i++) {
                for(int j=0;j<grid[0].length;j++) {
                    grid[i][j] = '.';
                }
            }

            for(Line l : lines) {
                Point prev = l.points.get(0);
                for(Point p: l.points) {
                    if(prev.isHorizontal(p)) {
                        if(prev.x < p.x) {
                            for(int i=prev.x;i<=p.x;i++) {
                                grid[i][prev.y] = '#';
                            }
                        } else {
                            for(int i=p.x;i<=prev.x;i++) {
                                grid[i][prev.y] = '#';
                            }
                        }
                    } else {
                        if(prev.y < p.y) {
                            for(int i=prev.y;i<=p.y;i++) {
                                grid[prev.x][i] = '#';
                            }
                        } else {
                            for(int i=p.y;i<=prev.y;i++) {
                                grid[prev.x][i] = '#';
                            }
                        }
                    }
                    prev = p;
                }
            }

            for (char[] chars : grid) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (chars[j] == '#') {
                        maxY = Math.max(maxY, j);
                    }
                }
            }
        }

        void applyLargeRules() {
            for(int i=0;i<grid.length;i++) {
                grid[i][maxY+2]='#';
            }
        }

        int maxSand() {
            boolean canAddSand = true;
            while(canAddSand) { canAddSand = simulateSand(maxY);}
            return count;
        }

        int maxSandLarge() {
            boolean canAddSand = true;
            while(canAddSand) { canAddSand = simulateSand(maxY+2);}
            return count;
        }

        boolean simulateSand(int maxY) {
            Point p = new Point(500, 0);

            if(grid[p.x][p.y] != '.') {
                return false;
            }

            while(p.y < maxY) {
                if(grid[p.x][p.y + 1] == '.') {
                    p.y++;
                } else if(grid[p.x - 1][p.y + 1] == '.') {
                    p.y++;
                    p.x--;
                } else if(grid[p.x + 1][p.y + 1] == '.') {
                    p.y++;
                    p.x++;
                } else {
                    grid[p.x][p.y] = 'o';
                    count++;
                    return true;
                }
            }
            return false;
        }

        void print(int minX, int minY, int maxX, int maxY) {
            for(int i=minX;i<maxX;i++) {
                for(int j=minY;j<maxY;j++) {
                    System.out.print(grid[i][j]);
                }
                System.out.println();
            }
        }
    }

    class Line {
        List<Point> points;

        Line(String in) {
            points = Arrays.stream(in.split(" -> ")).map(Point::new).toList();
        }
    }

    class Point {
        int x, y;

        boolean isHorizontal(Point other) {
            return y == other.y;
        }

        Point(String in) {
            String[] parts = in.split(",");
            x = Integer.parseInt(parts[0]);
            y = Integer.parseInt(parts[1]);
        }

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
