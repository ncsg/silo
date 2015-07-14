package com.pb.sawdust.tensor;

/**
 * The {@code WrappedTensor} interface denotes a {@code Tensor} instance which is wrapping another. The wrapping tensor
 * will often defer to the wrapped tensor when methods are called on it, and generally it will add some functionality to
 * the wrapped tensor.
 * <p>
 * This interface should not be used to mark all tensors which wrap other tensors; since it provides access to the
 * wrapped tensor, certain visibility restrictions cannot be maintained. Thus, it should only be used in cases where
 * providing structured access to the underlying tensor is desired.
 *
 * @author crf <br/>
 *         Started: Sep 20, 2009 9:14:15 AM
 */
public interface WrappedTensor<T extends Tensor<?>> {

    /**
     * Get the wrapped tensor.
     *
     * @return the tensor this one wraps.
     */
    T getWrappedTensor();
}