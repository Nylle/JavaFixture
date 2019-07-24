package com.github.nylle.javafixture.testobjects;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class TestCollections {

    private List<String> list;
    private ArrayList<String> arrayList;

    private Set<String> set;
    private NavigableSet<String> navigableSet;
    private SortedSet<String> sortedSet;
    private HashSet<String> hashSet;
    private TreeSet<String> treeSet;

    private Deque<String> deque;
    private ArrayDeque<String> arrayDeque;
    private BlockingDeque<String> blockingDeque;
    private LinkedBlockingDeque<String> linkedBlockingDeque;

    private Queue<String> queue;
    private LinkedList<String> linkedList;
    private TransferQueue<String> transferQueue;
    private LinkedTransferQueue<String> linkedTransferQueue;
    private BlockingQueue<String> blockingQueue;
    private LinkedBlockingQueue<String> linkedBlockingQueue;

    public List<String> getList() {
        return list;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public Set<String> getSet() {
        return set;
    }

    public NavigableSet<String> getNavigableSet() {
        return navigableSet;
    }

    public SortedSet<String> getSortedSet() {
        return sortedSet;
    }

    public HashSet<String> getHashSet() {
        return hashSet;
    }

    public TreeSet<String> getTreeSet() {
        return treeSet;
    }

    public Deque<String> getDeque() {
        return deque;
    }

    public ArrayDeque<String> getArrayDeque() {
        return arrayDeque;
    }

    public BlockingDeque<String> getBlockingDeque() {
        return blockingDeque;
    }

    public LinkedBlockingDeque<String> getLinkedBlockingDeque() {
        return linkedBlockingDeque;
    }

    public Queue<String> getQueue() {
        return queue;
    }

    public LinkedList<String> getLinkedList() {
        return linkedList;
    }

    public TransferQueue<String> getTransferQueue() {
        return transferQueue;
    }

    public LinkedTransferQueue<String> getLinkedTransferQueue() {
        return linkedTransferQueue;
    }

    public BlockingQueue<String> getBlockingQueue() {
        return blockingQueue;
    }

    public LinkedBlockingQueue<String> getLinkedBlockingQueue() {
        return linkedBlockingQueue;
    }
}
