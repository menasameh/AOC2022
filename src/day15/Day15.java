package day15;

import utils.FilesUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Day15 {
    public void smallSol() {
        Game game = getInput();

        System.out.println(game.getCount());
    }

    public void largeSol() {
        Game game = getInput();

        System.out.println(game.getOnlyValid());
    }

    Game getInput() {
        return new Game(Arrays.stream(FilesUtil.getContentOf("src/day15/input").split("\n")).map(Line::new).toList());
    }

    class Game {
        List<Line> lines;

        Game(List<Line> lines) {
            this.lines = lines;
        }

        long getCount() {
            int y = 2000000;

            long count = 0;
            for(int x=-4000000;x<8000000;x++){
                for(Line l : lines) {
                    long manDistance = l.s.manDistance(new Point(x, y));
                    if(manDistance <= l.nearestBeaconDistance()) {
                        if(!hasBeacon(x, y)) {
                            count++;
                        }
                        break;
                    }
                }

            }

            return count;
        }

        long getOnlyValid() {
            int limit = 4000000;
            for(int x=0;x<limit;x++){
                for(int y=0;y<limit;y++) {
                    boolean found = true;

                    for(Line l : lines) {
                        long manDistance = l.s.manDistance(new Point(x, y));
                        if(manDistance <= l.nearestBeaconDistance()) {
                            found = false;
                            y += l.nearestBeaconDistance() - manDistance;
                            break;
                        }
                    }

                    System.out.println(x + " " + y);
                    if(found) {
                        return x* 4000000L +y;
                    }
                }
            }

            return 0;
        }

        boolean hasBeacon(int x, int y) {
            return lines.stream()
                    .map(item -> item.b)
                    .anyMatch(item -> item.x == x && item.y == y);
        }
    }

    class Line {
        Point s, b;

        long nearestBeaconDistance() {
            return s.manDistance(b);
        }

        Line(String in) {
            List<String> line = Arrays.stream(in.split("Sensor at |: closest beacon is at ")).toList();
            s = new Point(line.get(1));
            b = new Point(line.get(2));
        }
    }

    class Point {
        int x, y;

        Point(String in) {
            String[] parts = in.split("(, y=)|(x=)");
            x = Integer.parseInt(parts[1]);
            y = Integer.parseInt(parts[2]);
        }

        long manDistance(Point other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y);
        }

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
