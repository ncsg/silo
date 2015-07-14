package com.pb.sawdust.tensor.decorators.primitive.size;

import com.pb.sawdust.tensor.decorators.primitive.ByteTensor;
import com.pb.sawdust.tensor.decorators.id.primitive.IdByteTensor;
import com.pb.sawdust.tensor.index.Index;

/**
 * The {@code ByteD0TensorShell} class is a wrapper which sets a rank 0 (scalar) {@code ByteTensor} as a
 * {@code D0Tensor} (or, more specifically, a {@code ByteD0Tensor}).
 *
 * @author crf <br/>
 *         Started: Jun 25, 2009 2:16:12 PM
 */
public class ByteD0TensorShell extends AbstractByteD0Tensor {
    private final ByteTensor tensor;

    /**
     * Constructor specifying tensor to wrap.
     *
     * @param tensor
     *        The tensor to wrap.
     *
     * @throws IllegalArgumentException if {@code tensor.size() != 0}.
     */
    public ByteD0TensorShell(ByteTensor tensor) {
        if (tensor.size() != 0)
            throw new IllegalArgumentException("Wrapped tensor must be of rank 0.");
        this.tensor = tensor;
    }

    public byte getCell() {
        return tensor.getCell();
    }

    public void setCell(byte value) {
        tensor.setCell(value);
    }

    public <I> IdByteTensor<I> getReferenceTensor(Index<I> index) {
        return tensor.getReferenceTensor(index);
    }
}