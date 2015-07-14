package com.pb.sawdust.tensor.alias.matrix.id;

import com.pb.sawdust.tensor.alias.matrix.primitive.CharMatrix;
import com.pb.sawdust.tensor.decorators.id.primitive.size.IdCharD2Tensor;
import com.pb.sawdust.tensor.Tensor;

import java.util.Iterator;

/**
 * The {@code IdCharMatrix} interface provides an alternate name for 2-dimensional id tensors holding {@code char}s.
 *
 * @author crf <br/>
 *         Started: Jun 16, 2009 9:22:03 AM
 */
public interface IdCharMatrix<I> extends IdCharD2Tensor<I>,CharMatrix {
    
    /**
     * {@inheritDoc}
     * 
     * The tensors this iterator loops over are guaranteed to be {@code IdCharVector}s.
     * 
     */
    Iterator<Tensor<Character>> iterator();
}