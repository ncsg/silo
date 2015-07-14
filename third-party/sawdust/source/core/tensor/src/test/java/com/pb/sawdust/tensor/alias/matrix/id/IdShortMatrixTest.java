package com.pb.sawdust.tensor.alias.matrix.id;

import com.pb.sawdust.tensor.decorators.id.primitive.size.IdShortD2TensorTest;
import com.pb.sawdust.tensor.Tensor;
import com.pb.sawdust.tensor.alias.vector.id.IdShortVector;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * @author crf <br/>
 *         Started: Jul 24, 2009 5:05:20 PM
 */
public abstract class IdShortMatrixTest<I> extends IdShortD2TensorTest<I> {
    @Test
    public void testIteratorType() {
        for (Tensor<Short> t : tensor)
            assertTrue(t instanceof IdShortVector);
    }
}