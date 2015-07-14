package com.pb.sawdust.tensor.alias.scalar;

import com.pb.sawdust.tensor.decorators.size.D0Tensor;

/**
 * The {@code Scalar} interface provides an alternate name for 0-dimensional tensors.
 *
 * @author crf <br/>
 *         Started: Jun 18, 2009 2:33:35 PM
 */
public interface Scalar<T> extends D0Tensor<T> {

    /**
     * {@inheritDoc}
     *
     * This method throws an exception, as a scalar has no dimensionality.
     */
    @Override
    public int size(int dimension);

}