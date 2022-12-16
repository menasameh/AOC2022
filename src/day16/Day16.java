package day16;

import utils.FilesUtil;

import java.util.*;

public class Day16 {
    public void smallSol() {
        Game game = getInput();

        System.out.println(game.startDp());

    }

    public void largeSol() {
        Game game = getInput();

        game.isMultiple = true;
        game.startDp();

        System.out.println(game.getMaxWithElephant());

    }

    Game getInput() {
        return new Game(Arrays.stream(FilesUtil.getContentOf("src/day16/input").split("\n")).map(Node::new).toList());
    }

    class Game {
        Node start;
        List<Node> nodes;
        boolean isMultiple = false;

        Game(List<Node> nodes) {
            HashMap<String, Node> map = new HashMap<>();
            this.nodes = nodes;

            for(Node n: nodes) {
                map.put(n.name, n);
                if(n.name.equals("AA")) {
                    start = n;
                }
            }

            for(Node n: nodes) {
                n.children = n.childrenNames.stream().map(map::get).toList();
            }
        }
        HashSet<Node> visited;
        HashMap<String, Integer> DP;
        HashMap<Integer, Integer> statesAt26;

        int startDp() {
            visited = new HashSet<>();
            DP = new HashMap<>();
            statesAt26 = new HashMap<>();
            return enhancedDP(0, 30, start);
        }

        String label(int state, int remainingTime, Node cur) {
            return state + ";" + remainingTime + ";" + cur.name;
        }

        int enhancedDP(int state, int remainingTime, Node cur) {
            if (remainingTime <= 1) {
                return 0;
            }

            if(DP.containsKey(label(state, remainingTime, cur))) {
                return DP.get(label(state, remainingTime, cur));
            }

            int max = -1000;

            if(cur.rate == 0 || (state & (1 << index(cur))) != 0) {
                for(Node child: cur.children) {
                    max = Math.max(max, enhancedDP(state, remainingTime - 1, child));
                }
                for(Node child: cur.children) {
                    max = Math.max(max, enhancedDP(state, remainingTime - 1, child));
                }
            } else {
                max = Math.max(max, cur.rate * (remainingTime - 1) + enhancedDP(state | 1 << index(cur),remainingTime - 1, cur));
            }

            DP.put(label(state, remainingTime, cur), max);
            if(remainingTime == 26) {
                statesAt26.put(state, max);
            }
            return max;
        }
        int getMaxWithElephant() {
            int maxValue = 0;

            List<Integer> keys = statesAt26.keySet().stream().toList();
            List<Integer> values = new ArrayList<>();

            for (Integer key : keys) {
                values.add(statesAt26.get(key));
            }

            for(int i=0;i<keys.size();i++) {
                for(int j=i+1;j<keys.size();j++) {
                    if((keys.get(i) & keys.get(j)) == 0) {
                        maxValue = Math.max(maxValue, values.get(i) + values.get(j));
                    }
                }
            }

            return maxValue;
        }

        int index(Node n) {
            for(int i=0;i<nodes.size();i++) {
                if(nodes.get(i).equals(n)) {
                    return i;
                }
            }

            return -1;
        }
    }

    class Node {
        String name;
        int rate;
        List<Node> children;
        List<String> childrenNames;

        Node(String in) {
            List<String> line = Arrays.stream(in.split("Valve | has flow rate=|; tunnels lead to valves |; tunnel leads to valve ")).toList();
            name = line.get(1);
            rate = Integer.parseInt(line.get(2));
            childrenNames = Arrays.stream(line.get(3).split(", ")).toList();
        }
    }
}