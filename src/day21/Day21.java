package day21;

import utils.FilesUtil;

import java.util.*;

public class Day21 {
    public void smallSol() {
        Game game = getInput();

        System.out.println(game.solve("root"));
    }

    public void largeSol() {
        Game game = getInput();

       System.out.println(game.solveLarge());
    }

    Game getInput() {
        return new Game(Arrays.stream(FilesUtil.getContentOf("src/day21/input").split("\n")).toList());
    }
}

class Game {
    Map<String, String> jobs;
    Map<String, Long> values;

    Game(List<String> monkeys) {
        jobs = new HashMap<>();
        values = new HashMap<>();
        for(String m: monkeys) {
            String[] parts = m.split(": ");
            jobs.put(parts[0], parts[1]);
        }
    }

    long solveLarge() {
        long lo = 0;
        long hi = (long) 1e15;
        jobs.put("root", "qqqz - zhms");
        jobs.put("humn", "0");
        while(lo < hi) {
            long mid = (lo+hi)/2L;
            jobs.put("humn", String.valueOf(mid));
            values = new HashMap<>();
            long val = solve("root");
            if(val == 0) {
                return mid;
            } else if(val > 0) {
                hi = mid;
            } else {
                lo = mid + 1;

            }
        }

        return -1;
    }

    long solve(String monkey) {
        if (values.containsKey(monkey)) {
            return values.get(monkey);
        }

        String job = jobs.get(monkey);
        try {
            values.put(monkey, Long.parseLong(job));
        } catch (NumberFormatException e) {
            String[] parts = job.split(" ");
            long operand1 = solve(parts[0]);
            long operand2 = solve(parts[2]);
            switch (parts[1]) {
                case "+" -> values.put(monkey, operand1 + operand2);
                case "-" -> values.put(monkey, operand1 - operand2);
                case "*" -> values.put(monkey, operand1 * operand2);
                case "/" -> values.put(monkey, operand1 / operand2);
            }
        }
        return values.get(monkey);
    }
}
