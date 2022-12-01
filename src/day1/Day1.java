package day1;

import utils.FilesUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {
    public void smallSol() {
        List<List<Integer>> list = getInput();

        int ans = list.stream()
                .map(i-> i.stream()
                        .mapToInt(Integer::intValue)
                        .sum())
                .mapToInt(Integer::intValue)
                .max()
                .getAsInt();
        System.out.println(ans);
    }

    public void largeSol() {
        List<List<Integer>> list = getInput();

        int ans = list.stream()
                .map(i-> i.stream()
                        .mapToInt(Integer::intValue)
                        .sum())
                .mapToInt(Integer::intValue)
                .sorted()
                .skip(list.size()-3)
                .sum();
        System.out.println(ans);
    }

    List<List<Integer>> getInput() {
        String input = FilesUtil.getContentOf("src/day1/input");
        return Arrays.stream(input.split("\n\n"))
                .map(list -> Arrays.stream(list.split("\n"))
                        .map(Integer::valueOf)
                        .toList())
                .toList();
    }
}