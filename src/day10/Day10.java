package day10;

import utils.FilesUtil;

import java.util.Arrays;
import java.util.List;

public class Day10 {
    public void smallSol() {
        Game game = getInput();

        game.play();
        game.print();

    }

    public void largeSol() {
        Game game = getInput();

        game.play();
        game.draw();
    }

    Game getInput() {
        List<String> list = Arrays.stream(FilesUtil.getContentOf("src/day10/input").split("\n")).toList();
        return new Game(list);
    }

    class Game {
        List<Move> moves;
        long x;
        int maxCycle;
        long[] values;

        Game(List<String> list) {

            moves = list.stream().map(Move::new).toList();
            x = 1;
            maxCycle = 0;
            values = new long[500];
            values[0] = 1;
        }

        void play() {
            int index = 1;
            for(Move m: moves) {
                if(m.operation.equals("addx")) {
                    values[index] = x;
                    x += m.value;
                    values[index+1] = x;
                    index += 2;
                } else {
                    values[index] = x;
                    index += 1;
                }
            }
            maxCycle = index;
        }

        void print() {
            long ans = 0;

            for(int i=19;i<=230;i+=40) {
                ans += values[i]*(i+1);
                System.out.println("i: " + i + ", " + values[i]);
            }

            System.out.println(ans);
        }

        void draw() {
            char[] grid = new char[240];

            for(int i=0;i<grid.length;i++) {
                long sprite = values[i];
                if((i%40) >= sprite - 1 && (i%40) <= sprite+1) {
                    grid[i] = '#';
                } else {
                    grid[i] = '.';
                }
            }
            for(int i=0;i<grid.length;i++) {
                System.out.print(grid[i]);
                if((i+1)%40 == 0) {
                    System.out.println();
                }
            }
        }


    }

    class Move {
        String operation;
        long value;

        Move(String in) {
            String[] parts = in.split(" ");
            operation = parts[0];
            if(!operation.equals("noop")) {
                value = Integer.parseInt(parts[1]);
            }
        }
    }
}
