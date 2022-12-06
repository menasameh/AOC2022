package day6;

import utils.FilesUtil;

import java.util.Arrays;

public class Day6 {
    public void smallSol() {
        String input = getInput();

        int count = 4;
        int ans = startOfSignal(input, count);

        System.out.println(ans);
    }

    public void largeSol() {
        String input = getInput();

        int count = 14;
        int ans = startOfSignal(input, count);

        System.out.println(ans);
    }

    int startOfSignal(String in, int count) {
        int[] arr = new int[26];

        for(int j=0;j<count;j++) {
            arr[in.charAt(j)-'a']++;
        }

        if(hasUniqueCharacters(arr, count)) {
            return count;
        }

        for(int i=count;i<in.length();i++) {
            arr[in.charAt(i-count)-'a']--;
            arr[in.charAt(i)-'a']++;

            if(hasUniqueCharacters(arr, count)) {
                return i+1;
            }
        }

        return -1;
    }

    boolean hasUniqueCharacters(int[] arr, int count) {
        return Arrays.stream(arr).filter(item -> item == 1).count() == count;
    }

   String getInput() {
        return FilesUtil.getContentOf("src/day6/input");
    }
}
