package ADT;

import java.util.Iterator;

/*
 * Title : List Abtract Data Type
 * Description :  A linear collection of entries with generic data type<T>, which allows duplicate entries.
                  It allows adding an entry at a specific position or by default at the end of the list.
                  Other operations like removing and searching are allowed with specified positions.
                  The position of a list starts with 1.
 * Purpose : To store a collection of entries in list without any restriction
 */
public interface ListInterface<T extends Comparable<T>> {

    //Add Entry to the end
    public void add(T newEntry);

    //
    public boolean add(T newEntry, Integer newPosition);

    //Get Entry
    public T get(Integer givenPosition);

    //Remove Entry
    public T remove(Integer givenPosition);

    //Replace Entry
    public boolean replace(T newEntry, Integer givenPosition);

    //Check List is Empty
    public boolean isEmpty();

    //Check List is Full
    public boolean isFull();

    //Clear List
    public void clear();

    //Get Number of Entries
    public int getNumberOfEntries();

    //Append List
    public void append(ListInterface<T> newList);

    //Contains
    public boolean contains(T entry);

    //Iterator
    public Iterator iterator();
}
