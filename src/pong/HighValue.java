package pong;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class HighValue<T extends Serializable & Comparable<T>> implements Serializable, Iterable<T> {

    private final List<T> sortedList;
    private final int maxSize;

    public HighValue(int maxSize) {
        this.maxSize = maxSize;
        sortedList = new ArrayList<>(maxSize);
    }

    public void add(T... items) {
        for (T item : items) {
            add(item);
        }
    }

    public boolean add(T item) {
        if (!wouldMakeIt(item)) {
            return false;
        } else {
            addItem(item);
            if (sortedList.size() > maxSize) {
                removeSmallest();
            }
            return true;
        }

    }

    private void removeSmallest() {
        sortedList.remove(0);
    }

    private void addItem(T item) {
        sortedList.add(findIndex(item), item);
    }

    private int findIndex(T item) {
        int index = Collections.binarySearch(sortedList, item);
        if (index >= 0) {
            return index;
        } else {
            return convertToIndex(index);
        }
    }

    private int convertToIndex(int index) {
        return Math.abs(index) - 1;
    }

    public boolean wouldMakeIt(T item) {
        return sortedList.size() < maxSize || item.compareTo(sortedList.get(0)) > 0;
    }

    public int size() {
        return sortedList.size();
    }

    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public Iterator<T> iterator() {
        return Lists.reverse(sortedList).iterator();
    }

}
