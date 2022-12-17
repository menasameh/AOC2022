package day17;

import utils.FilesUtil;

import java.util.*;

public class Day17 {
    public void smallSol() {
        Game game = getInput();

        System.out.println(game.simulate());
    }

    public void largeSol() {
        Game game = getInput();

        System.out.println(game.simulateLong());
    }

    Game getInput() {
        return new Game(FilesUtil.getContentOf("src/day17/input"));
    }
}

class Game {
    String gas;
    List<Shape> shapes;
    char[][] grid;

    Game(String gas) {
        this.gas = gas;
        shapes = new ArrayList<>();
        shapes.add(new Hline(2, 0));
        shapes.add(new Plus(2, 0));
        shapes.add(new L(2, 0));
        shapes.add(new Vline(2, 0));
        shapes.add(new Square(2, 0));

        grid = new char[7][1000000];
        for(char[] c: grid) {
            Arrays.fill(c, '.');
        }
    }

    int simulate() {
        int maxHeight = 0;
        int gasIndex = 0;

        for(int i=0;i<2022;i++) {
            Shape s = shapes.get(i % shapes.size());
            s.x = 2;
            s.setY(maxHeight + 3);

            boolean falling = true;

            while (falling) {
                if (gas.charAt(gasIndex) == '<') {
                    s.moveLeft(grid);
                } else if (gas.charAt(gasIndex) == '>') {
                    s.moveRight(grid);
                }

                gasIndex = (gasIndex + 1) % gas.length();

                falling = s.moveDown(grid);

            }
            s.place(grid);
            maxHeight = (int) Math.max(maxHeight, s.y + 1);
        }

        return maxHeight;
    }

    long simulateLong() {
        long maxHeight = 0;
        int gasIndex = 0;

        HashMap<String, String> mem = new HashMap<>();

        long i =0;
        long maxInput = 1000000000000L;

        while(i<maxInput) {

            Shape s = shapes.get((int) (i % shapes.size()));
            s.x = 2;
            s.setY(maxHeight + 3);

            if(mem.containsKey(label((int) (i % shapes.size()), gasIndex))) {
                String[] vals = mem.get(label((int) (i % shapes.size()), gasIndex)).split(";");
                long oldI = Long.parseLong(vals[0]);
                long oldHeight = Long.parseLong(vals[1]);

                if((maxInput - i) % (i - oldI) == 0) {
                    return maxHeight + (maxInput - i) / (i - oldI) * (maxHeight - oldHeight);
                }
            }

            boolean falling = true;

            while (falling) {
                if (gas.charAt(gasIndex) == '<') {
                    s.moveLeft(grid);
                } else if (gas.charAt(gasIndex) == '>') {
                    s.moveRight(grid);
                }

                gasIndex = (gasIndex + 1) % gas.length();

                falling = s.moveDown(grid);

            }
            s.place(grid);
            maxHeight = Math.max(maxHeight, s.y + 1);
            mem.put(label((int) (i % shapes.size()), gasIndex), label(i, maxHeight));

            i++;
        }

        return maxHeight;
    }

    String label(long a, long b) {
        return a + ";" + b;
    }

    void printState(int limit) {
        for(int j=limit+5;j>=0;j--) {
            for(int k=0;k<7;k++) {
                System.out.print(grid[k][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}

abstract class Shape {
    long x, y;

    public Shape(long x, long y) {
        this.x = x;
        this.y = y;
    }

    abstract void setY(long y);
    abstract void moveRight(char[][] grid);
    abstract void moveLeft(char[][] grid);
    abstract boolean moveDown(char[][] grid);

    abstract void place(char[][] grid);
}

class Hline extends Shape {
    public Hline(long x, long y) {
        super(x, y);
    }

    @Override
    void setY(long y) {
        this.y = y;
    }

    @Override
    public void moveRight(char[][] grid) {
        if(x == grid.length - 4) {
            return;
        }

        if(grid[(int) (x+4)][(int) y] != '.') {
            return;
        }

        x++;
    }

    @Override
    public void moveLeft(char[][] grid) {
        if(x == 0) {
            return;
        }

        if(grid[(int) (x-1)][(int) y] != '.') {
            return;
        }

        x--;
    }

    @Override
    public boolean moveDown(char[][] grid) {
        if(y == 0) {
            return false;
        }

        if(grid[(int) x][(int) (y-1)] != '.' || grid[(int) (x+1)][(int) (y-1)] != '.' || grid[(int) (x+2)][(int) (y-1)] != '.' || grid[(int) (x+3)][(int) (y-1)] != '.') {
            return false;
        }

        y--;
        return true;
    }

    @Override
    public void place(char[][] grid) {
        grid[(int) x][(int) y] = '#';
        grid[(int) (x+1)][(int) y] = '#';
        grid[(int) (x+2)][(int) y] = '#';
        grid[(int) (x+3)][(int) y] = '#';
    }
}

class Plus extends Shape {
    public Plus(long x, long y) {
        super(x, y);
    }

    @Override
    void setY(long y) {
        this.y = y + 2;
    }

    @Override
    public void moveRight(char[][] grid) {
        if(x == grid.length - 3) {
            return;
        }

        if(grid[(int) x+3][(int) y-1] != '.' || grid[(int) x+2][(int) y] != '.' || grid[(int) x+2][(int) y-2] != '.') {
            return;
        }

        x++;
    }

    @Override
    public void moveLeft(char[][] grid) {
        if(x == 0) {
            return;
        }

        if(grid[(int) x-1][(int) y-1] != '.' || grid[(int) x][(int) y] != '.' || grid[(int) x][(int) y-2] != '.') {
            return;
        }

        x--;
    }

    @Override
    public boolean moveDown(char[][] grid) {
        if(y == 2) {
            return false;
        }

        if(grid[(int) x+1][(int) y-3] != '.' || grid[(int) x][(int) y-2] != '.' || grid[(int) x+2][(int) y-2] != '.') {
            return false;
        }

        y--;
        return true;
    }

    @Override
    public void place(char[][] grid) {
        grid[(int) x+1][(int) y] = '#';
        grid[(int) x][(int) y-1] = '#';
        grid[(int) x+1][(int) y-1] = '#';
        grid[(int) x+2][(int) y-1] = '#';
        grid[(int) x+1][(int) y-2] = '#';
    }
}

class L extends Shape {
    public L(long x, long y) {
        super(x, y);
    }

    @Override
    void setY(long y) {
        this.y = y + 2;
    }

    @Override
    public void moveRight(char[][] grid) {
        if(x == grid.length - 3) {
            return;
        }

        if(grid[(int) x+3][(int) y] != '.' || grid[(int) x+3][(int) y-1] != '.' || grid[(int) x+3][(int) y-2] != '.') {
            return;
        }

        x++;
    }

    @Override
    public void moveLeft(char[][] grid) {
        if(x == 0) {
            return;
        }

        if(grid[(int) x-1][(int) y-2] != '.' || grid[(int) x+1][(int) y] != '.' || grid[(int) x+1][(int) y-1] != '.') {
            return;
        }

        x--;
    }

    @Override
    public boolean moveDown(char[][] grid) {
        if(y == 2) {
            return false;
        }

        if(grid[(int) x][(int) y-3] != '.' || grid[(int) x+1][(int) y-3] != '.' || grid[(int) x+2][(int) y-3] != '.') {
            return false;
        }

        y--;
        return true;
    }

    @Override
    public void place(char[][] grid) {
        grid[(int) x+2][(int) y] = '#';
        grid[(int) x+2][(int) y-1] = '#';
        grid[(int) x+2][(int) y-2] = '#';
        grid[(int) x][(int) y-2] = '#';
        grid[(int) x+1][(int) y-2] = '#';
    }
}

class Vline extends Shape {
    public Vline(long x, long y) {
        super(x, y);
    }

    @Override
    void setY(long y) {
        this.y = y + 3;
    }

    @Override
    public void moveRight(char[][] grid) {
        if(x == grid.length - 1) {
            return;
        }

        if(grid[(int) x+1][(int) y] != '.' || grid[(int) x+1][(int) y-1] != '.' || grid[(int) x+1][(int) y-2] != '.' || grid[(int) x+1][(int) y-3] != '.') {
            return;
        }

        x++;
    }

    @Override
    public void moveLeft(char[][] grid) {
        if(x == 0) {
            return;
        }

        if(grid[(int) x-1][(int) y] != '.' || grid[(int) x-1][(int) y-1] != '.' || grid[(int) x-1][(int) y-2] != '.' || grid[(int) x-1][(int) y-3] != '.') {
            return;
        }

        x--;
    }

    @Override
    public boolean moveDown(char[][] grid) {
        if(y == 3) {
            return false;
        }

        if(grid[(int) x][(int) y-4] != '.') {
            return false;
        }

        y--;
        return true;
    }

    @Override
    public void place(char[][] grid) {
        grid[(int) x][(int) y] = '#';
        grid[(int) x][(int) y-1] = '#';
        grid[(int) x][(int) y-2] = '#';
        grid[(int) x][(int) y-3] = '#';
    }
}

class Square extends Shape {
    public Square(long x, long y) {
        super(x, y);
    }

    @Override
    void setY(long y) {
        this.y = y + 1;
    }

    @Override
    public void moveRight(char[][] grid) {
        if(x == grid.length - 2) {
            return;
        }

        if(grid[(int) x+2][(int) y] != '.' || grid[(int) x+2][(int) y-1] != '.') {
            return;
        }

        x++;
    }

    @Override
    public void moveLeft(char[][] grid) {
        if(x == 0) {
            return;
        }

        if(grid[(int) x-1][(int) y] != '.' || grid[(int) x-1][(int) y-1] != '.') {
            return;
        }

        x--;
    }

    @Override
    public boolean moveDown(char[][] grid) {
        if(y == 1) {
            return false;
        }

        if(grid[(int) x][(int) y-2] != '.' || grid[(int) x+1][(int) y-2] != '.') {
            return false;
        }

        y--;
        return true;
    }

    @Override
    public void place(char[][] grid) {
        grid[(int) x][(int) y] = '#';
        grid[(int) x][(int) y-1] = '#';
        grid[(int) x+1][(int) y] = '#';
    }
}