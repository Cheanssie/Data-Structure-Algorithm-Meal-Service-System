package ADT;

import java.util.Iterator;

public interface SetInterface<T extends Comparable<T>> {

    public boolean add(T newElement);//add a new element that does not duplicate

    public T remove(T anElement);//remove an element from the set

    public boolean checkSubset(SetInterface<T> anotherSet);//check if currentSet contains all the elements in anotherSet

    public void union(SetInterface<T> anotherSet);//combine all the element from currentSet and anotherSet into currentSet

    public SetInterface<T> intersection(SetInterface<T> anotherSet);//extract all the element that exist in currentSet and anotherSet

    public boolean isEmpty();//check if the set is empty

    public void clear();//clear all element in the set

    public int getSize();//get the number of element in the set

    public Iterator iterator();//To traverse each each element in the set

    public boolean contains(T element);
}
