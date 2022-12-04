package day4;

import utils.FilesUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day4 {
    public void smallSol() {
        List<List<Range>> list = getInput();

        int ans = list.stream()
                .filter(l -> l.get(0).fullyContains(l.get(1)) || l.get(1).fullyContains(l.get(0)))
                .count();

        System.out.println(ans);
    }

    public void largeSol() {
        List<List<Range>> list = getInput();

        int ans = list.stream()
                .filter(l -> l.get(0).overlaps(l.get(1)) || l.get(1).overlaps(l.get(0)))
                .count();

        System.out.println(ans);
    }

    List<List<Range>> getInput() {
        String input = FilesUtil.getContentOf("src/day4/input");
        return Arrays.stream(input.split("\n"))
                .map(item -> Arrays.stream(item.split(",")).map(Range::new).toList())
                .toList();
    }

    class Range {
        int lo, hi;
        Range(String in) {
            int[] parts = Arrays.stream(in.split("-")).mapToInt(Integer::valueOf).toArray();
            lo = parts[0];
            hi = parts[1];
        }

        boolean fullyContains(Range other) {
            return other.lo >= lo && other.hi <= hi;
        }

        boolean overlaps(Range other) {
            return (other.lo >= lo && other.lo <= hi) || (other.hi >= lo && other.hi <= hi);
        }
    }
}
