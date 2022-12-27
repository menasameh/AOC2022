package day25;

import utils.FilesUtil;

import java.util.*;

public class Day25 {
    public void smallSol() {
        Game game = getInput();
        long value = game.play();

        System.out.println(value);
        // guessing character by character :"D
        System.out.println(game.SNAFUtoDec("2-=2-0=-0-=0200=--21"));
    }

    public void largeSol() {
        Game game = getInput();
    }

    Game getInput() {
        return new Game(Arrays.stream(FilesUtil.getContentOf("src/day25/input").split("\n")).toList());
    }
}

class Game {
    List<String> nums;
    long dec = 33078355623611L;
    String ans = "";

    Game(List<String> nums) {
        this.nums = nums;
    }

    long play() {
        return nums.stream().map(this::SNAFUtoDec).mapToLong(Long::longValue).sum();
    }

    void dp(String s, int index) {
        if(index == 0) {
            if(SNAFUtoDec(s) == dec) {
                ans = s;
            }
            return;
        }

        dp(s+"2", index - 1);
        dp(s+"1", index - 1);
        dp(s+"0", index - 1);
        dp(s+"-", index - 1);
        dp(s+"=", index - 1);
    }

    long SNAFUtoDec(String in) {
        long ans = 0;

        for(int i=0;i<in.length();i++) {
            long value = 0;
            if(in.charAt(i) == '2') value = 2;
            else if (in.charAt(i) == '1') value = 1;
            else if (in.charAt(i) == '0') value = 0;
            else if (in.charAt(i) == '-') value = -1;
            else if (in.charAt(i) == '=') value = -2;

            ans += (value * Math.pow(5, in.length() - i - 1));
        }

        return ans;
    }
}