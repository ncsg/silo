package com.pb.sawdust.tensor.factory;

import com.pb.sawdust.tensor.Tensor;
import com.pb.sawdust.tensor.index.Index;
import com.pb.sawdust.tensor.read.TensorReader;
import com.pb.sawdust.util.JavaType;

import java.util.List;
import java.util.Map;

/**
 * The {@code LiterateTensorFactory} provides a partial implementation of the {@code TensorFactory} and {@code ConcurrentTensorFactory}
 * interfaces covering the methods used to create tensors from {@code TensorReader}s.
 *
 * @author crf <br/>
 *         Started: Dec 5, 2009 3:00:24 PM
 */
public abstract class LiterateTensorFactory extends AbstractTensorFactory {

    @SuppressWarnings("unchecked") //casts are ok because JavaType in reader should ensure safety (in theory)
    public <T,I> Tensor<T> tensor(TensorReader<T,I> reader) {
        JavaType type = reader.getType();
        int[] dimensions = reader.getDimensions();
        Tensor<T> tensor;
        switch (type) {
            case BOOLEAN : tensor = (Tensor<T>) booleanTensor(dimensions); break;
            case CHAR : tensor = (Tensor<T>) charTensor(dimensions); break;
            case BYTE : tensor = (Tensor<T>) byteTensor(dimensions); break;
            case SHORT : tensor = (Tensor<T>) shortTensor(dimensions); break;
            case INT : tensor = (Tensor<T>) intTensor(dimensions); break;
            case LONG : tensor = (Tensor<T>) longTensor(dimensions); break;
            case FLOAT : tensor = (Tensor<T>) floatTensor(dimensions); break;
            case DOUBLE : tensor = (Tensor<T>) doubleTensor(dimensions); break;
            default : tensor = (Tensor<T>) tensor(dimensions); break;
        }
        tensor = reader.fillTensor(tensor);
        Index<?> readerIndex = tensor.getIndex();
        List<List<I>> ids = reader.getIds();
        if (ids != null) {
            switch (type) {
                case BOOLEAN : tensor = (Tensor<T>) booleanTensor(ids,tensor); break;
                case CHAR : tensor = (Tensor<T>) charTensor(ids,tensor); break;
                case BYTE : tensor = (Tensor<T>) byteTensor(ids,tensor); break;
                case SHORT : tensor = (Tensor<T>) shortTensor(ids,tensor); break;
                case INT : tensor = (Tensor<T>) intTensor(ids,tensor); break;
                case LONG : tensor = (Tensor<T>) longTensor(ids,tensor); break;
                case FLOAT : tensor = (Tensor<T>) floatTensor(ids,tensor); break;
                case DOUBLE : tensor = (Tensor<T>) doubleTensor(ids,tensor); break;
                default : tensor = tensor(ids,tensor); break;
            }
        }
        Map<String,Object> metadata = reader.getTensorMetadata();
        for (String key : metadata.keySet())
            tensor.setMetadataValue(key,metadata.get(key));
        for (String key : readerIndex.getMetadataKeys())
            tensor.getIndex().setMetadataValue(key,readerIndex.getMetadataValue(key));
        return tensor;
    }

    @SuppressWarnings("unchecked") //casts are ok because JavaType in reader should ensure safety (in theory)
    public <T,I> Tensor<T> concurrentTensor(TensorReader<T,I> reader, int concurrencyLevel) {
        JavaType type = reader.getType();
        int[] dimensions = reader.getDimensions();
        Tensor<T> tensor;
        switch (type) {
            case BOOLEAN : tensor = (Tensor<T>) concurrentBooleanTensor(concurrencyLevel,dimensions); break;
            case CHAR : tensor = (Tensor<T>) concurrentCharTensor(concurrencyLevel,dimensions); break;
            case BYTE : tensor = (Tensor<T>) concurrentByteTensor(concurrencyLevel,dimensions); break;
            case SHORT : tensor = (Tensor<T>) concurrentShortTensor(concurrencyLevel,dimensions); break;
            case INT : tensor = (Tensor<T>) concurrentIntTensor(concurrencyLevel,dimensions); break;
            case LONG : tensor = (Tensor<T>) concurrentLongTensor(concurrencyLevel,dimensions); break;
            case FLOAT : tensor = (Tensor<T>) concurrentFloatTensor(concurrencyLevel,dimensions); break;
            case DOUBLE : tensor = (Tensor<T>) concurrentDoubleTensor(concurrencyLevel,dimensions); break;
            default : tensor = (Tensor<T>) tensor(dimensions); break;
        }
        tensor = reader.fillTensor(tensor);
        Index<?> readerIndex = tensor.getIndex();
        List<List<I>> ids = reader.getIds();
        if (ids != null) {
            switch (type) {
                case BOOLEAN : tensor = (Tensor<T>) booleanTensor(ids,tensor); break;
                case CHAR : tensor = (Tensor<T>) charTensor(ids,tensor); break;
                case BYTE : tensor = (Tensor<T>) byteTensor(ids,tensor); break;
                case SHORT : tensor = (Tensor<T>) shortTensor(ids,tensor); break;
                case INT : tensor = (Tensor<T>) intTensor(ids,tensor); break;
                case LONG : tensor = (Tensor<T>) longTensor(ids,tensor); break;
                case FLOAT : tensor = (Tensor<T>) floatTensor(ids,tensor); break;
                case DOUBLE : tensor = (Tensor<T>) doubleTensor(ids,tensor); break;
                default : tensor = tensor(ids,tensor); break;
            }
        }
        Map<String,Object> metadata = reader.getTensorMetadata();
        for (String key : metadata.keySet())
            tensor.setMetadataValue(key,metadata.get(key));
        for (String key : readerIndex.getMetadataKeys())
            tensor.getIndex().setMetadataValue(key,readerIndex.getMetadataValue(key));
        return tensor;
    }

}