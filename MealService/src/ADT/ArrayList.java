/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ADT;

import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author user
 * @param <T>
 */
public class ArrayList<T extends Comparable<T>> implements ListInterface<T>, Serializable {

    private T[] array;
    private int numberOfEntries;
    private static int DEFAULT_CAPACITY = 10;

    public ArrayList() {
        array = (T[]) new Comparable[DEFAULT_CAPACITY];
    }

    public ArrayList(int size) {
        array = (T[]) new Comparable[size];
    }

    @Override
    public void add(T newEntry) {
        if (isFull()) {
            resize();
        }
        array[numberOfEntries] = newEntry;
        numberOfEntries++;
    }

    @Override
    public boolean add(T newEntry, Integer newPosition) {
        if (isFull()) {
            resize();
        }
        if (newPosition >= 1 && newPosition <= (numberOfEntries + 1)) {
            makeRoom(newPosition);
            array[newPosition - 1] = newEntry;
            numberOfEntries++;
            return true;
        }
        return false;
    }

    @Override
    public T get(Integer givenPosition) {
        if (!isEmpty()) {
            if (array[givenPosition] != null) {
                return array[givenPosition];
            }
        }
        return null;
    }

    @Override
    public T remove(Integer givenPosition) {
        T result = null;

        if (array[givenPosition - 1] != null) {
            result = array[givenPosition - 1];
            removeGap(givenPosition);

            numberOfEntries--;
        }
        return result;
    }

    @Override
    public boolean replace(T newEntry, Integer givenPosition) {
        if (!isEmpty()) {
            if (array[givenPosition - 1] != null) {
                array[givenPosition - 1] = newEntry;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public boolean isFull() {
        return numberOfEntries == array.length;
    }

    @Override
    public void clear() {
        for (int i = 0; i < numberOfEntries; i++) {
            array[i] = null;
        }

        numberOfEntries = 0;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public void append(ListInterface newList) {
        for (int i = 0; i < newList.getNumberOfEntries(); i++) {
            if (isFull()) {
                resize();
            }
            T item = (T) newList.get(i);
            if (!contains(item)) {
                array[numberOfEntries] = item;
                numberOfEntries++;
            }
        }
    }

    @Override
    public boolean contains(T entry) {
        if (!isEmpty()) {
            for (int i = 0; i < numberOfEntries; i++) {
                if (array[i].equals(entry)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Iterator iterator() {
        return new iterator();
    }

    private class iterator<T> implements Iterator<T> {

        private int current;

        public iterator() {
            current = 0;
        }

        @Override
        public boolean hasNext() {
            return current < numberOfEntries && array[current] != null;
        }

        @Override
        public T next() {
            return (T) array[current++];
        }

    }

    @Override
    public String toString() {
        String outputStr = "";
        for (int i = 0; i < numberOfEntries; i++) {
            outputStr += array[i];
        }

        return outputStr;
    }

    private void resize() {
        T[] tempArray = array;

        array = (T[]) new Comparable[array.length * 2];

        for (int i = 0; i < numberOfEntries; i++) {
            array[i] = tempArray[i];
        }
    }

    private void makeRoom(int newPosition) {
        int newIndex = newPosition - 1;
        int lastIndex = numberOfEntries - 1;

        for (int i = lastIndex; i >= newIndex; i--) {
            array[i + 1] = array[i];
        }
    }

    private void removeGap(int givenPosition) {
        int removedIndex = givenPosition - 1;
        int lastIndex = numberOfEntries - 1;

        for (int i = removedIndex; i < lastIndex; i++) {
            array[i] = array[i + 1];
        }
    }

}
