package student;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SortedList<T extends Comparable<? super T>> extends AbstractList<T> {
    private final List<T> list;

    public SortedList() {
        list = new ArrayList<>();
    }

    public SortedList(Collection<T> other) {
        list = new ArrayList<>(other);
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean add(T value) {
        boolean r = list.add(value);
        Collections.<T>sort(list);
        return r;
    }

    @Override
    public T remove(int index) {
        return list.remove(index);
    }

    @Override
    public T set(int index, T element) {
        T r = list.set(index, element);
        Collections.sort(list);
        return r;
    }
}
