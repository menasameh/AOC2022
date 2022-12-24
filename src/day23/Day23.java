package day23;

import utils.FilesUtil;
import java.util.*;

public class Day23 {
    public void smallSol() {
        Game game = getInput();

        game.play();

        System.out.println(game.score());
    }

    public void largeSol() {
        Game game = getInput();

        game.playLarge();

        System.out.println(game.maxRounds);
    }

    Game getInput() {
        return new Game(FilesUtil.getContentOf("src/day23/input"));
    }
}

class Game {
    List<Elf> elves;
    HashSet<String> map;
    int maxRounds = 0;

    Game(String in) {
        elves = new ArrayList<>();
        map = new HashSet<>();
        String[] parts = in.split("\n");

        for (int i = 0; i < parts.length; i++) {
            for (int j = 0; j < parts[0].length(); j++) {
                if(parts[i].charAt(j) == '#') {
                    elves.add(new Elf(i, j));
                    map.add(label(i, j));
                }
            }
        }
    }

    int getWidth() {
        int minX = (int) 1e9;
        int maxX = (int) -1e9;
        for(Elf e: elves) {
            minX = Math.min(minX, e.x);
            maxX = Math.max(maxX, e.x);
        }

        return maxX-minX + 1;
    }

    int getHeight() {
        int minY = (int) 1e9;
        int maxY = (int) -1e9;
        for(Elf e: elves) {
            minY = Math.min(minY, e.y);
            maxY = Math.max(maxY, e.y);
        }

        return maxY-minY + 1;
    }

    int score() {
        int h = getHeight();
        int w = getWidth();
        System.out.println(h + " " + w);
        return h*w - elves.size();
    }

    void print() {
        int minX = (int) 1e9;
        int maxX = (int) -1e9;
        int minY = (int) 1e9;
        int maxY = (int) -1e9;
        for(Elf e: elves) {
            minX = Math.min(minX, e.x);
            maxX = Math.max(maxX, e.x);
            minY = Math.min(minY, e.y);
            maxY = Math.max(maxY, e.y);
        }
        for(int i=minX;i<=maxX;i++) {
            for(int j=minY;j<=maxY;j++) {
                if(map.contains(label(i, j))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    void play() {
        playRounds(10);
    }

    void playLarge() {
        playRounds(100000);
    }

    void playRounds(int rounds) {
        int elvesNextIndex = 0;
        for(int i=0;i<rounds;i++) {
            boolean didMove = false;
            HashMap<String, Integer> next = new HashMap<>();
            // decide
            for(Elf e: elves) {
                if(e.neighbors().stream().noneMatch(item -> map.contains(item))) {
                    e.nextX = null;
                    e.nextY = null;
                } else {
                    if(elvesNextIndex == 0) {
                        if (e.north().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x - 1;
                            e.nextY = e.y;
                        } else if (e.south().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x + 1;
                            e.nextY = e.y;
                        } else if (e.west().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x;
                            e.nextY = e.y - 1;
                        } else if (e.east().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x;
                            e.nextY = e.y + 1;
                        }
                    } else if(elvesNextIndex == 1) {
                        if (e.south().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x + 1;
                            e.nextY = e.y;
                        } else if (e.west().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x;
                            e.nextY = e.y - 1;
                        } else if (e.east().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x;
                            e.nextY = e.y + 1;
                        } else if (e.north().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x - 1;
                            e.nextY = e.y;
                        }
                    } else if(elvesNextIndex == 2) {
                        if (e.west().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x;
                            e.nextY = e.y - 1;
                        } else if (e.east().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x;
                            e.nextY = e.y + 1;
                        } else if (e.north().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x - 1;
                            e.nextY = e.y;
                        } else if (e.south().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x + 1;
                            e.nextY = e.y;
                        }
                    } else if(elvesNextIndex == 3) {
                        if (e.east().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x;
                            e.nextY = e.y + 1;
                        } else if (e.north().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x - 1;
                            e.nextY = e.y;
                        } else if (e.south().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x + 1;
                            e.nextY = e.y;
                        } else if (e.west().stream().noneMatch(item -> map.contains(item))) {
                            e.nextX = e.x;
                            e.nextY = e.y - 1;
                        }
                    }
                }

                if(e.nextX != null && e.nextY != null) {
                    next.put(label(e.nextX, e.nextY), next.getOrDefault(label(e.nextX, e.nextY), 0)+1);
                }
            }

            // move if possible
            for(Elf e: elves) {
                if(e.nextX != null && e.nextY != null) {
                    if(next.getOrDefault(label(e.nextX, e.nextY), 0) == 1) {
                        e.x = e.nextX;
                        e.y = e.nextY;
                        didMove = true;
                    }
                    e.nextY = null;
                    e.nextX = null;
                }
            }

            // update map
            map.clear();
            map.addAll(elves.stream().map(item -> label(item.x, item.y)).toList());
            elvesNextIndex = (elvesNextIndex + 1) % 4;
            if(!didMove) {
                maxRounds = i+1;
                return;
            }
        }
    }

    String label(int x, int y) {
        return x + ";" + y;
    }
}

class Elf {
    int x, y;
    Integer nextX, nextY;

    Elf(int x, int y) {
        this.x = x;
        this.y = y;
    }

    List<String> neighbors() {
        return Arrays.stream(new List[]{north(), south(), east(), west()}).flatMap(item -> item.stream()).toList();
    }

    List<String> north() {
        return List.of(new String[]{label(x - 1, y - 1), label(x - 1, y), label(x - 1, y + 1)});
    }
    List<String> south() {
        return List.of(new String[]{label(x + 1, y - 1), label(x + 1, y), label(x + 1, y + 1)});
    }

    List<String> east() {
        return List.of(new String[]{label(x - 1, y + 1), label(x, y + 1), label(x + 1, y + 1)});
    }
    List<String> west() {
        return List.of(new String[]{label(x - 1, y - 1), label(x, y - 1), label(x + 1, y - 1)});
    }

    String label(int x, int y) {
        return x + ";" + y;
    }
}
