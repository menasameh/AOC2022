package day9;

import utils.FilesUtil;

import java.util.*;

public class Day9 {
    public void smallSol() {
        Game game = getInput();

        game.play();

        System.out.println(game.positionsVisited.stream().count());

    }

    public void largeSol() {
        Game game = getInput();

        game.playLarge();

        System.out.println(game.positionsVisited.stream().count());
    }

    Game getInput() {
        List<String> list = Arrays.stream(FilesUtil.getContentOf("src/day9/input").split("\n")).toList();
        return new Game(list);
    }

    class Game {
        List<Move> moves;
        List<SimpleMove> simpleMoves;
        Position head, tail;
        HashSet<String> positionsVisited;

        Game(List<String> list) {

            moves = list.stream().map(Move::new).toList();
            simpleMoves = new ArrayList<>();
            for(Move m: moves) {
                for(int i=0;i<m.count;i++) {
                    simpleMoves.add(new SimpleMove(m.direction));
                }
            }

            head = new Position(0, 0);
            tail = new Position(0, 0);

            positionsVisited = new HashSet<>();
            positionsVisited.add(tail.toString());
        }

        void play() {
            for(SimpleMove m: simpleMoves) {
                moveHead(head, m);
                updateTail(head, tail, true);
            }
        }

        void playLarge() {
            Position[] points = new Position[10];

            for(int i=0;i<points.length;i++) {
                points[i] = new Position(0, 0);
            }

            for(SimpleMove m: simpleMoves) {
                moveHead(points[0], m);
                for(int i=0;i<points.length-1;i++) {
                    updateTail(points[i], points[i+1], i == points.length-2);
                }
            }
        }

        void moveHead(Position p, SimpleMove move) {
            switch (move.direction) {
                case 'U' -> p.y--;
                case 'D' -> p.y++;
                case 'L' -> p.x--;
                case 'R' -> p.x++;
            }
        }

        void updateTail(Position h, Position t, boolean recordMove) {
            if(h.isTouching(t)) return;

            if(Math.abs(h.x-t.x) > 1 && Math.abs(h.y-t.y) > 1) {
                t.x = t.x < h.x ? h.x - 1 : h.x + 1;
                t.y = t.y < h.y ? h.y - 1 : h.y + 1;
            } else if(Math.abs(h.x-t.x) > 1 ) {
                t.x = t.x < h.x ? h.x - 1 : h.x + 1;
                t.y = h.y;
            } else if(Math.abs(h.y-t.y) > 1) {
                t.x = h.x;
                t.y = t.y < h.y ? h.y - 1 : h.y + 1;
            }
            if(recordMove) {
                positionsVisited.add(t.toString());
            }
        }
    }

    class Move {
        char direction;
        int count;

        Move(String in) {
            String[] parts = in.split(" ");
            direction = parts[0].charAt(0);
            count = Integer.parseInt(parts[1]);
        }
    }

    class SimpleMove {
        char direction;

        SimpleMove(char direction) {
            this.direction = direction;
        }
    }

    class Position {
        int x, y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return x + "-" + y;
        }

        boolean isTouching(Position other) {
            return Math.abs(x-other.x) <= 1 && Math.abs(y-other.y) <= 1;
        }
    }
}
