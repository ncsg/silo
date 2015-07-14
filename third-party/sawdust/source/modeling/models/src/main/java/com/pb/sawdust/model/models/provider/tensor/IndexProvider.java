package com.pb.sawdust.model.models.provider.tensor;

/**
 * The {@code IndexProvider} interface specifies a framework through which indices pointing to data locations in tensors
 * may be obtained.
 *
 * @author crf
 *         Started 4/6/12 6:08 AM
 */
public interface IndexProvider {
    /**
     * Get the number of dimensions this provider provides.
     *
     * @return the number of dimensions for this provider.
     */
    int getDimensionCount();

    /**
     * Get the index value for a specific index point.
     *
     * @param dimension
     *        The (0-based) dimension of the point.
     *
     * @param location
     *        The (0-based) location of the point.
     *
     * @return the index value for point {@code location} in dimension {@code dimension}.
     *
     * @throws IllegalArgumentException if either {@code dimension} or {@code location} is out of bounds (less than zero
     *                                  or greater than or equal to the size of the dimension or size of this provider).
     */
    int getIndex(int dimension, int location);

    /**
     * Get the index values for a range of locations in a specified dimension.
     *
     * @param dimension
     *        The (0-based) dimension.
     *
     * @param start
     *        The (0-based) starting point (inclusive).
     *
     * @param end
     *        The (0-based) ending point (exclusive).
     *
     * @return the index values for dimension {@code dimension} on locations <code>[start,end)</code>.
     *
     * @throws IllegalArgumentException if {@code dimension} is out of bounds (less than zero or greater than or equal
     *                                  to the number of dimension), if <code>start <= end</code>, or if either {@code start}
     *                                  or {@code end} is out of bounds (less than zero or greater than or equal to the size
     *                                  of this provider).
     */
    int[] getIndices(int dimension, int start, int end);

    /**
     * Get the length of this index. This is the equivalent method to {@code DataProvider#getDataLength()}.
     *
     * @return the size of this index provider.
     */
    int getIndexLength();
}