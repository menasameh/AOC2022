package day18;

import utils.FilesUtil;

import java.util.*;

public class Day18 {
    public void smallSol() {
        Game game = getInput();

        System.out.println(game.calcSurface());
    }

    public void largeSol() {
        Game game = getInput();

        System.out.println(game.calcExternalSurface());
    }

    Game getInput() {
        return new Game(Arrays.stream(FilesUtil.getContentOf("src/day18/input").split("\n")).map(Cube::new).toList());
    }
}

class Game {
    List<Cube> cubes;

    Game(List<Cube> cubes) {
        this.cubes = cubes;
    }

    int calcSurface() {
        int sum = 0;

        for(Cube c: cubes) {
            sum += 6 - c.neighbors().stream().filter( n -> cubes.contains(n)).count();
        }

        return sum;
    }

    void space(Cube min, Cube max) {
        min.x = max.x = cubes.get(0).x;
        min.y = max.y = cubes.get(0).y;
        min.z = max.z = cubes.get(0).z;
        for(Cube c: cubes) {
            min.x = Math.min(min.x, c.x);
            min.y = Math.min(min.y, c.y);
            min.z = Math.min(min.z, c.z);

            max.x = Math.max(max.x, c.x);
            max.y = Math.max(max.y, c.y);
            max.z = Math.max(max.z, c.z);
        }
        min.x--;
        min.y--;
        min.z--;
        max.x++;
        max.y++;
        max.z++;
    }

    int calcExternalSurface() {
        int sum = 0;
        Cube min = new Cube(0, 0 ,0);
        Cube max = new Cube(0, 0 ,0);
        space(min, max);

        Stack<Cube> externalCubes = new Stack<>();
        externalCubes.add(min);
        HashSet<Cube> enclosed = new HashSet<>(externalCubes);

        while(!externalCubes.isEmpty()) {
            Cube c = externalCubes.pop();
            for(Cube n: c.boundedNeighbors(min, max)) {
                if(enclosed.contains(n)) {
                    continue;
                }

                if(cubes.contains(n)) {
                    sum++;
                } else {
                    enclosed.add(n);
                    externalCubes.add(n);
                }
            }
        }
        return sum;
    }

}

class Cube {
    int x, y, z;

    Cube(String in) {
        String[] parts = in.split(",");
        this.x = Integer.parseInt(parts[0]);
        this.y = Integer.parseInt(parts[1]);
        this.z = Integer.parseInt(parts[2]);
    }

    Cube(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    List<Cube> neighbors() {
        return List.of(new Cube[]{
                new Cube(x - 1, y, z), new Cube(x + 1, y, z),
                new Cube(x, y - 1, z), new Cube(x, y + 1, z),
                new Cube(x, y, z - 1), new Cube(x , y, z + 1)
        });
    }

    List<Cube> boundedNeighbors(Cube min, Cube max) {
        return neighbors().stream().filter(item ->
                item.x >= min.x && item.x <= max.x &&
                        item.y >= min.y && item.y <= max.y &&
                        item.z >= min.z && item.z <= max.z
                ).toList();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cube cube = (Cube) o;
        return x == cube.x && y == cube.y && z == cube.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}