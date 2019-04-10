package com.apps.concurrency.lockfree;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

class LockFreeBoundedQueue<E> implements Queue<E> {

    private Object[] values;
    private AtomicLong tailPointer = new AtomicLong();
    private AtomicLong headPointer = new AtomicLong();

    public LockFreeBoundedQueue(int size) {
        values = new Object[size];
    }

    @Override
    public boolean offer(E e) {
        long curTail = tailPointer.get();
        long diff = curTail - values.length;
        if (headPointer.get() <= diff) {
            return false;
        }
        values[(int) (curTail % values.length)] = e;
        while (tailPointer.compareAndSet(curTail, curTail + 1)) ;
        return true;
    }

    @Override
    public E poll() {
        long curHead = headPointer.get();
        if (curHead >= tailPointer.get())
            return null;
        if (!headPointer.compareAndSet(curHead, curHead + 1)) {
            return null;
        }
        int index = (int) curHead % values.length;
        @SuppressWarnings("unchecked")
        E value = (E) values[index];
        values[index] = null;
        return value;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {

        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {

        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean add(E e) {

        return false;
    }

    @Override
    public E remove() {

        return null;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

}
