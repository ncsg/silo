package com.pb.sawdust.tensor.decorators.id.size;

import com.pb.sawdust.tensor.decorators.id.IdTensorTest;
import com.pb.sawdust.tensor.Tensor;
import static com.pb.sawdust.util.Range.range;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author crf <br/>
 *         Started: Jul 23, 2009 8:09:54 AM
 */
public abstract class IdD2TensorTest<T,I> extends IdTensorTest<T,I> {
    protected IdD2Tensor<T,I> dTensor;

    @Before
    @SuppressWarnings("unchecked") //cast is ok
    public void beforeTest() {
        super.beforeTest();
        dTensor = (IdD2Tensor<T,I>) tensor;
    }

    @Test
    public void testGetValueByIdD() {
        int[] index = new int[dimensions.length];
        for (int i : range(index.length))
            index[i] = random.nextInt(dimensions[i]);
        I[] ids = getIdsFromIndices(index);
        assertEquals(data.getValue(index), dTensor.getValueById(ids[0],ids[1]));
    }

    @Test(expected=IllegalArgumentException.class)
    @SuppressWarnings("unchecked") //only an Object[], but ok to be an I[]
    public void testGetValueByIdBadIdD() {
        int[] index = new int[dimensions.length];
        for (int i : range(index.length))
            index[i] = random.nextInt(dimensions[i]);
        I[] id = getIdsFromIndices(index);
        id[random.nextInt(index.length)] = (I) new Object();
        dTensor.getValueById(id[0],id[1]);
    }

    @Test
    public void testSetValueByIdD() {
        int[] index = new int[dimensions.length];
        for (int i : range(index.length))
            index[i] = random.nextInt(dimensions[i]);
        T value = getRandomElement();
        I[] ids = getIdsFromIndices(index);
        dTensor.setValueById(value,ids[0],ids[1]);
        assertEquals(value,idTensor.getValue(index[0],index[1]));
    }

    @Test(expected=IllegalArgumentException.class)
    @SuppressWarnings("unchecked") //only an Object[], but ok to be an I[]
    public void testSetValueByIdBadIdD() {
        int[] index = new int[dimensions.length];
        for (int i : range(index.length))
            index[i] = random.nextInt(dimensions[i]);
        I[] id = getIdsFromIndices(index);
        id[random.nextInt(index.length)] = (I) new Object();
        dTensor.setValueById(getRandomElement(),id[0],id[1]);
    }

    @Test
    public void testIteratorType() {
        for (Tensor<T> t : tensor)
            assertTrue(t instanceof IdD1Tensor);
    }

    @Test
    public void TestSizeSpecific() {
        assertEquals(2,dTensor.size());
    }

    @Test
    public void testGetValueD() {
        int[] index = new int[dimensions.length];
        for (int i : range(index.length))
            index[i] = random.nextInt(dimensions[i]);
        assertEquals(data.getValue(index),dTensor.getValue(index[0],index[1]));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndexTooBigD() {
        int[] index = new int[dimensions.length];
        int randomIndex = random.nextInt(dimensions.length);
        index[randomIndex] = dimensions[randomIndex];
        dTensor.getValue(index[0],index[1]);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndexTooSmallD() {
        int[] index = new int[dimensions.length];
        index[random.nextInt(dimensions.length)] = -1;
        dTensor.getValue(index[0],index[1]);
    }

    @Test
    public void testSetValueD() {
        int[] index = new int[dimensions.length];
        for (int i : range(index.length))
            index[i] = random.nextInt(dimensions[i]);
        T value = getRandomElement();
        dTensor.setValue(value,index[0],index[1]);
        assertEquals(value,dTensor.getValue(index));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testSetValueInvalidIndexTooBigD() {
        int[] index = new int[dimensions.length];
        int randomIndex = random.nextInt(dimensions.length);
        index[randomIndex] = dimensions[randomIndex];
        dTensor.setValue(getRandomElement(),index[0],index[1]);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testSetValueInvalidIndexTooSmallD() {
        int[] index = new int[dimensions.length];
        index[random.nextInt(dimensions.length)] = -1;
        dTensor.setValue(getRandomElement(),index[0],index[1]);
    }

}