package com.pb.sawdust.util.collections.iterators;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.BlockingQueue;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import net.jcip.annotations.NotThreadSafe;
import com.pb.sawdust.util.exceptions.RuntimeWrappingException;

/**
 * The {@code ParallelIterator} class provides a framework to get identical, parallel iterators. This framework is useful
 * if a given iteration cycle is too "expensive" to perform multiple times (<i>e.g.</i> iterating over the lines of a
 * large file), but may need to be used by multiple tasks. Rather than using the iterator once and chaining the element
 * through the various tasks, this framework allows each task to have its own independent iterator. This framework
 * provides a particular advantage to mulit-threaded tasks as each iterator can be used in separate threads safely.
 * Because of this, one "fast" task's iteration cycle will not be held up by a "slow" task, which can aid performance.
 * <p>
 * In order to maintain this potential efficiency, a certain "window" of iteration elements must be maintained: all
 * elements between the "slowest" iterators current element and the "fastest" iterator's are held in memory. This class
 * will allow this window to become as large as necessary, which may be undesirable if the iteration cycle is large
 * (and memory intensive); to get around this problem the queue used to hold the iteration elements may be changed through
 * the {@code getIteratorQueue()} method.
 * <p>
 * When constructing a {@code ParallelIterator} instance, a maximum number of parallel iterators is specified; the
 * {@code getIterator()} method may only be called this many times (anything beyond this will throw an exception). Note
 * that if all of the iterators are not in use when the iteration starts, the unused iterators are still valid, and the
 * element "window" described above will contain the entire iteration history (as these unused iterator's current
 * position is the start of the iteration). Thus, an accurate specification of the number of iterators needed is
 * especially important; this is particularly true if the default iterator queue is changed (<i>i.e.</i>
 * {@code getIteratorQueue()} is overidden) to a blocking queue - deadlocks may result if care is not taken (the
 * deadlock issue remains even if all iterators are in use, but it is particularly problematic if one or more iterators
 * are still unused).
 * <p>
 * As implied above, this class is threadsafe with respect to getting parallel iterators; however, the parallel iterators
 * themselves <i>are not</i> threadsafe, and should generally not be shared across threads. Thus, ignoring the iterators
 * returned by this class (which best practices would dictate not sharing across threads anyway), this class can be
 * considered threadsafe, so long as the "master" iterator is threadsafe and/or is not used outside of this class (which,
 * again, best practices should dictate anyway).
 *
 * @param <E>
 *        The type of elements returned by this class' iterators at each iteration.
 *
 * @author crf <br/>
 *         Started: Jul 9, 2008 7:51:39 AM
 */
@ThreadSafe //see caveats above, though
public class ParallelIterator<E> { 
    private final Lock masterLock = new ReentrantLock();
    private final Iterator<E> iterator;
    private final List<QueingIterator> iterators;
    private AtomicInteger freeIterators;
    private boolean blockingQueue;

    /**
     * Constructor specifying the "master" iterator and the maximum number of parallel iterators.
     *
     * @param iterator
     *        The "master" iterator; all parallel iterators will mimic this iterator's cycle.
     *
     * @param iteratorCount
     *        The maximum number of iterators available through this class.
     */
    public ParallelIterator(Iterator<E> iterator, int iteratorCount) {
        this.iterator = iterator;
        iterators = new ArrayList<QueingIterator>(iteratorCount);
        int count = 0;
        while (count++ < iteratorCount) {
            //check to see if blocking queue
            Queue<E> queue = getIteratorQueue();
            if (count == 1)
                blockingQueue = queue instanceof BlockingQueue;
            iterators.add(new QueingIterator(getIteratorQueue()));
        }
        freeIterators = new AtomicInteger(iteratorCount);
    }

    /**
     * Get a parallel iterator. The iterator returned by this method is unique; <i>i.e.</i> any separate calls to this
     * method will return a distinct iterator. Thus, the {@code iteratorCount} specified at construction provides the
     * upper limit for how many times this method may be called.
     *
     * @return a parallel iterator.
     *
     * @throws IllegalStateException if the iterators have already been used; that is, if this method has already been
     *                               called <tt>maxIterators</tt> times, where <tt>maxIterators = getFreeIteratorCount()</tt>
     *                               before this method has ever been called (<i>i.e.</i> at instantiation).
     */
    public Iterator<E> getIterator() {
        while(true) {
            int freeIteratorsSnap = freeIterators.get();
            int freeIteratorsDecrement = freeIteratorsSnap-1;
            if (freeIteratorsSnap <= 0)
                throw new IllegalStateException("No more iterators available.");
            if (freeIterators.compareAndSet(freeIteratorsSnap,freeIteratorsDecrement))
                return iterators.get(freeIteratorsDecrement);
        }
    }

    /**
     * Get the number of free parallel iterators currently available.
     *
     * @return the number of parallel iterators still available through a {@code getIterator()} call.
     */
    public int getFreeIteratorCount() {
        return freeIterators.get();
    }

    /**
     * Specify the queue to use to hold the iteration history ("window") for each iterator. The default is to allow
     * an unlimited number of elements to fill the queue. This method may be overridden to allow for queue limits (for
     * example, to save memory).
     *
     * @return the iteration queue to use for each parallel iterator.
     */
    protected Queue<E> getIteratorQueue() {
        return new LinkedList<E>();
    }

    @GuardedBy("masterLock")
    private boolean getNext() {
        masterLock.lock();
        try {
            if (!iterator.hasNext())
                return false;
            E next = iterator.next();
            for (QueingIterator iterator : iterators)
                if (blockingQueue)
                    try {
                        ((BlockingQueue<E>) iterator.iteratorBuffer).put(next);
                    } catch (InterruptedException e) {
                        throw new RuntimeWrappingException(e);
                    }
                else
                    iterator.iteratorBuffer.add(next);
            return true;
        } finally {
            masterLock.unlock();
        }
    }

    @NotThreadSafe
    private class QueingIterator implements Iterator<E> {
        private final Queue<E> iteratorBuffer;

        private QueingIterator(Queue<E> iteratorBuffer) {
            this.iteratorBuffer = iteratorBuffer;
        }

        public boolean hasNext() {
            return iteratorBuffer.size() > 0 || getNext();
        }

        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return iteratorBuffer.remove();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}