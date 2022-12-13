package day13;

import utils.FilesUtil;

import java.util.*;

public class Day13 {
    public void smallSol() {
        List<PacketPair> game = getInput();

        int ans = 0;
        for(int i=0;i<game.size();i++) {
            if(game.get(i).isInCorrectOrder()) {

                ans+= i+1;
            }
        }
        System.out.println(ans);
    }

    public void largeSol() {
        List<PacketPair> game = getInput();

        Pack leftValue = new Pack();
        leftValue.value = 2;
        Pack left = new Pack();
        left.children.add(new Pack());
        left.children.get(0).children.add(leftValue);
        Packet leftPacket = new Packet(left);

        Pack rightValue = new Pack();
        rightValue.value = 6;
        Pack right = new Pack();
        right.children.add(new Pack());
        right.children.get(0).children.add(rightValue);
        Packet rightPacket = new Packet(right);

        List<Packet> items = new ArrayList<>();
        for(PacketPair p: game) {
            items.add(p.left);
            items.add(p.right);
        }
        items.add(leftPacket);
        items.add(rightPacket);


        items.sort(Packet::checkOrder);

        for(Packet p : items) {
            p.print();
        }

        int l = 0;
        int r = 0;

        for(int i=0;i<items.size();i++) {
            if(items.get(i).equals(leftPacket)) {
                l = items.size()-i;
            }

            if(items.get(i).equals(rightPacket)) {
                r = items.size()-i;
            }
        }

        System.out.println(l * r);
    }

    List<PacketPair> getInput() {
        return Arrays.stream(FilesUtil.getContentOf("src/day13/input").split("\n\n")).map(PacketPair::new).toList();
    }

    class PacketPair {
        Packet left, right;

        PacketPair(Packet left, Packet right) {
            this.left = left;
            this.right = right;
        }
        PacketPair(String in) {
            String[] parts = in.split("\n");
            left = new Packet(parts[0]);
            right = new Packet(parts[1]);
        }

        boolean isInCorrectOrder() {
            return left.checkOrder(right) == 1;
        }
    }

    class Packet {

        Pack head;

        Packet(Pack head) {
            this.head = head;
        }

        Packet(String in) {
            head = new Pack();
            Pack curr = head;
            for(int i=0;i<in.length();i++) {
                if(in.charAt(i) == '[') {
                    Pack newChild = new Pack();
                    newChild.parent = curr;
                    curr.children.add(newChild);
                    curr = curr.children.get(curr.children.size()-1);
                } else if(in.charAt(i) >= '0' && in.charAt(i) <= '9') {
                    for(int j=i;j<in.length();j++) {
                            if(in.charAt(j) == ',' || in.charAt(j) == ']') {
                                String s = (String) in.subSequence(i, j);
                                Pack newChild = new Pack();
                                newChild.parent = curr;
                                newChild.value = Integer.parseInt(s);
                                curr.children.add(newChild);
                                i = j-1;
                                break;
                            }
                        }
                } else if(in.charAt(i) == ']') {
                    curr = curr.parent;
                }
            }
        }

        void print() {
            print(head);
            System.out.println();
        }

        private void print(Pack head) {
            if(head.children.isEmpty()) {
                System.out.print(head.value + ",");
                return;
            }

            for(Pack p : head.children) {
                print(p);
            }
        }

        private int checkOrder(Packet right) {
            return checkOrder(head, right.head);
        }

        private int checkOrder(Pack left, Pack right) {
            if(left.value != null && right.value != null) {
                if(left.value < right.value) {
                    return 1;
                } else if(left.value.equals(right.value)) {
                    return 0;
                } else {
                    return -1;
                }
            } else if(left.value != null) {
                Pack upgradedLeft = new Pack();
                upgradedLeft.children.add(left);
                return checkOrder(upgradedLeft, right);
            } else if(right.value != null) {
                Pack upgradedRight = new Pack();
                upgradedRight.children.add(right);
                return checkOrder(left, upgradedRight);
            }

            int i=0;
            while(i < left.children.size() && i < right.children.size()) {
                int value = checkOrder(left.children.get(i), right.children.get(i));

                if(value != 0) return value;

                i++;
            }

            if(i == left.children.size()) {
                if(left.children.size() == right.children.size()) {
                    return 0;
                }
                return 1;
            }

            return -1;
        }
    }

    class Pack {
        List<Pack> children;
        Integer value;
        Pack parent;

        Pack() {
            children = new ArrayList<>();
            value = null;
        }
    }
}
