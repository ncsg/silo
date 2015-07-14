package com.pb.sawdust.tensor.alias.matrix.primitive;

import com.pb.sawdust.tensor.decorators.primitive.size.BooleanD2TensorTest;
import com.pb.sawdust.tensor.alias.vector.primitive.BooleanVector;
import com.pb.sawdust.tensor.Tensor;
import org.junit.Test;                                                      
import static org.junit.Assert.*;

/**
 * @author crf <br/>
 *         Started: Jul 5, 2009 9:19:38 AM
 */
public abstract class BooleanMatrixTest extends BooleanD2TensorTest {    
    @Test
    public void testIteratorType() {
        for (Tensor<Boolean> t : tensor)
            assertTrue(t instanceof BooleanVector);
    }
}