package day7;

import utils.FilesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day7 {
    public void smallSol() {
        Game game = getInput();
        game.buildTree();
        game.calcSize(game.head);

        List<Node> dirs = new ArrayList<>();
        game.listDirSizes(game.head, dirs);

        long ans = dirs.stream().filter(item -> item.size <= 100000).mapToLong(num -> num.size).sum();

        System.out.println(ans);
    }

    public void largeSol() {
        Game game = getInput();
        game.buildTree();
        game.calcSize(game.head);

        List<Node> dirs = new ArrayList<>();
        game.listDirSizes(game.head, dirs);

        long freeSpace = 70000000 - game.head.size;
        long neededSpace = 30000000 - freeSpace;

        long ans = dirs.stream().filter(item -> item.size >= neededSpace).mapToLong(num -> num.size).min().getAsLong();

        System.out.println(ans);

    }

    Game getInput() {
        List<String> list = Arrays.stream(FilesUtil.getContentOf("src/day7/input").split("\n\\$")).toList();

        return new Game(list);
    }

    class Game {
        Node head;
        Node curr;
        List<Command> commands;

        Game(List<String> list) {
            head = new Node("/", 0, true, null);
            curr = head;
            commands = list.stream().map(String::trim).map(Command::new).toList();
        }

        void buildTree() {
            for(Command c: commands) {
                if(c.isChangeDir) {
                    if(c.dir.equals("/")) {
                        curr = head;
                    } else if(c.dir.equals("..")) {
                        curr = curr.parent;
                    } else {
                        for(Node n: curr.children) {
                            if(n.name.equals(c.dir)) {
                                curr = n;
                                break;
                            }
                        }
                    }
                } else {
                    for(Result r: c.results) {
                        curr.children.add(new Node(r.name, r.size, r.isDir, curr));
                    }
                }
            }
        }

        long calcSize(Node start) {
            if(start.children.isEmpty()) {
                return start.size;
            }

            long size = 0;
            for(Node n: start.children) {
                size += calcSize(n);
            }

            start.size = size;
            return size;
        }

        void listDirSizes(Node start, List<Node> list) {
            if(start.children.isEmpty()) {
                return;
            }

            if(start.isDir) {
                list.add(start);
            }

            for(Node n: start.children) {
                listDirSizes(n, list);
            }
        }

    }

    class Command {
        boolean isChangeDir;
        String dir;
        List<Result> results;

        Command(String in) {
            if(in.startsWith("cd")) {
                String[] parts = in.split(" ");
                isChangeDir = true;
                dir = parts[1];
                results = new ArrayList<>();
            } else {
                isChangeDir = false;
                dir = null;
                String[] parts = in.split("\n");
                results = new ArrayList<>();
                for(int i=1;i<parts.length;i++) {
                    results.add(new Result(parts[i]));
                }
            }
        }
    }

    class Result {
        boolean isDir;
        String name;
        long size;

        Result(String in) {
            String[] parts = in.split(" ");
            if(in.startsWith("dir")) {
                isDir = true;
                name = parts[1];
                size = 0;
            } else {
                isDir = false;
                name = parts[1];
                size = Long.parseLong(parts[0]);
            }
        }
    }

    class Node {
        List<Node> children;
        long size;
        String name;
        Node parent;
        boolean isDir;

        Node(String name, long size, boolean isDir, Node parent) {
            this.name = name;
            this.size = size;
            children = new ArrayList<>();
            this.parent = parent;
            this.isDir = isDir;
        }
    }
}
