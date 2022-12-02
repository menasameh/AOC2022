package day2;

import utils.FilesUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Day2 {
    public void smallSol() {
        List<Game> list = getInput();

        int ans = list.stream()
                .map(Game::score)
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println(ans);
    }

    public void largeSol() {
        List<Game> list = getInput();

        int ans = list.stream()
                .map(Game::command)
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println(ans);
    }

    List<Game> getInput() {
        String input = FilesUtil.getContentOf("src/day1/input");
        return Arrays.stream(input.split("\n"))
                .map(list -> Arrays.stream(list.split(" "))
                        .map(this::valueOf)
                        .toList())
                .map(list -> new Game(list.get(0), list.get(1)))
                .toList();
    }

    int valueOf(String x) {
        Map<String, Integer> map = Map.of(
                "X", 1, "A", 1,
                "Y", 2, "B", 2,
                "C", 3, "Z", 3);
        return map.get(x);
    }

    static class Game {
        int a;
        int b;

        Game(int a, int b) {
            this.a = a;
            this.b = b;
        }

        int score() {
            int[][] map = {
                    {1+3, 1, 1+6},
                    {2+6, 2+3, 2},
                    {3, 3+6, 3+3}
            };

            return map[b-1][a-1];
        }

        int command() {
            int[][] map = {
                    {3, 1+3, 2+6},
                    {1, 2+3, 3+6},
                    {2, 3+3, 1+6}
            };

            return map[a-1][b-1];
        }
    }
}
