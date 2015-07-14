package com.pb.sawdust.util.collections;

import java.util.Queue;
import java.util.Set;
import java.util.Collection;

/**
 * A {@code SetQueue} is a queue which can contain no repeated values. Though it implements both the {@code Queue} and
 * {@code Set} interface, it is intended to be used as a queue which is constrained to be a set, rather than a set which
 * is a queue.
 * <p>
 * Because element ordering is defined, and because elements cannot be repeated, some required methods create possibly
 * problematic situations. For example, if an object, {@code A}, exists at the beginning of a given {@code SetQueue}, and
 * then {@code A} is added to the setqueue again using {@code add(E)}, then one of two possible actions seems viable:
 * <ol>
 *     <li>Add {@code A} to the end of the queue, and remove the reference at the beginning (because only one reference may
 *         exist in the setlist).</li>
 *     <li>Don't add {@code A} to the queue, and retain the reference at the beginning of the queue.</li>
 * </ol>
 * When faced with such a dilemma, a setqueue will defer to not changing its current state; <i>i.e.</i> the second option.
 * If the first option is preferred (or something analogous to it, in the case of other queue methods), new {@code force*}
 * methods have been specified, which force the method to complete its intended operation.
 *
 * @param <E>
 *        The type of the elements held by this collection.
 *
 * @author crf <br/>
 *         Started: Dec 30, 2008 8:33:09 AM
 */
public interface SetQueue<E> extends Set<E>, Queue<E> {

    /**
     * {@inheritDoc}
     *
     * If the element already exists in the setqueue, then {@code false} will be returned. To force the element to be added
     * (<i>i.e.</i> change the add order for this setqueue), use {@code forceAdd(E)}).
     */
    boolean add(E element);

    /**
     * Inserts the specified element into this setqueue if it is possible to do so immediately without violating capacity
     * restrictions, returning true upon success and throwing an {@code IllegalStateException} if no space is currently
     * available. If the element already exists in the queue, then its previous position in the setqueue will be removed.
     *
     * @param element
     *        The element to be added to the setqueue.
     *
     * @return {@code true} if this setqueue has changed as a result of this operation, {@code false} otherwise.
     *
     * @throws IllegalStateException if the element cannot be added at this time due to capacity restrictions.
     */
    boolean forceAdd(E element);

    /**
     * {@inheritDoc}
     *
     * If any of the elements already exists in the setqueue, then their initial positions will be retained. To force the
     * elements to be added (<i>i.e.</i> change the add order for this setqueue), use {@code forceAddAll(Collection)}).
     * If the collection contains an object multiple times, the first instance of the element in the collection will be
     * used for positioning.
     *
     * @throws IllegalArgumentException if any of the elements in {@code c} already exist in this setlist.
     */
    boolean addAll(Collection<? extends E> c);

    /**
     * Insert all of the elements in a collection to the end of the setqueue.  The if the ordering of the collection is
     * not well defined, then neither will the order in which the elements will be added to the queue. If the element
     * already exists in the queue, then its previous position in the setqueue will be removed. If the collection contains
     * an object multiple times, the last instance of the element in the collection will be used for queue ordering.
     *
     * @param c
     *        The collection containing the elements to add.
     *
     * @return {@code true} if the setlist changed as a result of this operation, {@code false} otherwise.
     */
    boolean forceAddAll(Collection<? extends E> c);

    /**
     * {@inheritDoc}
     *
     * If the element already exists in the setqueue, then {@code false} will be returned. To force the element to be added
     * (<i>i.e.</i> change the add order for this setqueue), use {@code forceAdd(E)}).
     */
    boolean offer(E element);

    /**
     * Inserts the specified element into this queue if it is possible to do so immediately without violating capacity
     * restrictions. When using a capacity-restricted queue, this method is generally preferable to {@code forceAdd(E)},
     * which can fail to insert an element only by throwing an exception.
     *
     * @param element
     *        The element to be added to the setqueue.
     *
     * @return {@code true} if this setqueue has changed as a result of this operation, {@code false} otherwise.
     */
    boolean forceOffer(E element);
}