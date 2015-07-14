package com.pb.sawdust.tensor.decorators.primitive.size;

import com.pb.sawdust.tensor.decorators.primitive.BooleanTensor;
import com.pb.sawdust.tensor.decorators.id.primitive.IdBooleanTensor;
import com.pb.sawdust.tensor.index.Index;

/**
 * The {@code BooleanD0TensorShell} class is a wrapper which sets a rank 0 (scalar) {@code BooleanTensor} as a
 * {@code D0Tensor} (or, more specifically, a {@code BooleanD0Tensor}).
 *
 * @author crf <br/>
 *         Started: Jun 25, 2009 2:16:12 PM
 */
public class BooleanD0TensorShell extends AbstractBooleanD0Tensor {
    private final BooleanTensor tensor;

    /**
     * Constructor specifying tensor to wrap.
     *
     * @param tensor
     *        The tensor to wrap.
     *
     * @throws IllegalArgumentException if {@code tensor.size() != 0}.
     */
    public BooleanD0TensorShell(BooleanTensor tensor) {
        if (tensor.size() != 0)
            throw new IllegalArgumentException("Wrapped tensor must be of rank 0.");
        this.tensor = tensor;
    }

    public boolean getCell() {
        return tensor.getCell();
    }

    public void setCell(boolean value) {
        tensor.setCell(value);
    }

    public <I> IdBooleanTensor<I> getReferenceTensor(Index<I> index) {
        return tensor.getReferenceTensor(index);
    }
}