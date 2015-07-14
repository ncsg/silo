package com.pb.sawdust.tensor.decorators.id.primitive.size;

import com.pb.sawdust.tensor.decorators.primitive.size.LongD8TensorShell;
import com.pb.sawdust.tensor.decorators.primitive.size.LongD8Tensor;
import com.pb.sawdust.tensor.decorators.primitive.size.LongD7Tensor;
import com.pb.sawdust.tensor.index.Index;
import com.pb.sawdust.tensor.index.BaseIndex;
import com.pb.sawdust.tensor.TensorImplUtil;
import com.pb.sawdust.tensor.Tensor;


import java.util.List;
import java.util.Iterator;

/**
 * The {@code IdLongD8TensorShell} class is a wrapper which sets a {@code LongD8TensorShell} as an {@code IdTensor<I>} (or,
 * more specifically, an {@code IdLongD8Tensor}).
 *
 * @author crf <br/>
 *         Started: Jan 16, 2009 9:47:43 AM
 *         Revised: Dec 14, 2009 12:35:32 PM
 */
public class IdLongD8TensorShell<I> extends LongD8TensorShell implements IdLongD8Tensor<I> {
    private final LongD8Tensor tensor;
    private final Index<I> index;

    IdLongD8TensorShell(LongD8Tensor tensor, Index<I> index) {
        super(tensor);
        this.tensor = tensor;
        this.index = index;
    }

    /**
     * Constructor which will wrap a tensor, using its current index for the ids. Note that this constructor is
     * inherently unsafe, as there is no way to check that the type of the tensor's index matches the type declared
     * when this constructor is called. Therefore, it should be used with care or in situations where the index type
     * can be verified.
     * 
     * @param tensor
     *        The tensor to wrap.
     */
    @SuppressWarnings("unchecked") //warning is duly noted and clearly stated in the documentation
    public IdLongD8TensorShell(LongD8Tensor tensor) {
        this(tensor,(Index<I>) tensor.getIndex());
    }

    /**
     * Constructor specifying the tensor to wrap, as well as the ids to use to reference the indices.
     *
     * @param tensor
     *        The tensor to wrap.
     *
     * @param ids
     *        The ids to reference the indices with.
     *
     * @throws IllegalArgumentException if {@code ids.size() != 8}, if the length of each array in {@code ids} 
     *                                  does not match its respective dimension's size in {@code tensor}, or if any array 
     *                                  in {@code ids} contains repeated elements.
     */
    @SafeVarargs
    @SuppressWarnings({"unchecked","varargs"})
    public IdLongD8TensorShell(LongD8Tensor tensor, I[] ... ids) {
        this(tensor,new BaseIndex<I>(tensor,ids));
    }

    /**
     * Constructor specifying the tensor to wrap, as well as the ids to use to reference the indices.
     *
     * @param tensor
     *        The tensor to wrap.
     *
     * @param ids
     *        The ids to reference the indices with.
     *
     * @throws IllegalArgumentException if {@code ids.size() != 8}, if the length of each array in {@code ids} 
     *                                  does not match its respective dimension's size in {@code tensor}, or if any array 
     *                                  in {@code ids} contains repeated elements.
     */
    public IdLongD8TensorShell(LongD8Tensor tensor, List<List<I>> ids) {
        this(tensor,new BaseIndex<I>(tensor,ids));
    }

    public Index<I> getIndex() {
        return index;
    }

    public long getCellById(I d0id, I d1id, I d2id, I d3id, I d4id, I d5id, I d6id, I d7id) {
        return tensor.getCell(index.getIndex(0,d0id),index.getIndex(1,d1id),index.getIndex(2,d2id),index.getIndex(3,d3id),index.getIndex(4,d4id),index.getIndex(5,d5id),index.getIndex(6,d6id),index.getIndex(7,d7id));
    }

    public void setCellById(long value, I d0id, I d1id, I d2id, I d3id, I d4id, I d5id, I d6id, I d7id) {
        tensor.setCell(value,index.getIndex(0,d0id),index.getIndex(1,d1id),index.getIndex(2,d2id),index.getIndex(3,d3id),index.getIndex(4,d4id),index.getIndex(5,d5id),index.getIndex(6,d6id),index.getIndex(7,d7id));
    }

    @SuppressWarnings({"unchecked", "varargs"})
    public Long getValueById(I ... ids) {
        TensorImplUtil.checkIndicesLength(this,ids);
        return getCellById(ids[0],ids[1],ids[2],ids[3],ids[4],ids[5],ids[6],ids[7]);
    }

    @SuppressWarnings({"unchecked", "varargs"})
    public long getCellById(I ... ids) {
        TensorImplUtil.checkIndicesLength(this,ids);
        return getCellById(ids[0],ids[1],ids[2],ids[3],ids[4],ids[5],ids[6],ids[7]);
    }

    @SuppressWarnings({"unchecked", "varargs"})
    public void setCellById(long value, I ... ids) {
        TensorImplUtil.checkIndicesLength(this,ids);
        setCellById(value,ids[0],ids[1],ids[2],ids[3],ids[4],ids[5],ids[6],ids[7]);
    }

    @SuppressWarnings({"unchecked", "varargs"})
    public void setValueById(Long value, I ... ids) {
        setCellById(value,ids);
    }

    public Long getValueById(I d0id, I d1id, I d2id, I d3id, I d4id, I d5id, I d6id, I d7id) {
        return getCellById(d0id,d1id,d2id,d3id,d4id,d5id,d6id,d7id);
    }

    public void setValueById(Long value, I d0id, I d1id, I d2id, I d3id, I d4id, I d5id, I d6id, I d7id) {
        setCellById(value,d0id,d1id,d2id,d3id,d4id,d5id,d6id,d7id);
    }

    public Iterator<Tensor<Long>> iterator() {
        return new Iterator<Tensor<Long>>() {
            private final Iterator<Tensor<Long>> iterator = IdLongD8TensorShell.super.iterator();
            
            public boolean hasNext() {
                return iterator.hasNext();
            }

            public Tensor<Long> next() {
                return new IdLongD7TensorShell<I>((LongD7Tensor) iterator.next());
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}