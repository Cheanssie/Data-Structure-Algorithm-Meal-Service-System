package ADT;

import java.io.Serializable;
import java.util.Iterator;

public class DoublyLinkedSet<T extends Comparable<T>> implements SetInterface<T>, Serializable {

    Node firstNode;

    public DoublyLinkedSet() {
        firstNode = null;
    }

    @Override
    public boolean add(T newElement) {
        if (!isEmpty()) {
            Node currentNode = firstNode;
            while (currentNode != null) {
                if (currentNode.data.equals(newElement)) {
                    return false;
                }
                currentNode = currentNode.next;
            }
        }
        Node newNode = new Node(newElement, firstNode);
        if (firstNode != null) {
            firstNode.previous = newNode;
            firstNode = newNode;
        } else {
            firstNode = newNode;
        }
        return true;
    }

    @Override
    public T remove(T anElement) {
        T removeElement = null;
        if (!isEmpty()) {
            if (firstNode.data.equals(anElement)) {
                removeElement = firstNode.data;
                firstNode = firstNode.next;
            } else {
                Node currentNode = firstNode;
                while (currentNode != null) {
                    if (currentNode.data == anElement) {
                        removeElement = currentNode.data;
                        currentNode.previous.next = currentNode.next;
                        break;
                    }
                    currentNode = currentNode.next;
                }
            }
        }

        return removeElement;
    }

    @Override
    public boolean checkSubset(SetInterface<T> anotherSet) {
        Node currentNode = firstNode;
        int count = 0;
        boolean isSubset = false;

        if (!anotherSet.isEmpty()) {
            while (currentNode != null) {
                Node anotherSetNode = (Node) ((DoublyLinkedSet<T>) anotherSet).firstNode;
                while (anotherSetNode != null) {
                    if (((T) currentNode.data).equals((T) anotherSetNode.data)) {
                        count++;
                    }
                    anotherSetNode = anotherSetNode.next;
                }
                currentNode = currentNode.next;
            }

            if (count == ((SetInterface<T>) anotherSet).getSize()) {
                isSubset = true;
            }
        } else {
            isSubset = true;
        }
        return isSubset;
    }

    @Override
    public void union(SetInterface<T> anotherSet) {
        Node anotherSetNode = (Node) ((DoublyLinkedSet<T>) anotherSet).firstNode;
        while (anotherSetNode != null) {
            this.add(anotherSetNode.data);
            anotherSetNode = anotherSetNode.next;
        }
    }

    @Override
    public SetInterface<T> intersection(SetInterface<T> anotherSet) {
        SetInterface<T> newSet = new DoublyLinkedSet<>();
        Node currentNode = firstNode;

        while (currentNode != null) {
            Node anotherSetNode = (Node) ((DoublyLinkedSet<T>) anotherSet).firstNode;
            while (anotherSetNode != null) {
                if (currentNode.data.equals(anotherSetNode.data)) {
                    newSet.add(currentNode.data);
                }
                anotherSetNode = anotherSetNode.next;
            }
            currentNode = currentNode.next;
        }
        return newSet;
    }

    @Override
    public boolean contains(T anElement) {
        if (!isEmpty()) {
            if (firstNode.data.equals(anElement)) {
                return true;
            } else {
                Node currentNode = firstNode;
                while (currentNode != null) {
                    if (currentNode.data.equals(anElement)) {
                        return true;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        return false;
    }

    @Override
    public void clear() {
        firstNode = null;
    }

    @Override
    public boolean isEmpty() {
        return firstNode == null;
    }

    @Override
    public int getSize() {
        int size = 0;
        Node currentNode = firstNode;
        while (currentNode != null) {
            size++;
            currentNode = currentNode.next;
        }
        return size;
    }

    @Override
    public String toString() {
        String outStr = "";
        Node currentNode = firstNode;
        while (currentNode != null) {
            outStr += currentNode.data + "";
            currentNode = currentNode.next;
        }
        outStr += "\n";
        return outStr;
    }

    @Override
    public Iterator iterator() {
        return new LinkedSetIterator(firstNode);
    }

    private class LinkedSetIterator implements Iterator<T> {

        private Node currentNode;

        private LinkedSetIterator(Node firstNode) {
            currentNode = firstNode;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            T result = currentNode.data;
            currentNode = currentNode.next;
            return result;
        }
    }

    private class Node implements Serializable {

        private T data;
        private Node next;
        private Node previous;

        public Node(T newElement) {
            data = newElement;
            next = null;
            previous = null;
        }

        public Node(T newElement, Node nextNode) {
            data = newElement;
            next = nextNode;
            previous = null;
        }

    }
}
