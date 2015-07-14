package com.pb.sawdust.tensor.slice;

import com.pb.sawdust.util.test.TestBase;
import com.pb.sawdust.util.array.ArrayUtil;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.LinkedList;

/**
 * @author crf <br/>
 *         Started: Feb 27, 2009 12:35:15 PM
 */
public abstract class SliceTest extends TestBase {
    protected int[] indices;
    protected Slice slice;

    abstract protected int[] getIndices();
    abstract protected Slice getSlice(int[] indices);

    @Before
    public void beforeTest() {
        indices = getIndices();
        slice = getSlice(indices);
    }

    @Test
    public void testGetSize() {
        assertEquals(indices.length,slice.getSize());
    }

    @Test
    public void testGetValueAt() {
        int index = random.nextInt(indices.length);
        assertEquals(indices[index],slice.getValueAt(index));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetValueAtTooLow() {
        slice.getValueAt(-1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetValueAtTooHigh() {
        slice.getValueAt(indices.length);
    }

    @Test
    public void testGetSliceIndices() {
        assertArrayEquals(indices,slice.getSliceIndices());
    }

    @Test
    public void testGetMaxIndex() {
        int max = 0;
        for (int i : indices)
            max = i > max ? i : max;
        assertEquals(max,slice.getMaxIndex());
    }

    @Test
    public void testIterator() {
        List<Integer> elements = new LinkedList<Integer>();
        for (int i : slice)
            elements.add(i);
        assertArrayEquals(indices, ArrayUtil.toPrimitive(elements.toArray(new Integer[elements.size()])));
    }

}