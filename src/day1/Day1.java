package day1;

import utils.FilesUtil;

import java.util.Arrays;
import java.util.List;

public class Day1 {
    public void smallSol() {
        List<Integer> list = getInput();
        System.out.println(list);
    }

    public void largeSol() {
        List<Integer> list = getInput();
    }

    List<Integer> getInput() {
        String input = FilesUtil.getContentOf("src/day1/input");
        return Arrays.stream(input.split("\n")).map(Integer::valueOf).toList();
    }
}
