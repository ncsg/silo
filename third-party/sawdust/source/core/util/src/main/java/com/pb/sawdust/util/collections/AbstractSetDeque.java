package com.pb.sawdust.util.collections;

import java.util.*;

/**
 * The {@code AbstractSetDeque} class provides a basic implementation of the {@code SetDeque} interface to lower the
 * burden for programmers. Internally, it maintains a {@code HashSet} to ensure the set properties, and an
 * implementation-specific {@code Deque} to ensure the deque properties.
 *
 * @param <E>
 *        The type of the elements held by this collection.
 *
 * @author crf <br/>
 *         Started: Dec 30, 2008 11:21:49 AM
 */
public abstract class AbstractSetDeque<E> extends AbstractSetQueue<E> implements SetDeque<E> {
    private Deque<E> elementDeque = null;

    /**
     * Get the list that deque be used to hold the elements in this SetQueue and ensure deque properties. This deque should
     * be empty and a reference to it should be unavailable to it outside of this method's scope. As this class
     * generally calls this backing list for all of the methods specified in the {@code Deque} interface, the
     * specific advantages and disadvantages of the deque returned by this method are inherited by this instance.
     * <p>
     * This method is used in the constructor, and should not be used elsewhere. <b>This method should return
     * a new empty instance of the backing deque type, not a reference to the deque used in this instance.</b> That is,
     * if this method is called repeatedly, a new object should be return, not a reference to one which has already been
     * created. This requirement is to ensure data encapsulation in the {@code SetDeque} implementation.
     *
     * @param initialSize
     *        The size to initialize the deque with. If a value less than 0 is entered, then a deque with the default
     *        size should be returned.
     *
     * @return an empty deque.
     */
    abstract protected Deque<E> getBackingDeque(int initialSize);

    protected Queue<E> getBackingQueue(int initialSize) {
        if (elementDeque == null)
            return elementDeque = getBackingDeque(initialSize);
        return getBackingDeque(initialSize);
    }

    /**
     * Constructor for a default setdeque. The size of the backing deque and hashset will be of default size.
     */
    public AbstractSetDeque() {
        super();
    }

    /**
     * Constructor specifying the initial size of the setdeque. The initial size will be used to initialize both the
     * backing hashset and deque.
     *
     * @param initialSize
     *        The initial size for the setdeque.
     */
    public AbstractSetDeque(int initialSize) {
        super(initialSize);
    }

    /**
     * Constructor specifying the initial size and load factor of the setdeque. The initial size will be used to
     * initialize both the backing hashset and deque, and the load factor will be used by the hashset.
     *
     * @param initialSize
     *        The initial size for this setdeque.
     *
     * @param loadFactor
     *        The load factor for this setdeque.
     *
     * @see java.util.HashSet#HashSet(int, float)
     */
    public AbstractSetDeque(int initialSize, int loadFactor) {
        super(initialSize,loadFactor);
    }

    /**
     * Constructor which will create a new setdeque containing all of the elements in a specified collection. The
     * setdeque will be initialized to the size of the collection. The ordering of the elements in the setdeque will
     * be the same as the iteration order of the elements in the collection, whether or not the iteration order is
     * well-defined.
     *
     * @param c
     *        The collection holding the elements which the setdeque will be initialized with.
     */
    public AbstractSetDeque(Collection<? extends E> c) {
        super(c);
    }

    public boolean add(E element) {
        return addLast(element,false);
    }

    public boolean forceAdd(E element) {
        return addLast(element,true);
    }

    private void addFirst(E element, boolean force) {
        if (!contains(element)) {
            elementDeque.addFirst(element);
            addToSet(element);
        } else if (force){
            elementDeque.remove(element);
            elementDeque.addFirst(element);
        }
    }

    public void addFirst(E element) {
        addFirst(element,false);
    }

    public void forceAddFirst(E element) {
        addFirst(element,true);
    }

    private boolean addLast(E element, boolean force) {
        if (!contains(element)) {
            elementDeque.addLast(element);
            return addToSet(element);
        } else if (force){
            elementDeque.remove(element);
            elementDeque.addLast(element);
            return true;
        }
        return false;
    }

    public void addLast(E element) {
        addLast(element,false);
    }

    public void forceAddLast(E element) {
        addLast(element,true);
    }

    public void push(E element) {
        addFirst(element);
    }

    public void forcePush(E element) {
        forceAddFirst(element);
    }

    public boolean offer(E element) {
        return offerLast(element);
    }

    public boolean forceOffer(E element) {
        return forceOfferLast(element);
    }

    private boolean offerFirst(E element, boolean force) {
        if (!contains(element)) {
            return elementDeque.offerFirst(element) && addToSet(element);
        } else if (force){
            elementDeque.remove(element);
            return elementDeque.offerFirst(element);
        }
        return false;
    }

    public boolean offerFirst(E element) {
        return offerFirst(element,false);
    }

    public boolean forceOfferFirst(E element) {
        return offerFirst(element,true);
    }

    private boolean offerLast(E element, boolean force) {
        if (!contains(element)) {
            return elementDeque.offerLast(element) && addToSet(element);
        } else if (force){
            elementDeque.remove(element);
            return elementDeque.offerLast(element);
        }
        return false;
    }

    public boolean offerLast(E element) {
        return offerLast(element,false);
    }

    public boolean forceOfferLast(E element) {
        return offerLast(element,true);
    }

    public E removeFirst() {
        E element = elementDeque.removeFirst();
        removeFromSet(element);
        return element;
    }

    public E removeLast() {
        E element = elementDeque.removeLast();
        removeFromSet(element);
        return element;
    }

    public E pollFirst() {
        E element = elementDeque.pollFirst();
        if (element != null)
            removeFromSet(element);
        return element;
    }

    public E pollLast() {
        E element = elementDeque.pollLast();
        if (element != null)
            removeFromSet(element);
        return element;
    }

    public E getFirst() {
        return elementDeque.getFirst();
    }

    public E getLast() {
        return elementDeque.getLast();
    }

    public E peekFirst() {
        return elementDeque.peekFirst();
    }

    public E peekLast() {
        return elementDeque.peekFirst();
    }

    @SuppressWarnings("unchecked") //if o can be removed from deque, then it must be an E
    public boolean removeFirstOccurrence(Object o) {
        return elementDeque.removeFirstOccurrence(o) && removeFromSet((E) o);
    }

    @SuppressWarnings("unchecked") //if o can be removed from deque, then it must be an E
    public boolean removeLastOccurrence(Object o) {
        return elementDeque.removeLastOccurrence(o) && removeFromSet((E) o);
    }

    public E pop() {
        return removeFirst();
    }

    public Iterator<E> descendingIterator() {
        return elementDeque.descendingIterator();
    }

    public E remove() {
        E element = elementDeque.remove();
        removeFromSet(element);
        return element;
    }

    public E poll() {
        E element = elementDeque.poll();
        if (element != null)
            removeFromSet(element);
        return element;
    }

    public E element() {
        return elementDeque.element();
    }

    public E peek() {
        return elementDeque.peek();
    }
}