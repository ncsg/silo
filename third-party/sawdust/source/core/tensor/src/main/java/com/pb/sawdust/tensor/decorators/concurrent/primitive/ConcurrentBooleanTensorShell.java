package com.pb.sawdust.tensor.decorators.concurrent.primitive;

import com.pb.sawdust.tensor.index.Index;
import com.pb.sawdust.tensor.decorators.id.primitive.IdBooleanTensor;
import com.pb.sawdust.tensor.decorators.concurrent.ConcurrentTensorLocks;
import com.pb.sawdust.tensor.decorators.concurrent.ConcurrentTensorShell;
import com.pb.sawdust.util.array.BooleanTypeSafeArray;
import com.pb.sawdust.tensor.decorators.primitive.BooleanTensor;
import com.pb.sawdust.tensor.TensorImplUtil;

import java.util.concurrent.locks.Lock;

/**
 * The {@code ConcurrentBooleanTensorShell} class provides a wrapper for implementations of the {@code BooleanTensor} interface
 * with support for concurrent access. The locking policy is set by the {@code ConcurrentTensorLocks} implementation used
 * in the class.
 *
 * @author crf <br/>
 *         Started: January 30, 2009 10:47:31 PM
 *         Revised: Dec 14, 2009 12:35:34 PM
 */
public class ConcurrentBooleanTensorShell extends ConcurrentTensorShell<Boolean> implements BooleanTensor {
    private final BooleanTensor tensor;

    /**
     * Constructor specifying the tensor to wrap and the concurrency policy used for locking the tensor.
     *
     * @param tensor
     *        The tensor to wrap.
     *
     * @param locks
     *        The {@code ConcurrentTensorLocks} instance holding the concurrency policy used when locking the tensor.
     */
    public ConcurrentBooleanTensorShell(BooleanTensor tensor, ConcurrentTensorLocks locks) {
        super(tensor,locks);
        this.tensor = tensor;
    }

    public boolean getCell(int ... indices) {
        Lock lock = locks.getReadLock(indices);
        lock.lock();
        try {
            return tensor.getCell(indices);
        } finally {
            lock.unlock();
        }
    }

    public void setCell(boolean value, int ... indices) {
        Lock lock = locks.getWriteLock(indices);
        lock.lock();
        try {
            tensor.setCell(value,indices);
        } finally {
            lock.unlock();
        }
    }

    public void setTensorValues(BooleanTypeSafeArray valuesArray) {
        Lock lock = locks.getTensorWriteLock();
        lock.lock();
        try {
            tensor.setTensorValues(valuesArray);
        } finally {
            lock.unlock();
        }
    }

    public BooleanTypeSafeArray getTensorValues(Class<Boolean> type) {
        Lock lock = locks.getTensorReadLock();
        lock.lock();
        try {
            return tensor.getTensorValues(type);
        } finally {
            lock.unlock();
        }
    }

    public BooleanTypeSafeArray getTensorValues() {
        Lock lock = locks.getTensorReadLock();
        lock.lock();
        try {
            return tensor.getTensorValues();
        } finally {
            lock.unlock();
        }
    }

    public <I> IdBooleanTensor<I> getReferenceTensor(Index<I> index) {
        return (IdBooleanTensor<I>) super.getReferenceTensor(index);
    }

    protected BooleanTensor getComposedTensor(Index<?> index) {
        return TensorImplUtil.getComposedTensor(this,index); 
    }
}