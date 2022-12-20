package day20;

import utils.FilesUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day20 {
    public void smallSol() {
        Game game = getInput();

        System.out.println(game.search());
    }

    public void largeSol() {
        Game game = getInput();

        System.out.println(game.searchLarge());
    }

    Game getInput() {
        return new Game(Arrays.stream(FilesUtil.getContentOf("src/day20/input").split("\n")).map(Long::parseLong).toList());
    }
}

class Game {
    List<Long> nums;

    Game(List<Long> nums) {
        this.nums = nums;
    }

    long search() {
        List<Long> finalList = mix(nums, 1);

        int zeroIndex = finalList.indexOf(0L);

        return finalList.get((zeroIndex+1000) % finalList.size()) +
                finalList.get((zeroIndex+2000) % finalList.size()) +
                finalList.get((zeroIndex+3000) % finalList.size());
    }

    long searchLarge() {
        List<Long> finalList = nums.stream().map(item -> item * 811589153L).toList();
        finalList = mix(finalList, 10);

        int zeroIndex = finalList.indexOf(0L);

        return finalList.get((zeroIndex+1000) % finalList.size()) +
                finalList.get((zeroIndex+2000) % finalList.size()) +
                finalList.get((zeroIndex+3000) % finalList.size());
    }

    List<Long> mix(List<Long> in, int count) {
        DLL ll = new DLL();
        for(Long i : in) {
            ll.append(i);
        }

        List<DLL.Node> nodesInOrder = ll.toList();
        ll.connectEnds();

        for(int i=0;i<count;i++) {
            int index = 0;
            while(index < in.size()) {
                DLL.Node n = nodesInOrder.get(index);
                index++;
                ll.move(n, n.data);
            }
        }

        return ll.toList().stream().map(item -> item.data).toList();
    }
}

// Doubly from GFG: https://www.geeksforgeeks.org/introduction-and-insertion-in-a-doubly-linked-list/
class DLL {

    // Head of list
    Node head;

    // Doubly Linked list Node
    class Node {
        long data;
        Node prev;
        Node next;

        // Constructor to create a new node
        // next and prev is by default initialized as null
        Node(long d) { data = d; }

    }

    // Add a node at the end of the list
    void append(long new_data)
    {
        /* 1. allocate node
         * 2. put in the data */
        Node new_node = new Node(new_data);

        Node last = head; /* used in step 5*/

        /* 3. This new node is going to be the last node, so
         * make next of it as NULL*/
        new_node.next = null;

        /* 4. If the Linked List is empty, then make the new
         * node as head */
        if (head == null) {
            new_node.prev = null;
            head = new_node;
            return;
        }

        /* 5. Else traverse till the last node */
        while (last.next != null)
            last = last.next;

        /* 6. Change the next of last node */
        last.next = new_node;

        /* 7. Make last node as previous of new node */
        new_node.prev = last;
    }

    public void connectEnds() {
        Node last = head;

        while (last.next != null) {
            last = last.next;
        }

        last.next = head;
        head.prev = last;
    }

    public void move(Node toMove, long steps)
    {
        if(steps == 0) {
            return;
        }
        steps = reducedMoves(steps, toList().size()-1);
       Node prev = toMove.prev;
       Node next = toMove.next;

        toMove.prev = null;
        toMove.next = null;

        prev.next = next;
        next.prev = prev;

       Node newPrev = prev;

       while(steps != 0) {
           if(steps > 0) {
               newPrev = newPrev.next;
               steps--;
           } else {
               newPrev = newPrev.prev;
               steps++;
           }
       }

       InsertAfter(newPrev, toMove);
    }

    long reducedMoves(long moves, int size) {
        return (moves + 811589153L * 1000 * size) % size;
    }

    /* Given a node as prev_node, insert a new node after the
     * given node */
    public void InsertAfter(Node prev_Node, Node new_node)
    {

        /*1. check if the given prev_node is NULL */
        if (prev_Node == null) {
            System.out.println(
                    "The given previous node cannot be NULL ");
            return;
        }

        /* 4. Make next of new node as next of prev_node */
        new_node.next = prev_Node.next;

        /* 5. Make the next of prev_node as new_node */
        prev_Node.next = new_node;

        /* 6. Make prev_node as previous of new_node */
        new_node.prev = prev_Node;

        /* 7. Change previous of new_node's next node */
        if (new_node.next != null)
            new_node.next.prev = new_node;
    }

    public List<Node> toList() {
        Node mover = head;
        List<Node> list = new ArrayList<>();

        while (mover != null) {
            list.add(mover);
            mover = mover.next;
            if(mover == head) {
                break;
            }
        }
        return list;
    }
}