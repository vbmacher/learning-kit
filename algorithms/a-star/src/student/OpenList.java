package student;

import cz.cvut.atg.zui.astar.AbstractOpenList;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class OpenList<T> extends AbstractOpenList<T> {
    private final Queue<TwithCost> items = new PriorityQueue<>(Comparator.comparingDouble(o -> o.cost));

    private double nextCost;

    void setNextCost(double nextCost) {
        this.nextCost = nextCost;
    }

    @Override
    protected boolean addItem(T item) {
        return items.add(new TwithCost(item, nextCost));
    }

    void update(T item, double newCost) {
        TwithCost found = null;
        for (TwithCost inqueue : items ) {
            if (inqueue.item.equals(item)) {
                found = inqueue;
                break;
            }
        }
        if (found != null)  {
            items.remove(found);
            found.cost = newCost;
            items.add(found);
        }
    }

    boolean contains(T item) {
        for (TwithCost inqueue : items) {
            if (inqueue.item.equals(item)) {
                return true;
            }
        }
        return false;
    }

    T retrieveBest() {
        return items.poll().item;
    }

    boolean isEmpty() {
        return items.isEmpty();
    }

    private final class TwithCost {
        private final T item;
        private double cost;

        private TwithCost(T item, double cost) {
            this.item = item;
            this.cost = cost;
        }
    }

}
