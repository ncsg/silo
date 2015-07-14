package com.pb.sawdust.tensor.decorators.concurrent.primitive.size;

import com.pb.sawdust.tensor.TensorImplUtil;
import com.pb.sawdust.tensor.index.Index;
import com.pb.sawdust.tensor.decorators.concurrent.size.ConcurrentD7TensorShell;
import com.pb.sawdust.tensor.decorators.primitive.ByteTensor;
import com.pb.sawdust.tensor.decorators.id.primitive.IdByteTensor;

import com.pb.sawdust.tensor.decorators.primitive.size.ByteD7Tensor;
import com.pb.sawdust.util.array.ByteTypeSafeArray;

import java.util.concurrent.locks.Lock;

/**
 * The {@code ConcurrentByteD7TensorShell} class provides a wrapper for implementations of the {@code ByteD7Tensor} interface
 * with support for concurrent access. The locking policy is set by the {@code D7ConcurrentTensorLocks} implementation used
 * in the class.
 *
 * @author crf <br/>
 *         Started: January 30, 2009 10:47:31 PM
 *         Revised: Dec 14, 2009 12:35:30 PM
 */
public class ConcurrentByteD7TensorShell extends ConcurrentD7TensorShell<Byte> implements ByteD7Tensor {
    private final ByteD7Tensor tensor;

    /**
     * Constructor specifying the tensor to wrap and the concurrency policy used for locking the tensor.
     *
     * @param tensor
     *        The tensor to wrap.
     *
     * @param locks
     *        The {@code ConcurrentD7TensorLocks} instance holding the concurrency policy used when locking the tensor.
     */
    public ConcurrentByteD7TensorShell(ByteD7Tensor tensor, ConcurrentD7TensorLocks locks) {
        super(tensor,locks);
        this.tensor = tensor;
    }
    
    public byte getCell(int d0index, int d1index, int d2index, int d3index, int d4index, int d5index, int d6index) {
        Lock lock = locks.getReadLock(d0index,d1index,d2index,d3index,d4index,d5index,d6index);
        lock.lock();
        try {
            return tensor.getCell(d0index,d1index,d2index,d3index,d4index,d5index,d6index);
        } finally {
            lock.unlock();
        }
    }

    public void setCell(byte value, int d0index, int d1index, int d2index, int d3index, int d4index, int d5index, int d6index) {
        Lock lock = locks.getWriteLock(d0index,d1index,d2index,d3index,d4index,d5index,d6index);
        lock.lock();
        try {
            tensor.setCell(value,d0index,d1index,d2index,d3index,d4index,d5index,d6index);
        } finally {
            lock.unlock();
        }
    }

    public byte getCell(int ... indices) {
        TensorImplUtil.checkIndicesLength(this,indices);
        Lock lock = locks.getReadLock(indices[0],indices[1],indices[2],indices[3],indices[4],indices[5],indices[6]);
        lock.lock();
        try {
            return tensor.getCell(indices[0],indices[1],indices[2],indices[3],indices[4],indices[5],indices[6]);
        } finally {
            lock.unlock();
        }
    }

    public void setCell(byte value, int ... indices) {
        TensorImplUtil.checkIndicesLength(this,indices);
        Lock lock = locks.getWriteLock(indices[0],indices[1],indices[2],indices[3],indices[4],indices[5],indices[6]);
        lock.lock();
        try {
            tensor.setCell(value,indices[0],indices[1],indices[2],indices[3],indices[4],indices[5],indices[6]);
        } finally {
            lock.unlock();
        }
    }

    public void setTensorValues(ByteTypeSafeArray valuesArray) {
        Lock lock = locks.getTensorWriteLock();
        lock.lock();
        try {
            tensor.setTensorValues(valuesArray);
        } finally {
            lock.unlock();
        }
    }

    public ByteTypeSafeArray getTensorValues() {
        Lock lock = locks.getTensorReadLock();
        lock.lock();
        try {
            return tensor.getTensorValues();
        } finally {
            lock.unlock();
        }
    }               

    public ByteTypeSafeArray getTensorValues(Class<Byte> type) {
        Lock lock = locks.getTensorReadLock();
        lock.lock();
        try {
            return tensor.getTensorValues(type);
        } finally {
            lock.unlock();
        }
    }

    protected ByteTensor getComposedTensor(Index<?> index) {
        return TensorImplUtil.getComposedTensor(this,index); 
    }

    public <I> IdByteTensor<I> getReferenceTensor(Index<I> index) {
        return (IdByteTensor<I>) super.getReferenceTensor(index);
    }
}