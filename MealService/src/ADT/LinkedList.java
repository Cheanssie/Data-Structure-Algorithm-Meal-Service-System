package ADT;

import java.io.Serializable;
import java.util.Iterator;

/*
 * Title : Link ADT Implementer
 * Method : Linked List
 */
public class LinkedList<T extends Comparable<T>> implements ListInterface<T>, Serializable {

    private Node firstNode; //A node that points to the first node in the linked-list
    private int numberOfEntries; //Holds total number of entries exist in the linked-list
    private Node lastNode;

    public LinkedList() {
        this.clear();
    }

    @Override
    public void add(T newEntry) {
        Node newNode = new Node(newEntry);

        //Step1 : If list is empty, point the firstNode to newEntry and lastNode to newEntry. Otherwise, lastNode.next point to newEntry then point lastNode to newEntry
        if (this.isEmpty()) {
            firstNode = newNode;
        } else {
            lastNode.next = newNode;
        }
        lastNode = newNode; //Eventually, lastNode will still point newNode regardless of the situation
        numberOfEntries++;
    }

    @Override
    public boolean add(T newEntry, Integer newPosition) {
        Node newNode = new Node(newEntry);

        if (newPosition >= 1 && newPosition <= (numberOfEntries + 1)) { //Check if the position is a valid one, within the range of numberOfEntries or to the end of list
            if (newPosition == 1 && this.isEmpty()) { //If add to the first position, link new entry to current firstNode, point firstNode to new entry
                newNode.next = firstNode;
                firstNode = newNode;
                lastNode = newNode;
            } else if (newPosition == 1) {
                newNode.next = firstNode;
                firstNode = newNode;
            } else if (newPosition == numberOfEntries + 1) {
                lastNode.next = newNode;
                lastNode = newNode;
            } else { //Loop to the position right before the newPosition, then add
                Node currentNode = firstNode;
                for (int currentPosition = 1; currentPosition < newPosition - 1; currentPosition++) {
                    currentNode = currentNode.next;
                }
                newNode.next = currentNode.next;
                currentNode.next = newNode;
            }
            numberOfEntries++;
            return true;
        }
        return false;
    }

    @Override
    public T get(Integer givenPosition) {
        T result = null;
        if (givenPosition >= 1 && givenPosition <= numberOfEntries) {
            Node currentNode = firstNode;
            for (int currentPosition = 1; currentPosition < givenPosition; currentPosition++) {
                currentNode = currentNode.next;
            }
            result = currentNode.data;
        }
        return result;
    }

    @Override
    public T remove(Integer givenPosition) {
        T entryRemoved = null;
        if (givenPosition >= 1 && givenPosition <= numberOfEntries) {
            if (givenPosition == 1) {
                entryRemoved = firstNode.data;
                firstNode = firstNode.next;
            } else {
                Node currentNode = firstNode;
                for (int currentPosition = 1; currentPosition < givenPosition - 1; currentPosition++) {
                    currentNode = currentNode.next;
                }
                entryRemoved = currentNode.next.data;
                if (givenPosition == numberOfEntries) {
                    lastNode = currentNode;
                }
                currentNode.next = currentNode.next.next;
            }

            numberOfEntries--;
        }
        return entryRemoved;
    }

    @Override
    public boolean replace(T newEntry, Integer givenPosition) {

        if (givenPosition >= 1 && givenPosition <= numberOfEntries) {
            Node currentNode = firstNode;
            for (int currentPosition = 1; currentPosition < givenPosition; currentPosition++) {
                currentNode = currentNode.next;
            }
            currentNode.data = newEntry;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public boolean isFull() {
        //Linked-List will never be full
        return false;
    }

    @Override
    public void clear() {
        firstNode = null;
        numberOfEntries = 0;
        lastNode = null;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public void append(ListInterface<T> newList) {
        Node newListCurrentNode = ((LinkedList<T>) newList).firstNode;

        while (newListCurrentNode != null) {
            this.add(newListCurrentNode.data);
            newListCurrentNode = newListCurrentNode.next;
        }
    }

    @Override
    public boolean contains(T entry) {
        Node currentNode = firstNode;
        while (currentNode != null) {
            if (currentNode.data.equals(entry)) {
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    @Override
    public String toString() {
        String strResult = "";
        Node currentNode = firstNode;
        while (currentNode != null) {
            strResult += currentNode.data;
            currentNode = currentNode.next;
        }

        strResult += "\n";
        return strResult;
    }

    @Override
    public Iterator iterator() {
        return new LinkedListIterator(firstNode);
    }

    private class LinkedListIterator implements Iterator<T> {

        private Node currentNode;

        private LinkedListIterator(Node firstNode) {
            this.currentNode = firstNode;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            T result = null;
            if (currentNode != null) {
                result = currentNode.data;
                currentNode = currentNode.next;
            }

            return result;
        }
    }

    private class Node implements Serializable {

        private T data; //Data part of a node that hold actual data
        private Node next; //Link part of a node that hold reference if next node

        private Node(T newEntry) { //Constructor use when adding first entry into empty list
            data = newEntry;
            next = null;
        }

        private Node(T newEntry, Node next) { //Constructor use when adding entry into non-empty list
            data = newEntry;
            this.next = next;
        }
    }
}
