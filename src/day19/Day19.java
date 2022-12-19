package day19;

import utils.FilesUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19 {
    public void smallSol() {
        Game game = getInput();

        System.out.println(game.search());
    }

    public void largeSol() {
        Game game = getInput();

        System.out.println(game.searchLarge());
    }

    Game getInput() {
        return new Game(Arrays.stream(FilesUtil.getContentOf("src/day19/input").split("\n")).map(Blueprint::new).toList());
    }
}

class Game {
    List<Blueprint> blueprints;
    Blueprint currentBlueprint;
    HashMap<String, Integer> mem;
    int maxSoFar = 0;

    Game(List<Blueprint> blueprints) {
        this.blueprints = blueprints;
        currentBlueprint = blueprints.get(0);
    }

    int search() {
        int sum = 0;
        for(Blueprint b: blueprints) {
            mem = new HashMap<>();
            currentBlueprint = b;
            sum += b.num * dp(24, 0,0,0,0,1,0,0,0);
        }
        return sum;
    }

    int searchLarge() {
        int sum = 1;
        for(Blueprint b: blueprints.subList(0, 3)) {
            mem = new HashMap<>();
            currentBlueprint = b;
            maxSoFar = 0;
            sum *= dp(32, 0,0,0,0,1,0,0,0);
        }
        return sum;
    }

    int dp(int time, int ore, int clay, int obs, int geode, int rOre, int rClay, int rObs, int rGeode) {
        if(time <= 0) {
            maxSoFar = Math.max(maxSoFar, geode);
            return geode;
        }

        if(mem.containsKey(label(time, ore, clay, obs, geode, rOre, rClay, rObs, rGeode))) {
            return mem.get(label(time, ore, clay, obs, geode, rOre, rClay, rObs, rGeode));
        }

        int newOre = ore + rOre;
        int newClay = clay + rClay;
        int newObs = obs + rObs;
        int newGeode = geode + rGeode;

        // Prune ways, when we can't beat the maxSoFar even if we produced a geode robot every minute
        if(geode + (time * (time + 1) / 2) + time * rGeode < maxSoFar) {
            return -10000;
        }

        int max = 0;

        if(ore >= currentBlueprint.geodeRobotOre && obs >= currentBlueprint.geodeRobotObs) {
            max = Math.max(max, dp(time - 1, newOre - currentBlueprint.geodeRobotOre, newClay, newObs - currentBlueprint.geodeRobotObs, newGeode, rOre, rClay, rObs, rGeode+1));
        }

        if(ore >= currentBlueprint.obsRobotOre && clay >= currentBlueprint.obsRobotClay) {
            max = Math.max(max, dp(time - 1, newOre - currentBlueprint.obsRobotOre, newClay - currentBlueprint.obsRobotClay, newObs, newGeode, rOre, rClay, rObs + 1, rGeode));
        }

        if(ore >= currentBlueprint.clayRobotOre) {
            max = Math.max(max, dp(time - 1, newOre - currentBlueprint.clayRobotOre, newClay, newObs, newGeode, rOre, rClay + 1, rObs, rGeode));
        }

        if(ore >= currentBlueprint.oreRobotOre) {
            max = Math.max(max, dp(time - 1, newOre - currentBlueprint.oreRobotOre, newClay, newObs, newGeode, rOre + 1, rClay, rObs, rGeode));
        }

        max = Math.max(max, dp(time - 1, newOre, newClay, newObs, newGeode, rOre, rClay, rObs, rGeode));

        mem.put(label(time, ore, clay, obs, geode, rOre, rClay, rObs, rGeode), max);
        return max;
    }

    String label(int time, int ore, int clay, int obs, int geode, int rOre, int rClay, int rObs, int rGeode ) {
        return time + ";" + ore + ";" + clay + ";" + obs + ";" + geode + ";" + rOre + ";" + rClay + ";" + rObs + ";" + rGeode;
    }

}

class Blueprint {
    int num;
    int oreRobotOre;
    int clayRobotOre;
    int obsRobotOre;
    int obsRobotClay;
    int geodeRobotOre;
    int geodeRobotObs;

    Blueprint(String in) {
        Pattern pattern = Pattern.compile("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(in);

        if (matcher.find()) {
            num =  Integer.parseInt(matcher.group(1));
            oreRobotOre =  Integer.parseInt(matcher.group(2));
            clayRobotOre =  Integer.parseInt(matcher.group(3));
            obsRobotOre =  Integer.parseInt(matcher.group(4));
            obsRobotClay =  Integer.parseInt(matcher.group(5));
            geodeRobotOre =  Integer.parseInt(matcher.group(6));
            geodeRobotObs =  Integer.parseInt(matcher.group(7));
        }
    }
}