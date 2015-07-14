package com.pb.sawdust.tensor;

import com.pb.sawdust.tensor.factory.ConcurrentTensorFactory;
import com.pb.sawdust.tensor.factory.ConcurrentTensorFactoryTest;
import com.pb.sawdust.tensor.factory.TensorFactory;
import com.pb.sawdust.tensor.factory.TensorFactoryTest;
import com.pb.sawdust.util.test.TestBase;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@code LinearTensorTest} ...
 *
 * @author crf <br/>
 *         Started Dec 24, 2010 11:38:57 AM
 */
public class LinearTensorTest extends TensorFactoryTest {

    public static void main(String ... args) {
        TestBase.main();
    }

    protected Collection<Class<? extends TestBase>> getAdditionalTestClasses() {
        List<Class<? extends TestBase>> adds = new LinkedList<Class<? extends TestBase>>();
        adds.addAll(super.getAdditionalTestClasses());
        adds.add(ConcurrentLinearTensorTest.class);
        return adds;
    }

    protected TensorFactory getFactory() {
        return LinearTensor.getFactory();
    }

    public static class ConcurrentLinearTensorTest extends ConcurrentTensorFactoryTest {

        @Override
        protected ConcurrentTensorFactory getFactory() {
        return LinearTensor.getFactory();
        }
    }
}