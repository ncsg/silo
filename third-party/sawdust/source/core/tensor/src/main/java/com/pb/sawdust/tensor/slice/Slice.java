package com.pb.sawdust.tensor.slice;

import java.util.Iterator;

/**
 * The {@code Slice} interface provides a structure through which tensor indices can be reordered, reduced, or otherwise
 * changed. A slice can be viewed as a simplified index which contains mappings from its indices to some reference
 * indices for one dimension. For example, if a tensor has a dimension of size {@code 10}, a slice can be built which
 * reduces the dimension size to {@code 5} and reorders the indices:
 * <pre><code>
 *     [4,3,4,8,0]
 * </code></pre>
 * (Note that a source index could be repeated). Such a slice could be used in combination with a {@code SliceIndex} and
 * the source tensor to create a reference tensor with reordered elements and a reduced dimension size.
 *
 * @author crf <br/>
 *         Started: Oct 13, 2008 10:01:49 AM
 */
public interface Slice extends Iterable<Integer> {
    /**
     * Get the number of index elements in this slice.
     *
     * @return the size of this slice.
     */
    int getSize();

    /**
     * Get the reference index corresponding to a specified index point in this slice. That is, get the index point
     * in the source tensor/index corresponding to the specified index point in this slice.
     *
     * @param index
     *        The index point in this slice.
     *
     * @return the reference index corresponding to {@code index}.
     *
     * @throws IllegalArgumentException {@code index} is less than zero or greater than one less than the number of
     *                                  index elements in this slice.
     */
    int getValueAt(int index);

    /**
     * Get the set of reference index points corresponding to every index in this slice. That is, get the index points
     * in the source tensor/index corresponding to each index point in this slice. The returned array will have a length
     * equal to {@code size()}.
     *
     * @return all of the reference index points for this slice.
     */
    int[] getSliceIndices();

    /**
     * Get the maximum reference index in this slice. That is, the maximum value in {@code getSliceIndices()}. This
     * method is used to efficiently determine whether a slice can be used with a given index/tensor, so it should
     * cached for efficiency.
     *
     * @return the maximum reference index in this slice.
     */
    int getMaxIndex();

    /**
     * Get an iterator which cycles, in order, through this slice's reference indices. That is, for a given {@code Slice}
     * instance {@code s}, the following for heading:
     * <pre><code>
     *     for (int i : s) {...}
     * </code></pre>
     * is equivalent to:
     * <pre><code>
     *     for (int i : s.getSliceIndices()) {...}
     * </code></pre>
     *
     * @return an iterator which will cycle through this slice's indices.
     */
    Iterator<Integer> iterator();
}