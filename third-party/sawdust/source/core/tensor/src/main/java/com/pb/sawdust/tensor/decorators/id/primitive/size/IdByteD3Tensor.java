package com.pb.sawdust.tensor.decorators.id.primitive.size;


import com.pb.sawdust.tensor.decorators.id.primitive.IdByteTensor;
import com.pb.sawdust.tensor.decorators.primitive.size.ByteD3Tensor;
import com.pb.sawdust.tensor.decorators.id.size.IdD3Tensor;
import com.pb.sawdust.tensor.Tensor;

import java.util.Iterator;

/**
 * The {@code IdByteD3Tensor} class combines the {@code Byte3Tensor} and {@code IdTensor} interfaces. It adds a
 * number of methods extending {@code ByteD3Tensor} methods so that dimensional indices can be referenced by ids.
 *
 * @param <I>
 *        The type of id used to reference dimensional indices.
 *
 * @author crf <br/>
 *         Started: Jan 14, 2009 11:00:16 PM
 *         Revised: Dec 14, 2009 12:35:25 PM
 */
public interface IdByteD3Tensor<I> extends ByteD3Tensor,IdByteTensor<I>,IdD3Tensor<Byte,I> {
    /**
     * Get the cell in this tensor at the specified location. This method should be more efficient than the other {@code getCell}
     * and {@code getValue} methods using ids in the interfaces being extended.
     *
     * @param d0id
     *        The id of dimension 0 of the tensor element.
     *
     * @param d1id
     *        The id of dimension 1 of the tensor element.
     *
     * @param d2id
     *        The id of dimension 2 of the tensor element.
     *
     * @return the tensor value at the specified location.
     *
     * @throws IllegalArgumentException if any id is not valid in its respective dimension.
     */
    byte getCellById(I d0id, I d1id, I d2id);

    /**
     * Set the value of a cell in this tensor at the specified location, specified by id.  This method should be more
     * efficient than the other {@code setCell} and {@code getValue} methods using ids in the interfaces being extended.
     *
     * @param value
     *        The value to set the cell to.
     *
     * @param d0id
     *        The id of dimension 0 of the tensor element.
     *
     * @param d1id
     *        The id of dimension 1 of the tensor element.
     *
     * @param d2id
     *        The id of dimension 2 of the tensor element.
     *
     * @throws IllegalArgumentException if any id is not valid in its respective dimension.
     */
    void setCellById(byte value, I d0id, I d1id, I d2id);

    /**
     * {@inheritDoc}
     *
     * The tensors this iterator loops over are guaranteed to be {@code IdbyteD2Tensor<I>} tensors.
     * 
     */
    Iterator<Tensor<Byte>> iterator();
}