package day3;

import utils.FilesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day3 {
    public void smallSol() {
        List<List<String>> list = getInput();

        int ans = list.stream()
                .map(l -> priority(l.get(0), l.get(1), l.get(1)))
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println(ans);
    }

    public void largeSol() {
        Collection<List<String>> list = getLargeInput();

        int ans = list.stream()
                .map(l -> priority(l.get(0), l.get(1), l.get(2)))
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println(ans);

    }

    int priority(String a, String b, String d) {
        int[] arr = new int[54];

        for(char c : a.toCharArray()) {
            int l = c - 'a';
            int u = c - 'A' + 26;
            if(c >= 'a' && c <= 'z') {
                arr[l] = 1;
            } else {
                arr[u] = 1;
            }
        }

        for(char c : b.toCharArray()) {
            int l = c - 'a';
            int u = c - 'A' + 26;
            if(c >= 'a' && c <= 'z') {
                if(arr[l] == 1) arr[l] = 2;
            } else {
                if(arr[u] == 1) arr[u] = 2;
            }
        }

        for(char c : d.toCharArray()) {
            int l = c - 'a';
            int u = c - 'A' + 26;
            if(c >= 'a' && c <= 'z') {
                if(arr[l] == 2) return l+1;
            } else {
                if(arr[u] == 2) return u+1;
            }
        }
        return -1;
    }
    List<List<String>> getInput() {
        String input = FilesUtil.getContentOf("src/day3/input");
        return Arrays.stream(input.split("\n"))
                .map(item -> Arrays.asList(item.substring(0, (item.length()/2)), item.substring(item.length()/2)))
                .toList();
    }

    Collection<List<String>> getLargeInput() {
        int chunkSize = 3;
        AtomicInteger counter = new AtomicInteger();
        String input = FilesUtil.getContentOf("src/day3/input");
        return Arrays.stream(input.split("\n")).collect(Collectors.groupingBy(i -> counter.getAndIncrement() / chunkSize))
                        .values();
    }
}
