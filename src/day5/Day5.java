package day5;

import utils.FilesUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Day5 {
    public void smallSol() {
        Game game = getInput();

        game.simulateActions();
        game.printTopStacks();
    }

    public void largeSol() {
        Game game = getInput();

        game.simulateActionsMultiMove();
        game.printTopStacks();
    }

   Game getInput() {
        String input = FilesUtil.getContentOf("src/day5/input");
        return new Game(Arrays.stream(input.split("\n\n"))
                .toList());
    }

    class Game {
        Stack<Character>[] stacks;
        List<Action> actions;

        Game(List<String> in) {
            String stacksString = in.get(0);
            String ActionString = in.get(1);

            List<String> stacksLines = Arrays.stream(stacksString.split("\n")).toList();
            int count = Arrays.stream(stacksLines.get(stacksLines.size() - 1).trim().split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .max()
                    .getAsInt();

            stacks = new Stack[count];

            int startIndex = 1;
            for(int i=0;i<count;i++) {
                int cur = startIndex + 4*i;
                stacks[i] = new Stack<>();
                for(int j=stacksLines.size()-2;j>=0;j--) {
                    if(stacksLines.get(j).length() < cur) break;
                    char item = stacksLines.get(j).charAt(cur);
                    if(item == ' ') break;
                    stacks[i].push(item);
                }
            }

            actions = Arrays.stream(ActionString.split("\n")).map(Action::new).toList();
        }

        void simulateActions() {
            for(Action a : actions) {
                for(int i =0;i<a.count;i++) {
                    stacks[a.to-1].push(stacks[a.from-1].pop());
                }
            }
        }

        void simulateActionsMultiMove() {
            for(Action a : actions) {
                Stack<Character> temp = new Stack<>();
                for(int i =0;i<a.count;i++) {
                    temp.push(stacks[a.from-1].pop());
                }
                for(int i =0;i<a.count;i++) {
                    stacks[a.to-1].push(temp.pop());
                }
            }
        }

        void printTopStacks() {
            Arrays.stream(stacks).map(Stack::peek).forEach(System.out::print);
        }
    }

    class Action {
        int from, to, count;

        Action(String in) {
            // move 3 from 2 to 9
            String[] parts = in.split(" ");
            this.from = Integer.parseInt(parts[3]);
            this.to = Integer.parseInt(parts[5]);
            this.count = Integer.parseInt(parts[1]);
        }
    }
}
