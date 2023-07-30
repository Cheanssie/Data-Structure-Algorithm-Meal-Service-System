package ADT;

import java.util.Iterator;


public interface QueueInterface<T extends Comparable<T>> {
    
    //ADD ENTRY
    public boolean enqueue(T newEntry);
    
    //REMOVE ENTRY
    public T dequeue();
    
    //GET FRONT ENTRY
    public T peek();
    
    //GET REAR ENTRY
    public T getRear();
    
    //CHECK IF EMPTY
    public boolean isEmpty();
    
    //CHECK IF FULL
    public boolean isFull();
    
    //CLEAR QUEUE ENTRIES
    public void clear();
    
    //GET QUEUE ARRAY SIZE
    public int size();
    
    //GET NUMBER OF ENTRIES
    public int getNumberOfEntries();
    
    //Iterator
    public Iterator iterator();
}
