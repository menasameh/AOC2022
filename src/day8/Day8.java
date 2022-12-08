package day8;

import utils.FilesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day8 {
    public void smallSol() {
        Game game = getInput();

        System.out.println(game.getVisibleCount());
    }

    public void largeSol() {
        Game game = getInput();

        System.out.println(game.getScenicScore());
    }

    Game getInput() {
        List<String> list = Arrays.stream(FilesUtil.getContentOf("src/day8/input").split("\n")).toList();
        return new Game(list);
    }

    class Game {
        int[][] grid;
        Game(List<String> list) {
            grid = new int[list.size()][];

            for(int i=0;i<list.size();i++) {
                grid[i] = new int[list.get(i).length()];
                for(int j=0;j<list.get(i).length();j++) {
                    grid[i][j] = list.get(i).charAt(j) - '0';
                }
            }
        }

        int getVisibleCount() {
            int count = grid.length * 2 + grid[0].length * 2 - 4;
            for(int i=1;i< grid.length - 1;i++) {
                for(int j=1;j<grid[i].length - 1;j++) {
                    boolean[] isVisible = {true, true, true, true};

                    for(int k=i+1;k<grid.length;k++) {
                        if(grid[i][j] <= grid[k][j]) {
                            isVisible[0] = false;
                            break;
                        }
                    }

                    for(int k=i-1;k>=0;k--) {
                        if(grid[i][j] <= grid[k][j]) {
                            isVisible[1] = false;
                            break;
                        }
                    }

                    for(int k=j+1;k<grid[i].length;k++) {
                        if(grid[i][j] <= grid[i][k]) {
                            isVisible[2] = false;
                            break;
                        }
                    }

                    for(int k=j-1;k>=0;k--) {
                        if(grid[i][j] <= grid[i][k]) {
                            isVisible[3] = false;
                            break;
                        }
                    }

                    if(isVisible[0] ||isVisible[1] ||isVisible[2] ||isVisible[3]) {
                        count++;
                    }
                }
            }
            return count;
        }

        int getScenicScore() {
            int max = 0;
            for(int i=1;i< grid.length - 1;i++) {
                for(int j=1;j<grid[i].length - 1;j++) {
                    int[] score = {0, 0, 0, 0};

                    for(int k=i+1;k<grid.length;k++) {
                        score[0] = k-i;
                        if(grid[i][j] <= grid[k][j]) {
                            break;
                        }
                    }

                    for(int k=i-1;k>=0;k--) {
                        score[1] = i-k;
                        if(grid[i][j] <= grid[k][j]) {
                            break;
                        }
                    }

                    for(int k=j+1;k<grid[i].length;k++) {
                        score[2] = k-j;
                        if(grid[i][j] <= grid[i][k]) {

                            break;
                        }
                    }

                    for(int k=j-1;k>=0;k--) {
                        score[3] = j-k;
                        if(grid[i][j] <= grid[i][k]) {

                            break;
                        }
                    }

                    max = Math.max(max, score[0]*score[1]*score[2]*score[3]);
                }
            }
            return max;
        }
    }
}
