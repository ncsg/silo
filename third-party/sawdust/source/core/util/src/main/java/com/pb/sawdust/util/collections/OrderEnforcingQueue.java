package com.pb.sawdust.util.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The {@code OrderEnforcingQueue} provides a {@code BlockingQueue} whose indexed elements will always be returned in
 * consecutive order.  That is, if the queue last returned an {@code IndexedElement<E>} whose index was 5, then the
 * queue will block until it contains an {@code IndexedElement<E>} whose index is 6.  This class can be useful for
 * maintaining some sort ordering on results in a concurrent application.
 * <p>
 * It is assumed that the indexed elements that this queue will hold are ordered consecutively, in increasing order,
 * with no gaps.  Failure to maintain this assumption will lead to exceptions being thrown (elements added whose
 * index is less than the current index), or indefinite blocking (gaps in the index sequence).
 * <p>
 * Because this queue only makes an element available if the previous (sequential) element has been processed, it is
 * possible for there to be a discrepancy between what has been added to the queue and what can be seen.  For example,
 * if elements with indices 3 and 4 have been added, but the queue is still waiting on the element with index 2, then
 * calling {@code size()} will not include elements 3 and 4.  The reason for this is so that {@code size()} is always
 * consistent with what can be (currently) seen from the queue methods.  To examine these non-visible elements, the
 * {@link #blockedElementCount()} or {@link #getBlockedElements()} methods can be called.
 * <p>
 * This queue has no effective capacity restraints; however, no more than {@code Integer.MAX_INT} elements may be visible
 * at one time (even if more sequential elements have been added to the queue).  Thus, all bulk operations ({@code toArray()},
 * {@code size()}, <i>etc.</i>) will only act on the first {@code Integer.MAX_INT} elements in this queue.
 *
 * @param <E>
 *        The type of the elements held by this queue.
 *
 * @author crf <br/>
 *         Started Aug 14, 2010 6:31:11 PM
 */
public class OrderEnforcingQueue<E> implements BlockingQueue<IndexedElement<E>> {
    private final LinkedBlockingQueue<IndexedElement<E>> queue;
    private final AtomicLong currentIndex;
    private final Map<Long,IndexedElement<E>> holdingMap;
    private final ConcurrentMap<Long,Boolean> indicesBeingProcessed;
    private final Lock addLock;

    /**
     * Constructor specifying the starting index for the elements.
     *
     * @param startingIndex
     *        The first element index for this queue.  Elements will not be visible until an element with this value
     *        as its index has been added.
     */
    public OrderEnforcingQueue(long startingIndex) {
        currentIndex = new AtomicLong(startingIndex);
        holdingMap = new ConcurrentHashMap<Long,IndexedElement<E>>();
        indicesBeingProcessed = new ConcurrentHashMap<Long,Boolean>();
        queue = new LinkedBlockingQueue<IndexedElement<E>>();
        addLock = new ReentrantLock();
    }

    /**
     * Constructor using the default (0) starting index.
     */
    public OrderEnforcingQueue() {
        this(0);
    }

    /**
     * Get the next index for this queue.  That is, this method returns the next index that this queue <i>has not
     * processed</i>, not what element lies at its head.  So, if a queue contains elements with indices 0,1,4,6 (and
     * element 0 is at the head of the queue), then this method would return 2.
     *
     * @return the next sequential index that this queue needs to process.
     */
    public long getCurrentIndex() {
        return currentIndex.get();
    }

    /**
     * Get the number of elements that have been added to this queue but are not visible because one or more preceding
     * (index-wise) elements have not been processed.
     *
     * @return the number of elements that are not visible because of successive-ordering rules.
     */
    public int blockedElementCount() {
        return holdingMap.size();
    }

    /**
     * Get the elements that have been added to this queue but are not visible because one or more preceding (index-wise)
     * elements have not been processed.
     *
     * @return the elements that are not visible because of successive-ordering rules.
     */
    public Collection<IndexedElement<E>> getBlockedElements() {
        return holdingMap.values();
    }

    private long addElementInit(IndexedElement<E> element) {
        long index = element.getElementIndex();
        if (indicesBeingProcessed.putIfAbsent(index,true) != null || holdingMap.containsKey(index))
            throw new IllegalArgumentException("Element with index " + index + " already exists in queue.");
        if (index < currentIndex.get())
            throw new IllegalArgumentException("Current index (" + currentIndex + ") exceeds this element's index (" + index + ")");
        holdingMap.put(index,element);
        indicesBeingProcessed.remove(index);
        return index;
    }

    /**
     * {@inheritDoc}
     *
     * This method will never return {@code false}, since if the queue is full the element will be placed with the
     * "invisible" elements.
     *
     * @throws IllegalArgumentException if the element's index already exists in the this queue (visible or not) or if
     *                                  the index is less than this queue's current index.
     */
    public boolean offer(IndexedElement<E> element) {
        addElementInit(element);
        shiftHoldings();
        return true;
    }

    private void shiftHoldings() {
        long index;
        while (holdingMap.containsKey(index = currentIndex.get())) {
            addLock.lock(); //need to lock onto this
            try {
                if (currentIndex.compareAndSet(index,index+1)) {  //make sure index is still valid
                    if (!queue.offer(holdingMap.get(index))) {
                        return; //queue full, get out of here
                    } else {
                        holdingMap.remove(index); //remove element from holdings
                    }
                }
            } finally {
                addLock.unlock();
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the element's index already exists in the this queue (visible or not) or if
     *                                  the index is less than this queue's current index.
     */
    @Override
    public boolean add(IndexedElement<E> element) {
        if (offer(element))
            return true;
        throw new IllegalStateException("Queue full (should not be possible).");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the element's index already exists in the this queue (visible or not) or if
     *                                  the index is less than this queue's current index.
     */
    @Override
    public void put(IndexedElement<E> element) throws InterruptedException {
        offer(element); //queue effectively unlimited in size
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the element's index already exists in the this queue (visible or not) or if
     *                                  the index is less than this queue's current index.
     */
    @Override
    public boolean offer(IndexedElement<E> element, long timeout, TimeUnit unit) throws InterruptedException {
        return offer(element);
    }

    @Override
    public IndexedElement<E> remove() {
        IndexedElement<E> element = queue.remove();
        shiftHoldings();
        return element;
    }

    @Override
    public IndexedElement<E> poll() {
        IndexedElement<E> element = queue.poll();
        shiftHoldings();
        return element;
    }

    @Override
    public IndexedElement<E> element() {
        return queue.element();
    }

    @Override
    public IndexedElement<E> peek() {
        return queue.peek();
    }

    @Override
    public IndexedElement<E> take() throws InterruptedException {
        IndexedElement<E> element = queue.take();
        shiftHoldings();
        return element;
    }

    @Override
    public IndexedElement<E> poll(long timeout, TimeUnit unit) throws InterruptedException {
        IndexedElement<E> element = queue.poll(timeout,unit);
        shiftHoldings();
        return element;
    }

    /**
     * {@inheritDoc}
     *
     * This method always returns {@code Integer.MAX_VALUE}, since an unlimited number of elements can be added to it.
     * However, no more than {@code Integer.MAX_VALUE} elements can be visible at any one time.
     */
    @Override
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    /**
     * This method is not supported, as the only element that could be removed would be the first one (to preserve
     * sequential access), and this element may be gotten through {@code remove()}.
     *
     * @throws UnsupportedOperationException
     */
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return queue.containsAll(c);
    }

    /**
     * This method is unsupported.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean addAll(Collection<? extends IndexedElement<E>> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is unsupported.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is unsupported.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is unsupported.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * Note that because there may be gaps in the element sequence, certain elements already added to this queue will
     * not be included in this method's returned value.  That is, if a no more elements are added, this method will
     * return the number of times that {@code take()} can be called before blocking. Also, if the number of (visible)
     * elements in this queue are greater than {@code Integer.MAX_SIZE}, then {@code Integer.MAX_SIZE} will be returned.
     */
    @Override
    public int size() {
        return queue.size();
    }

    /**
     * {@inheritDoc}
     *
     * Note that because there may be gaps in the element sequence, this method may return {@code true} even if elements
     * are being held invisibly. That is, this method will return {@code true} if {@code take()} will block, even if
     * {@link #blockedElementCount()} returns a value greater than zero.
     */
    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    /**
     * {@inheritDoc}
     *
     *  Note that because there may be gaps in the element sequence, the iterator returned by this method will only
     * iterate over the elements that are visible (via {@code take()} or {@code peek()}). If {@code queue.size() == Integer.MAX_INT}
     * at the time this method is called, then the returned iterator may only iterate ove {@code Integer.MAX_INT} elements.
     * The returned <tt>Iterator</tt> is a "weakly consistent" iterator that will never throw {@code ConcurrentModificationException},
     * and guarantees to traverse elements as they existed upon
     * construction of the iterator, and may (but is not guaranteed to)
     * reflect any modifications subsequent to construction.
     */
    @Override
    public Iterator<IndexedElement<E>> iterator() {
        return queue.iterator();
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return queue.toArray(a);
    }

    @Override
    public int drainTo(Collection<? super IndexedElement<E>> c) {
        return queue.drainTo(c);
    }

    @Override
    public int drainTo(Collection<? super IndexedElement<E>> c, int maxElements) {
        return queue.drainTo(c,maxElements);
    }

}