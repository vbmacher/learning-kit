package cz.zoom.whiteboard;

import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class Tasks implements Iterable<Task> {
    private final List<Task> tasks = new CopyOnWriteArrayList<Task>();
    
    public Tasks(Task[] tasks) {
        this.tasks.addAll(Arrays.asList(tasks));
    }
    
    public BufferedImage[] render() throws IOException, WriterException {
        List<BufferedImage> taskImages = new LinkedList<BufferedImage>();
        for (Task task : tasks) {
            taskImages.add(task.render());
        }
        return taskImages.toArray(new BufferedImage[0]);
    }
    
    public int size() {
        return tasks.size();
    }
    
    public Task[] getTasks() {
        return tasks.toArray(new Task[0]);
    }
    
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
    
    public boolean add(Task task) {
        return tasks.add(task);
    }
    
    public boolean addAll(Collection<? extends Task> tasks) {
        return this.tasks.addAll(tasks);
    }
    
    public boolean remove(Task task) {
        return tasks.remove(task);
    }
    
}
