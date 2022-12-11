package day11;

import utils.FilesUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day11 {
    public void smallSol() {
        Game game = getInput();

        game.play(20, true);

    }

    public void largeSol() {
        Game game = getInput();

        game.play(10000, false);
    }

    Game getInput() {
        List<String> list = Arrays.stream(FilesUtil.getContentOf("src/day11/input").split("\n\n")).toList();
        return new Game(list);
    }

    class Game {
        List<Monkey> monkeys;

        Game(List<String> list) {
            monkeys = list.stream().map(Monkey::new).toList();
        }

        void play(int rounds, boolean withDivision) {
            for(int i=0;i<rounds;i++) {
                List<Monkey> next = monkeys.stream().map(Monkey::new).toList();
                int inspections = 0;
                int monkeysDivisor = monkeys.stream().map(mo -> mo.divisor).reduce(1, (a,b)->a*b);

                for(int j=0;j<monkeys.size();j++) {
                    Monkey monkey = monkeys.get(j);
                    for(long item: monkey.items) {
                        long value = item;
                        inspections++;
                        if(monkey.isMultiplyOperation) {
                            value = monkey.operationOperand == 0 ? value * value : value * monkey.operationOperand;
                        } else {
                            value += monkey.operationOperand;
                        }

                        value = value % (monkeysDivisor);

                        if(withDivision) {
                            value = value / 3;
                        }

                        if(value % monkey.divisor == 0) {
                            next.get(monkey.ifTrue).items.add(value);
                        } else {
                            next.get(monkey.ifFalse).items.add(value);
                        }
                    }

                    List<Long> toDelete = next.get(j).items;
                    for(long item: next.get(j).items) {
                        long value = item;
                        inspections++;
                        if(monkey.isMultiplyOperation) {
                            value = monkey.operationOperand == 0 ? value * value : value * monkey.operationOperand;
                        } else {
                            value += monkey.operationOperand;
                        }

                        value = value % (monkeysDivisor);

                        if(withDivision) {
                            value = value / 3;
                        }

                        if(value % monkey.divisor == 0) {
                            next.get(monkey.ifTrue).items.add(value);
                        } else {
                            next.get(monkey.ifFalse).items.add(value);
                        }
                    }
                    next.get(j).items.removeAll(toDelete);
                    next.get(j).inspections += inspections;
                    inspections = 0;
                }
                if (i == 9999) {
                    System.out.println();
                }
                monkeys = next;
            }

            List<Integer> inspections = monkeys.stream().map(item -> item.inspections).sorted().toList();

            BigInteger bi = BigInteger.valueOf(inspections.get(inspections.size()-1));
            bi = bi.multiply(BigInteger.valueOf(inspections.get(inspections.size()-2)));

            System.out.println(bi);
        }

    }

    class Monkey {
        List<Long> items;
        boolean isMultiplyOperation;
        int operationOperand;
        int divisor;
        int ifTrue, ifFalse;

        int inspections;

        Monkey(String in) {
            String[] parts = in.split("\n");
            items = Arrays.stream(parts[1].split(": ")[1].split(", ")).map(Long::parseLong).toList();
            isMultiplyOperation = parts[2].contains("old *");
            if(parts[2].contains("old * old")) {
                operationOperand = 0;
            } else {
                operationOperand = Integer.parseInt(parts[2].split("old [+*] ")[1]);
            }
            divisor = Integer.parseInt(parts[3].split("divisible by ")[1]);
            ifTrue = Integer.parseInt(parts[4].split("throw to monkey ")[1]);
            ifFalse = Integer.parseInt(parts[5].split("throw to monkey ")[1]);
            inspections = 0;
        }

        Monkey(Monkey in) {
            items = new ArrayList<>();
            isMultiplyOperation = in.isMultiplyOperation;
            operationOperand = in.operationOperand;
            divisor = in.divisor;
            ifTrue = in.ifTrue;
            ifFalse = in.ifFalse;
            inspections = in.inspections;
        }
    }
}
