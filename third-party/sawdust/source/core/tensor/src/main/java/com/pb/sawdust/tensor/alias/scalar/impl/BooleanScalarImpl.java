package com.pb.sawdust.tensor.alias.scalar.impl;

import com.pb.sawdust.tensor.decorators.primitive.AbstractBooleanTensor;
import com.pb.sawdust.tensor.decorators.primitive.size.AbstractBooleanD0Tensor;
import com.pb.sawdust.tensor.decorators.id.primitive.IdBooleanTensor;
import com.pb.sawdust.tensor.index.Index;
import com.pb.sawdust.tensor.TensorImplUtil;

/**
 * The {@code BooleanScalarImpl} class provides the default {@code BooleanScalar} implementation.
 *
 * @author crf <br/>
 *         Started: Jun 16, 2009 8:14:56 PM
 */
public class BooleanScalarImpl extends AbstractBooleanD0Tensor {
    private boolean value;

    /**
     * Constructor placing a default {@code boolean} as the scalar value.
     */
    public BooleanScalarImpl() {
    }

    /**
     * Constructor specifying the value of the scalar.
     *
     * @param value
     *        The value to set the scalar to.
     */
    public BooleanScalarImpl(boolean value) {
        this.value = value;
    }

    /**
     * Constructor specifying the value and index of the scalar. This constructor should only be used for calls to
     * {@code getReferenceTensor(Index)}.
     *
     * @param value
     *        The value to set the scalar to.
     *
     * @param index
     *        The index to use with this scalar.
     *
     * @throws IllegalArgumentException if {@code index.size() != 0}.
     */
    protected BooleanScalarImpl(boolean value, Index<?> index) {
        super(index);
        this.value = value;
    }

    @Override
    public boolean getCell() {
        return value;
    }

    @Override
    public void setCell(boolean value) {
        this.value = value;
    }

    @Override
    @SuppressWarnings("unchecked") //idTensorCaster will turn this to an IdBooleanTensor, for sure
    public <I> IdBooleanTensor<I> getReferenceTensor(Index<I> index) {         
        if (!index.isValidFor(this))
            throw new IllegalArgumentException("Index invalid for this tensor.");
        return (IdBooleanTensor<I>) TensorImplUtil.idTensorCaster(new ComposedTensor(index));
    }

    private class ComposedTensor extends AbstractBooleanTensor {

        public ComposedTensor(Index<?> index) {
            super(index);
        }

        @Override
        public boolean getCell(int... indices) {
            return BooleanScalarImpl.this.getCell();
        }

        @Override
        public void setCell(boolean value, int... indices) {
            BooleanScalarImpl.this.setCell(value);
        }
    }
}