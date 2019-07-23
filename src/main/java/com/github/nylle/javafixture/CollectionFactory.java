package com.github.nylle.javafixture;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
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

public class CollectionFactory {

    public <T> T create(final Class<T> type) {
        //TODO: I'd be surprised if this works (just playing around)
        return (T) createFromInterface(type);
    }

    Collection<?> createFromInterface(final Class<?> interfaceType) {

        if (List.class.isAssignableFrom(interfaceType)) {
            return new ArrayList<>();
        }

        if (NavigableSet.class.isAssignableFrom(interfaceType)) {
            return new TreeSet<>();
        }

        if (SortedSet.class.isAssignableFrom(interfaceType)) {
            return new TreeSet<>();
        }

        if (Set.class.isAssignableFrom(interfaceType)) {
            return new HashSet<>();
        }

        if (BlockingDeque.class.isAssignableFrom(interfaceType)) {
            return new LinkedBlockingDeque<>();
        }

        if (Deque.class.isAssignableFrom(interfaceType)) {
            return new ArrayDeque<>();
        }

        if (TransferQueue.class.isAssignableFrom(interfaceType)) {
            return new LinkedTransferQueue<>();
        }

        if (BlockingQueue.class.isAssignableFrom(interfaceType)) {
            return new LinkedBlockingQueue<>();
        }

        if (Queue.class.isAssignableFrom(interfaceType)) {
            return new LinkedList<>();
        }

        return null;
    }

}

