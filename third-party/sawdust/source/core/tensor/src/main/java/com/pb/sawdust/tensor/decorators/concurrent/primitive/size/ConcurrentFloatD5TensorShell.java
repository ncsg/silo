package com.pb.sawdust.tensor.decorators.concurrent.primitive.size;

import com.pb.sawdust.tensor.TensorImplUtil;
import com.pb.sawdust.tensor.index.Index;
import com.pb.sawdust.tensor.decorators.concurrent.size.ConcurrentD5TensorShell;
import com.pb.sawdust.tensor.decorators.primitive.FloatTensor;
import com.pb.sawdust.tensor.decorators.id.primitive.IdFloatTensor;

import com.pb.sawdust.tensor.decorators.primitive.size.FloatD5Tensor;
import com.pb.sawdust.util.array.FloatTypeSafeArray;

import java.util.concurrent.locks.Lock;

/**
 * The {@code ConcurrentFloatD5TensorShell} class provides a wrapper for implementations of the {@code FloatD5Tensor} interface
 * with support for concurrent access. The locking policy is set by the {@code D5ConcurrentTensorLocks} implementation used
 * in the class.
 *
 * @author crf <br/>
 *         Started: January 30, 2009 10:47:31 PM
 *         Revised: Dec 14, 2009 12:35:29 PM
 */
public class ConcurrentFloatD5TensorShell extends ConcurrentD5TensorShell<Float> implements FloatD5Tensor {
    private final FloatD5Tensor tensor;

    /**
     * Constructor specifying the tensor to wrap and the concurrency policy used for locking the tensor.
     *
     * @param tensor
     *        The tensor to wrap.
     *
     * @param locks
     *        The {@code ConcurrentD5TensorLocks} instance holding the concurrency policy used when locking the tensor.
     */
    public ConcurrentFloatD5TensorShell(FloatD5Tensor tensor, ConcurrentD5TensorLocks locks) {
        super(tensor,locks);
        this.tensor = tensor;
    }
    
    public float getCell(int d0index, int d1index, int d2index, int d3index, int d4index) {
        Lock lock = locks.getReadLock(d0index,d1index,d2index,d3index,d4index);
        lock.lock();
        try {
            return tensor.getCell(d0index,d1index,d2index,d3index,d4index);
        } finally {
            lock.unlock();
        }
    }

    public void setCell(float value, int d0index, int d1index, int d2index, int d3index, int d4index) {
        Lock lock = locks.getWriteLock(d0index,d1index,d2index,d3index,d4index);
        lock.lock();
        try {
            tensor.setCell(value,d0index,d1index,d2index,d3index,d4index);
        } finally {
            lock.unlock();
        }
    }

    public float getCell(int ... indices) {
        TensorImplUtil.checkIndicesLength(this,indices);
        Lock lock = locks.getReadLock(indices[0],indices[1],indices[2],indices[3],indices[4]);
        lock.lock();
        try {
            return tensor.getCell(indices[0],indices[1],indices[2],indices[3],indices[4]);
        } finally {
            lock.unlock();
        }
    }

    public void setCell(float value, int ... indices) {
        TensorImplUtil.checkIndicesLength(this,indices);
        Lock lock = locks.getWriteLock(indices[0],indices[1],indices[2],indices[3],indices[4]);
        lock.lock();
        try {
            tensor.setCell(value,indices[0],indices[1],indices[2],indices[3],indices[4]);
        } finally {
            lock.unlock();
        }
    }

    public void setTensorValues(FloatTypeSafeArray valuesArray) {
        Lock lock = locks.getTensorWriteLock();
        lock.lock();
        try {
            tensor.setTensorValues(valuesArray);
        } finally {
            lock.unlock();
        }
    }

    public FloatTypeSafeArray getTensorValues() {
        Lock lock = locks.getTensorReadLock();
        lock.lock();
        try {
            return tensor.getTensorValues();
        } finally {
            lock.unlock();
        }
    }               

    public FloatTypeSafeArray getTensorValues(Class<Float> type) {
        Lock lock = locks.getTensorReadLock();
        lock.lock();
        try {
            return tensor.getTensorValues(type);
        } finally {
            lock.unlock();
        }
    }

    protected FloatTensor getComposedTensor(Index<?> index) {
        return TensorImplUtil.getComposedTensor(this,index); 
    }

    public <I> IdFloatTensor<I> getReferenceTensor(Index<I> index) {
        return (IdFloatTensor<I>) super.getReferenceTensor(index);
    }
}