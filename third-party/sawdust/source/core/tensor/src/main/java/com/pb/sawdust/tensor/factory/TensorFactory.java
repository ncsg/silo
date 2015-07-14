package com.pb.sawdust.tensor.factory;

import com.pb.sawdust.tensor.Tensor;
import com.pb.sawdust.tensor.alias.matrix.Matrix;
import com.pb.sawdust.tensor.alias.matrix.id.*;
import com.pb.sawdust.tensor.alias.matrix.primitive.*;
import com.pb.sawdust.tensor.alias.scalar.Scalar;
import com.pb.sawdust.tensor.alias.scalar.id.*;
import com.pb.sawdust.tensor.alias.scalar.primitive.*;
import com.pb.sawdust.tensor.alias.vector.Vector;
import com.pb.sawdust.tensor.alias.vector.id.*;
import com.pb.sawdust.tensor.alias.vector.primitive.*;
import com.pb.sawdust.tensor.decorators.primitive.*;
import com.pb.sawdust.tensor.decorators.id.primitive.*;
import com.pb.sawdust.tensor.decorators.id.*;
import com.pb.sawdust.tensor.read.TensorReader;

import java.util.List;

/**
 * The {@code TensorFactory} interface specifies a structure for factories which will generate concrete {@code Tensor} 
 * implementation instances. Methods are specified for generating both generic ("{@code Object}") and primitive tensors,
 * tensors with or withour ids, tensors initialized with default values, and tensor copies. Though no methods explicitly
 * return "sized" tensors, the methods' documentation establishes that all returned tensorss less than rank 10 will be
 * castable to the appropriate {@code DnTensor} (or {@code Scalar},{@code Vector}, {@code Marix}), where {@code n} is the
 * tenosor rank; thus support for generating sized tensors is implicitly included.
 * 
 * @author crf <br/>
 *         Started: June 11, 2009 11:27:32 AM
 *         Revised: Dec 14, 2009 12:35:35 PM
 */
public interface TensorFactory {
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code byte}s. 
     * The tensor will be initialized with default {@code byte} values as specified by the Java Language Specification. Though
     * the tensor returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code ByteDnTensor}, where {@code n} is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then 
     * the returned tensor will be a {@code ByteScalar}, {@code ByteVector}, or {@code ByteMatrix}, respectively. That is, this tensor may be cast to the
     * appropriate sized tensor if that interface's methods are needed.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    ByteTensor byteTensor(int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code byte}s and
     * will be filled with a user-specified default value. Though the tensor returned is not explicitly sized, if the dimension count 
     * is between 0 and 9 then the returned tensor will actually be a {@code ByteDnTensor}, where {@code n} is the number of tensor 
     * dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code ByteScalar}, {@code ByteVector}, or 
     * {@code ByteMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions filled with {@code defaultValue}.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    ByteTensor initializedByteTensor(byte defaultValue, int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code byte}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code byte} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdByteDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions.  If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdByteScalar}, 
     * {@code IdByteVector}, or {@code IdByteMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdByteTensor<I> byteTensor(List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code byte}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdByteDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdByteScalar}, {@code IdByteVector}, or {@code IdByteMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdByteTensor<I> initializedByteTensor(byte defaultValue, List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code byte}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code byte} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdByteDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdByteScalar}, 
     * {@code IdByteVector}, or {@code IdByteMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdByteTensor<I> byteTensor(I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code byte}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdByteDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdByteScalar}, {@code IdByteVector}, or {@code IdByteMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdByteTensor<I> initializedByteTensor(byte defaultValue, I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code short}s. 
     * The tensor will be initialized with default {@code short} values as specified by the Java Language Specification. Though
     * the tensor returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code ShortDnTensor}, where {@code n} is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then 
     * the returned tensor will be a {@code ShortScalar}, {@code ShortVector}, or {@code ShortMatrix}, respectively. That is, this tensor may be cast to the
     * appropriate sized tensor if that interface's methods are needed.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    ShortTensor shortTensor(int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code short}s and
     * will be filled with a user-specified default value. Though the tensor returned is not explicitly sized, if the dimension count 
     * is between 0 and 9 then the returned tensor will actually be a {@code ShortDnTensor}, where {@code n} is the number of tensor 
     * dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code ShortScalar}, {@code ShortVector}, or 
     * {@code ShortMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions filled with {@code defaultValue}.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    ShortTensor initializedShortTensor(short defaultValue, int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code short}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code short} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdShortDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions.  If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdShortScalar}, 
     * {@code IdShortVector}, or {@code IdShortMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdShortTensor<I> shortTensor(List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code short}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdShortDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdShortScalar}, {@code IdShortVector}, or {@code IdShortMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdShortTensor<I> initializedShortTensor(short defaultValue, List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code short}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code short} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdShortDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdShortScalar}, 
     * {@code IdShortVector}, or {@code IdShortMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdShortTensor<I> shortTensor(I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code short}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdShortDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdShortScalar}, {@code IdShortVector}, or {@code IdShortMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdShortTensor<I> initializedShortTensor(short defaultValue, I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code int}s. 
     * The tensor will be initialized with default {@code int} values as specified by the Java Language Specification. Though
     * the tensor returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IntDnTensor}, where {@code n} is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then 
     * the returned tensor will be a {@code IntScalar}, {@code IntVector}, or {@code IntMatrix}, respectively. That is, this tensor may be cast to the
     * appropriate sized tensor if that interface's methods are needed.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    IntTensor intTensor(int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code int}s and
     * will be filled with a user-specified default value. Though the tensor returned is not explicitly sized, if the dimension count 
     * is between 0 and 9 then the returned tensor will actually be a {@code IntDnTensor}, where {@code n} is the number of tensor 
     * dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IntScalar}, {@code IntVector}, or 
     * {@code IntMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions filled with {@code defaultValue}.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    IntTensor initializedIntTensor(int defaultValue, int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code int}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code int} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdIntDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions.  If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdIntScalar}, 
     * {@code IdIntVector}, or {@code IdIntMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdIntTensor<I> intTensor(List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code int}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdIntDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdIntScalar}, {@code IdIntVector}, or {@code IdIntMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdIntTensor<I> initializedIntTensor(int defaultValue, List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code int}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code int} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdIntDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdIntScalar}, 
     * {@code IdIntVector}, or {@code IdIntMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdIntTensor<I> intTensor(I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code int}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdIntDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdIntScalar}, {@code IdIntVector}, or {@code IdIntMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdIntTensor<I> initializedIntTensor(int defaultValue, I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code long}s. 
     * The tensor will be initialized with default {@code long} values as specified by the Java Language Specification. Though
     * the tensor returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code LongDnTensor}, where {@code n} is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then 
     * the returned tensor will be a {@code LongScalar}, {@code LongVector}, or {@code LongMatrix}, respectively. That is, this tensor may be cast to the
     * appropriate sized tensor if that interface's methods are needed.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    LongTensor longTensor(int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code long}s and
     * will be filled with a user-specified default value. Though the tensor returned is not explicitly sized, if the dimension count 
     * is between 0 and 9 then the returned tensor will actually be a {@code LongDnTensor}, where {@code n} is the number of tensor 
     * dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code LongScalar}, {@code LongVector}, or 
     * {@code LongMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions filled with {@code defaultValue}.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    LongTensor initializedLongTensor(long defaultValue, int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code long}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code long} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdLongDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions.  If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdLongScalar}, 
     * {@code IdLongVector}, or {@code IdLongMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdLongTensor<I> longTensor(List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code long}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdLongDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdLongScalar}, {@code IdLongVector}, or {@code IdLongMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdLongTensor<I> initializedLongTensor(long defaultValue, List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code long}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code long} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdLongDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdLongScalar}, 
     * {@code IdLongVector}, or {@code IdLongMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdLongTensor<I> longTensor(I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code long}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdLongDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdLongScalar}, {@code IdLongVector}, or {@code IdLongMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdLongTensor<I> initializedLongTensor(long defaultValue, I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code float}s. 
     * The tensor will be initialized with default {@code float} values as specified by the Java Language Specification. Though
     * the tensor returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code FloatDnTensor}, where {@code n} is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then 
     * the returned tensor will be a {@code FloatScalar}, {@code FloatVector}, or {@code FloatMatrix}, respectively. That is, this tensor may be cast to the
     * appropriate sized tensor if that interface's methods are needed.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    FloatTensor floatTensor(int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code float}s and
     * will be filled with a user-specified default value. Though the tensor returned is not explicitly sized, if the dimension count 
     * is between 0 and 9 then the returned tensor will actually be a {@code FloatDnTensor}, where {@code n} is the number of tensor 
     * dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code FloatScalar}, {@code FloatVector}, or 
     * {@code FloatMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions filled with {@code defaultValue}.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    FloatTensor initializedFloatTensor(float defaultValue, int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code float}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code float} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdFloatDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions.  If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdFloatScalar}, 
     * {@code IdFloatVector}, or {@code IdFloatMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdFloatTensor<I> floatTensor(List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code float}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdFloatDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdFloatScalar}, {@code IdFloatVector}, or {@code IdFloatMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdFloatTensor<I> initializedFloatTensor(float defaultValue, List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code float}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code float} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdFloatDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdFloatScalar}, 
     * {@code IdFloatVector}, or {@code IdFloatMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdFloatTensor<I> floatTensor(I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code float}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdFloatDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdFloatScalar}, {@code IdFloatVector}, or {@code IdFloatMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdFloatTensor<I> initializedFloatTensor(float defaultValue, I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code double}s. 
     * The tensor will be initialized with default {@code double} values as specified by the Java Language Specification. Though
     * the tensor returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code DoubleDnTensor}, where {@code n} is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then 
     * the returned tensor will be a {@code DoubleScalar}, {@code DoubleVector}, or {@code DoubleMatrix}, respectively. That is, this tensor may be cast to the
     * appropriate sized tensor if that interface's methods are needed.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    DoubleTensor doubleTensor(int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code double}s and
     * will be filled with a user-specified default value. Though the tensor returned is not explicitly sized, if the dimension count 
     * is between 0 and 9 then the returned tensor will actually be a {@code DoubleDnTensor}, where {@code n} is the number of tensor 
     * dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code DoubleScalar}, {@code DoubleVector}, or 
     * {@code DoubleMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions filled with {@code defaultValue}.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    DoubleTensor initializedDoubleTensor(double defaultValue, int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code double}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code double} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdDoubleDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions.  If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdDoubleScalar}, 
     * {@code IdDoubleVector}, or {@code IdDoubleMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdDoubleTensor<I> doubleTensor(List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code double}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdDoubleDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdDoubleScalar}, {@code IdDoubleVector}, or {@code IdDoubleMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdDoubleTensor<I> initializedDoubleTensor(double defaultValue, List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code double}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code double} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdDoubleDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdDoubleScalar}, 
     * {@code IdDoubleVector}, or {@code IdDoubleMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdDoubleTensor<I> doubleTensor(I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code double}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdDoubleDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdDoubleScalar}, {@code IdDoubleVector}, or {@code IdDoubleMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdDoubleTensor<I> initializedDoubleTensor(double defaultValue, I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code char}s. 
     * The tensor will be initialized with default {@code char} values as specified by the Java Language Specification. Though
     * the tensor returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code CharDnTensor}, where {@code n} is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then 
     * the returned tensor will be a {@code CharScalar}, {@code CharVector}, or {@code CharMatrix}, respectively. That is, this tensor may be cast to the
     * appropriate sized tensor if that interface's methods are needed.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    CharTensor charTensor(int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code char}s and
     * will be filled with a user-specified default value. Though the tensor returned is not explicitly sized, if the dimension count 
     * is between 0 and 9 then the returned tensor will actually be a {@code CharDnTensor}, where {@code n} is the number of tensor 
     * dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code CharScalar}, {@code CharVector}, or 
     * {@code CharMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions filled with {@code defaultValue}.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    CharTensor initializedCharTensor(char defaultValue, int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code char}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code char} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdCharDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions.  If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdCharScalar}, 
     * {@code IdCharVector}, or {@code IdCharMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdCharTensor<I> charTensor(List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code char}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdCharDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdCharScalar}, {@code IdCharVector}, or {@code IdCharMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdCharTensor<I> initializedCharTensor(char defaultValue, List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code char}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code char} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdCharDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdCharScalar}, 
     * {@code IdCharVector}, or {@code IdCharMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdCharTensor<I> charTensor(I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code char}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdCharDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdCharScalar}, {@code IdCharVector}, or {@code IdCharMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdCharTensor<I> initializedCharTensor(char defaultValue, I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code boolean}s. 
     * The tensor will be initialized with default {@code boolean} values as specified by the Java Language Specification. Though
     * the tensor returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code BooleanDnTensor}, where {@code n} is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then 
     * the returned tensor will be a {@code BooleanScalar}, {@code BooleanVector}, or {@code BooleanMatrix}, respectively. That is, this tensor may be cast to the
     * appropriate sized tensor if that interface's methods are needed.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    BooleanTensor booleanTensor(int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code boolean}s and
     * will be filled with a user-specified default value. Though the tensor returned is not explicitly sized, if the dimension count 
     * is between 0 and 9 then the returned tensor will actually be a {@code BooleanDnTensor}, where {@code n} is the number of tensor 
     * dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code BooleanScalar}, {@code BooleanVector}, or 
     * {@code BooleanMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions filled with {@code defaultValue}.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    BooleanTensor initializedBooleanTensor(boolean defaultValue, int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code boolean}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code boolean} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdBooleanDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions.  If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdBooleanScalar}, 
     * {@code IdBooleanVector}, or {@code IdBooleanMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdBooleanTensor<I> booleanTensor(List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code boolean}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdBooleanDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdBooleanScalar}, {@code IdBooleanVector}, or {@code IdBooleanMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <I> IdBooleanTensor<I> initializedBooleanTensor(boolean defaultValue, List<List<I>> ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code boolean}s and
     * will have its indices accessible by ids. The tensor will be initialized with default {@code boolean} values as
     * specified by the Java Language Specification. Though the tensor returned is not explicitly sized, if the dimension
     * count is between 0 and 9 then the returned tensor will actually be a {@code IdBooleanDnTensor<I>}, where {@code n} is
     * the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor will be a {@code IdBooleanScalar}, 
     * {@code IdBooleanVector}, or {@code IdBooleanMatrix}, respectively. That is, this tensor may be cast to the appropriate sized tensor if that interface's
     * methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdBooleanTensor<I> booleanTensor(I[][] ids, int ... dimensions);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold {@code boolean}s,
     * will be filled with a user-specified default value, and will have its indices accessible by ids. Though the tensor
     * returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually
     * be a {@code IdBooleanDnTensor<I>}, where {@code n} is the number of tensor dimensions. If the dimension count is 
     * 0, 1, or 2, then the returned tensor will be a {@code IdBooleanScalar}, {@code IdBooleanVector}, or {@code IdBooleanMatrix}, respectively. 
     * That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <I> IdBooleanTensor<I> initializedBooleanTensor(boolean defaultValue, I[][] ids, int ... dimensions);

    /**
     * Copy a tensor into a new tensor. This method will make a copy, not a reference tensor, so changes to the
     * source tensor will not be reflected in the new tensor (and vice-versa). Though the tensor returned is not
     * explicitly typed nor sized, if the source tensor holds primitive types and/or is up to 9 dimensions in size,
     * the returned tensor will implement the appropriate interface from the {@code com.pb.sawdust.tensor.decorators}
     * packages. (<i>e.g.</i> if the source tensor is a {@code BooleanD2Tensor}, the returned tensor could be validly
     * cast to that type).
     *
     * @param <T>
     *        The type the tensors (source and returned) hold.
     *
     * @param tensor
     *        The source tensor to copy.
     *
     * @return a copy of {@code tensor}.
     */
    <T> Tensor<T> copyTensor(Tensor<T> tensor);

    /**
     * Copy an id tensor into a new tensor. This method will make a copy, not a reference tensor, so changes to the
     * source tensor will not be reflected in the new tensor (and vice-versa). Though the tensor returned is not
     * explicitly typed nor sized, if the source tensor holds primitive types and/or is up to 9 dimensions in size,
     * the returned tensor will implement the appropriate interface from the {@code com.pb.sawdust.tensor.decorators}
     * packages. (<i>e.g.</i> if the source tensor is a {@code IdBooleanD2Tensor}, the returned tensor could be validly
     * cast to that type).
     *
     * @param <T>
     *        The type the tensors (source and returned) hold.
     *
     * @param <I>
     *        The type of the tensor ids.
     * 
     * @param tensor
     *        The source tensor to copy.
     * 
     * @return a copy of {@code tensor}.
     */
    <T,I> IdTensor<T,I> copyTensor(IdTensor<T,I> tensor);

    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold the specified type. 
     * The tensor will be initialized with default {@code null} values. Though the tensor returned is not explicitly sized, if the 
     * dimension count is between 0 and 9 then the returned tensor will actually be a {@code DnTensor}, where {@code n} 
     * is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor
     * will be a {@code Scalar}, {@code Vector}, or {@code Matrix}, respectively.
     *  That is, this tensor may be cast to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <T>
     *        The type this tensor will hold.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     */
    <T> Tensor<T> tensor(int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will be filled with a user-specified default value. 
     * Though the tensor returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually be 
     * a {@code DnTensor}, where {@code n} is the number of tensor  dimensions. If the dimension count is 0, 1, or 2, then the returned tensor
     * will be a {@code Scalar}, {@code Vector}, or {@code Matrix}, respectively. That is, this tensor may be cast to the appropriate 
     * sized tensor if that interface's methods are needed.
     *
     * @param <T>
     *        The type this tensor will hold.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions filled with {@code defaultValue}.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1.
     * @throws NullPointerException if {@code defaultValue == null}.
     */
    <T> Tensor<T> initializedTensor(T defaultValue, int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold the specified type
     * and will have its indices accessible by ids. The tensor will be initialized with default {@code null} values. Though 
     * the tensor returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will 
     * actually be a {@code IdDnTensor}, where {@code n} is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor
     * will be a {@code IDScalar}, {@code IdVector}, or {@code IdMatrix}, respectively. That is, this tensor may be cast 
     * to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <T>
     *        The type this tensor will hold.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     */
    <T,I> IdTensor<T,I> tensor(List<List<I>> ids, int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold the specified type, will 
     * be filled with a user-specified default value and will have its indices accessible by ids. Though  the tensor returned is 
     * not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually be a {@code IdDnTensor}, 
     * where {@code n} is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor
     * will be a {@code IDScalar}, {@code IdVector}, or {@code IdMatrix}, respectively. That is, this tensor may be cast  to the appropriate sized tensor if that 
     * interface's methods are needed.
     *
     * @param <T>
     *        The type this tensor will hold.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one list per dimension, and this list's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.size() != dimensions.length},
     *                                  if any of the id lists' sizes do not match their respective dimension's size, or
     *                                  if any of the id lists contains repeated elements.
     * @throws NullPointerException if {@code defaultValue == null}.
     */
    <T,I> IdTensor<T,I> initializedTensor(T defaultValue, List<List<I>> ids, int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold the specified type
     * and will have its indices accessible by ids. The tensor will be initialized with default {@code null} values. Though 
     * the tensor returned is not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will 
     * actually be a {@code IdDnTensor}, where {@code n} is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor
     * will be a {@code IDScalar}, {@code IdVector}, or {@code IdMatrix}, respectively. That is, this tensor may be cast 
     * to the appropriate sized tensor if that interface's methods are needed.
     *
     * @param <T>
     *        The type this tensor will hold.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     */
    <T,I> IdTensor<T,I> tensor(I[][] ids, int ... dimensions);
    
    /**
     * Factory method used to get a {@code Tensor} of the specified dimensions which will hold the specified type, will 
     * be filled with a user-specified default value and will have its indices accessible by ids. Though  the tensor returned is 
     * not explicitly sized, if the dimension count is between 0 and 9 then the returned tensor will actually be a {@code IdDnTensor}, 
     * where {@code n} is the number of tensor dimensions. If the dimension count is 0, 1, or 2, then the returned tensor
     * will be a {@code IDScalar}, {@code IdVector}, or {@code IdMatrix}, respectively. That is, this tensor may be cast  to the appropriate sized tensor if that 
     * interface's methods are needed.
     *
     * @param <T>
     *        The type this tensor will hold.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param defaultValue
     *        The default value that the tensor will be filled with.
     *
     * @param ids
     *        The ids to be used for index referencing. There should be one array per dimension, and this array's length
     *        should match that dimension's size.
     *
     * @param dimensions
     *        The array specifying the dimensionality of the tensor. The length of this array defines the number of dimensions in
     *        the tensor, and the size of each element specifies the size of each tensor dimension.
     *
     * @return a tensor with the specified dimensions.
     *
     * @throws IllegalArgumentException if any element in {@code dimensions} is less than 1, if {@code ids.length != dimensions.length},
     *                                  if any of the id arrays' lengths do not match their respective dimension's size, or
     *                                  if any of the id arrays contains repeated elements.
     * @throws NullPointerException if {@code defaultValue == null}.
     */
    <T,I> IdTensor<T,I> initializedTensor(T defaultValue, I[][] ids, int ... dimensions);

    /**
     * Create a tensor using a reader. If ids are specified by the reader, then the returned tensor is guaranteed to be a 
     * (subclass of a) {@code IdTensor<T,I>}. The returned tensor will also implement the expected size and type interfaces
     * to which it naturally belongs.
     *
     * @param <T> the type held by the tensor.
     * 
     * @param <I> the type of the ids of the tensor (if used).
     *
     * @param reader the reader used to read in the tensor specifications and data.
     *
     * @return a tensor created from {@code reader}.
     **/
    <T,I> Tensor<T> tensor(TensorReader<T,I> reader);  

    /**
     * Factory method to get a scalar that holds a {@code byte}.
     *
     * @return a byte scalar.
     */
    public ByteScalar byteScalar();

    /**
     * Factory method to get a scalar that holds a {@code byte} which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @return a byte scalar with the specified size.
     */
    public ByteScalar initializedByteScalar(byte value) ;

    /**
     * Factory method to get a scalar that holds a {@code byte} with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @param ids
     *        The ids for the scalar.
     *
     * @param d0
     *        The size of the scalar.
     *
     * @return a byte scalar with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdByteScalar<I> initializedByteScalar(byte value, I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code byte}s.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a byte vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public ByteVector byteVector(int d0) ;

    /**
     * Factory method to get a vector that holds {@code byte}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a byte vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdByteVector<I> byteVector(List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code byte}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a byte vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdByteVector<I> byteVector(I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code byte}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a byte vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public ByteVector initializedByteVector(byte value, int d0) ;

    /**
     * Factory method to get a vector that holds {@code byte}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a byte vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdByteVector<I> initializedByteVector(byte value, List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code byte}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a byte vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdByteVector<I> initializedByteVector(byte value, I[] ids, int d0) ;

    /**
     * Factory method to get a matrix that holds {@code byte}s.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a byte matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public ByteMatrix byteMatrix(int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code byte}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a byte matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdByteMatrix<I> byteMatrix(List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code byte}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a byte matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdByteMatrix<I> byteMatrix(I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code byte}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a byte matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public ByteMatrix initializedByteMatrix(byte value, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code byte}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a byte matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdByteMatrix<I> initializedByteMatrix(byte value,List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code byte}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a byte matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdByteMatrix<I> initializedByteMatrix(byte value, I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a scalar that holds a {@code short}.
     *
     * @return a short scalar.
     */
    public ShortScalar shortScalar() ;

    /**
     * Factory method to get a scalar that holds a {@code short} which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @return a short scalar with the specified size.
     */
    public ShortScalar initializedShortScalar(short value) ;

    /**
     * Factory method to get a scalar that holds a {@code short} with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @param ids
     *        The ids for the scalar.
     *
     * @param d0
     *        The size of the scalar.
     *
     * @return a short scalar with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdShortScalar<I> initializedShortScalar(short value, I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code short}s.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a short vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public ShortVector shortVector(int d0) ;

    /**
     * Factory method to get a vector that holds {@code short}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a short vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdShortVector<I> shortVector(List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code short}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a short vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdShortVector<I> shortVector(I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code short}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a short vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public ShortVector initializedShortVector(short value, int d0) ;

    /**
     * Factory method to get a vector that holds {@code short}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a short vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdShortVector<I> initializedShortVector(short value, List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code short}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a short vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdShortVector<I> initializedShortVector(short value, I[] ids, int d0) ;

    /**
     * Factory method to get a matrix that holds {@code short}s.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a short matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public ShortMatrix shortMatrix(int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code short}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a short matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdShortMatrix<I> shortMatrix(List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code short}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a short matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdShortMatrix<I> shortMatrix(I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code short}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a short matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public ShortMatrix initializedShortMatrix(short value, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code short}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a short matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdShortMatrix<I> initializedShortMatrix(short value,List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code short}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a short matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdShortMatrix<I> initializedShortMatrix(short value, I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a scalar that holds a {@code int}.
     *
     * @return a int scalar.
     */
    public IntScalar intScalar() ;

    /**
     * Factory method to get a scalar that holds a {@code int} which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @return a int scalar with the specified size.
     */
    public IntScalar initializedIntScalar(int value) ;

    /**
     * Factory method to get a scalar that holds a {@code int} with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @param ids
     *        The ids for the scalar.
     *
     * @param d0
     *        The size of the scalar.
     *
     * @return a int scalar with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdIntScalar<I> initializedIntScalar(int value, I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code int}s.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a int vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public IntVector intVector(int d0) ;

    /**
     * Factory method to get a vector that holds {@code int}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a int vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdIntVector<I> intVector(List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code int}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a int vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdIntVector<I> intVector(I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code int}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a int vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public IntVector initializedIntVector(int value, int d0) ;

    /**
     * Factory method to get a vector that holds {@code int}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a int vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdIntVector<I> initializedIntVector(int value, List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code int}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a int vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdIntVector<I> initializedIntVector(int value, I[] ids, int d0) ;

    /**
     * Factory method to get a matrix that holds {@code int}s.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a int matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public IntMatrix intMatrix(int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code int}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a int matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdIntMatrix<I> intMatrix(List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code int}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a int matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdIntMatrix<I> intMatrix(I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code int}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a int matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public IntMatrix initializedIntMatrix(int value, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code int}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a int matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdIntMatrix<I> initializedIntMatrix(int value,List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code int}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a int matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdIntMatrix<I> initializedIntMatrix(int value, I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a scalar that holds a {@code long}.
     *
     * @return a long scalar.
     */
    public LongScalar longScalar() ;

    /**
     * Factory method to get a scalar that holds a {@code long} which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @return a long scalar with the specified size.
     */
    public LongScalar initializedLongScalar(long value) ;

    /**
     * Factory method to get a scalar that holds a {@code long} with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @param ids
     *        The ids for the scalar.
     *
     * @param d0
     *        The size of the scalar.
     *
     * @return a long scalar with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdLongScalar<I> initializedLongScalar(long value, I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code long}s.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a long vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public LongVector longVector(int d0) ;

    /**
     * Factory method to get a vector that holds {@code long}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a long vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdLongVector<I> longVector(List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code long}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a long vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdLongVector<I> longVector(I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code long}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a long vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public LongVector initializedLongVector(long value, int d0) ;

    /**
     * Factory method to get a vector that holds {@code long}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a long vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdLongVector<I> initializedLongVector(long value, List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code long}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a long vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdLongVector<I> initializedLongVector(long value, I[] ids, int d0) ;

    /**
     * Factory method to get a matrix that holds {@code long}s.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a long matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public LongMatrix longMatrix(int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code long}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a long matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdLongMatrix<I> longMatrix(List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code long}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a long matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdLongMatrix<I> longMatrix(I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code long}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a long matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public LongMatrix initializedLongMatrix(long value, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code long}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a long matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdLongMatrix<I> initializedLongMatrix(long value,List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code long}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a long matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdLongMatrix<I> initializedLongMatrix(long value, I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a scalar that holds a {@code float}.
     *
     * @return a float scalar.
     */
    public FloatScalar floatScalar() ;

    /**
     * Factory method to get a scalar that holds a {@code float} which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @return a float scalar with the specified size.
     */
    public FloatScalar initializedFloatScalar(float value) ;

    /**
     * Factory method to get a scalar that holds a {@code float} with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @param ids
     *        The ids for the scalar.
     *
     * @param d0
     *        The size of the scalar.
     *
     * @return a float scalar with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdFloatScalar<I> initializedFloatScalar(float value, I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code float}s.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a float vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public FloatVector floatVector(int d0) ;

    /**
     * Factory method to get a vector that holds {@code float}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a float vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdFloatVector<I> floatVector(List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code float}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a float vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdFloatVector<I> floatVector(I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code float}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a float vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public FloatVector initializedFloatVector(float value, int d0) ;

    /**
     * Factory method to get a vector that holds {@code float}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a float vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdFloatVector<I> initializedFloatVector(float value, List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code float}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a float vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdFloatVector<I> initializedFloatVector(float value, I[] ids, int d0) ;

    /**
     * Factory method to get a matrix that holds {@code float}s.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a float matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public FloatMatrix floatMatrix(int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code float}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a float matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdFloatMatrix<I> floatMatrix(List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code float}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a float matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdFloatMatrix<I> floatMatrix(I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code float}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a float matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public FloatMatrix initializedFloatMatrix(float value, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code float}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a float matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdFloatMatrix<I> initializedFloatMatrix(float value,List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code float}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a float matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdFloatMatrix<I> initializedFloatMatrix(float value, I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a scalar that holds a {@code double}.
     *
     * @return a double scalar.
     */
    public DoubleScalar doubleScalar() ;

    /**
     * Factory method to get a scalar that holds a {@code double} which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @return a double scalar with the specified size.
     */
    public DoubleScalar initializedDoubleScalar(double value) ;

    /**
     * Factory method to get a scalar that holds a {@code double} with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @param ids
     *        The ids for the scalar.
     *
     * @param d0
     *        The size of the scalar.
     *
     * @return a double scalar with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdDoubleScalar<I> initializedDoubleScalar(double value, I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code double}s.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a double vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public DoubleVector doubleVector(int d0) ;

    /**
     * Factory method to get a vector that holds {@code double}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a double vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdDoubleVector<I> doubleVector(List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code double}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a double vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdDoubleVector<I> doubleVector(I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code double}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a double vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public DoubleVector initializedDoubleVector(double value, int d0) ;

    /**
     * Factory method to get a vector that holds {@code double}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a double vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdDoubleVector<I> initializedDoubleVector(double value, List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code double}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a double vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdDoubleVector<I> initializedDoubleVector(double value, I[] ids, int d0) ;

    /**
     * Factory method to get a matrix that holds {@code double}s.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a double matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public DoubleMatrix doubleMatrix(int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code double}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a double matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdDoubleMatrix<I> doubleMatrix(List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code double}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a double matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdDoubleMatrix<I> doubleMatrix(I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code double}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a double matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public DoubleMatrix initializedDoubleMatrix(double value, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code double}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a double matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdDoubleMatrix<I> initializedDoubleMatrix(double value,List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code double}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a double matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdDoubleMatrix<I> initializedDoubleMatrix(double value, I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a scalar that holds a {@code char}.
     *
     * @return a char scalar.
     */
    public CharScalar charScalar() ;

    /**
     * Factory method to get a scalar that holds a {@code char} which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @return a char scalar with the specified size.
     */
    public CharScalar initializedCharScalar(char value) ;

    /**
     * Factory method to get a scalar that holds a {@code char} with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @param ids
     *        The ids for the scalar.
     *
     * @param d0
     *        The size of the scalar.
     *
     * @return a char scalar with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdCharScalar<I> initializedCharScalar(char value, I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code char}s.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a char vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public CharVector charVector(int d0) ;

    /**
     * Factory method to get a vector that holds {@code char}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a char vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdCharVector<I> charVector(List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code char}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a char vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdCharVector<I> charVector(I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code char}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a char vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public CharVector initializedCharVector(char value, int d0) ;

    /**
     * Factory method to get a vector that holds {@code char}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a char vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdCharVector<I> initializedCharVector(char value, List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code char}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a char vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdCharVector<I> initializedCharVector(char value, I[] ids, int d0) ;

    /**
     * Factory method to get a matrix that holds {@code char}s.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a char matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public CharMatrix charMatrix(int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code char}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a char matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdCharMatrix<I> charMatrix(List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code char}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a char matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdCharMatrix<I> charMatrix(I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code char}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a char matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public CharMatrix initializedCharMatrix(char value, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code char}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a char matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdCharMatrix<I> initializedCharMatrix(char value,List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code char}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a char matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdCharMatrix<I> initializedCharMatrix(char value, I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a scalar that holds a {@code boolean}.
     *
     * @return a boolean scalar.
     */
    public BooleanScalar booleanScalar() ;

    /**
     * Factory method to get a scalar that holds a {@code boolean} which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @return a boolean scalar with the specified size.
     */
    public BooleanScalar initializedBooleanScalar(boolean value) ;

    /**
     * Factory method to get a scalar that holds a {@code boolean} with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @param ids
     *        The ids for the scalar.
     *
     * @param d0
     *        The size of the scalar.
     *
     * @return a boolean scalar with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdBooleanScalar<I> initializedBooleanScalar(boolean value, I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code boolean}s.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a boolean vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public BooleanVector booleanVector(int d0) ;

    /**
     * Factory method to get a vector that holds {@code boolean}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a boolean vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdBooleanVector<I> booleanVector(List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code boolean}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a boolean vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdBooleanVector<I> booleanVector(I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code boolean}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a boolean vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public BooleanVector initializedBooleanVector(boolean value, int d0) ;

    /**
     * Factory method to get a vector that holds {@code boolean}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a boolean vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdBooleanVector<I> initializedBooleanVector(boolean value, List<I> ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code boolean}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a boolean vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <I> IdBooleanVector<I> initializedBooleanVector(boolean value, I[] ids, int d0) ;

    /**
     * Factory method to get a matrix that holds {@code boolean}s.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a boolean matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public BooleanMatrix booleanMatrix(int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code boolean}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a boolean matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdBooleanMatrix<I> booleanMatrix(List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code boolean}s with indices accessible by ids.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a boolean matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdBooleanMatrix<I> booleanMatrix(I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code boolean}s which is initially filled with a specified value.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a boolean matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public BooleanMatrix initializedBooleanMatrix(boolean value, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code boolean}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a boolean matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdBooleanMatrix<I> initializedBooleanMatrix(boolean value,List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix that holds {@code boolean}s with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a boolean matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <I> IdBooleanMatrix<I> initializedBooleanMatrix(boolean value, I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a scalar that holds a {@code }.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @return a scalar.
     */
    public <T> Scalar<T> scalar() ;

    /**
     * Factory method to get a scalar which is initially filled with a specified value.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @return a scalar with the specified size.
     */
    public <T> Scalar<T> initializedScalar(T value) ;

    /**
     * Factory method to get a scalar with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the scalar with.
     *
     * @param ids
     *        The ids for the scalar.
     *
     * @param d0
     *        The size of the scalar.
     *
     * @return a scalar with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <T,I> IdScalar<T,I> initializedScalar(T value, I[] ids, int d0) ;

    /**
     * Factory method to get a vector that holds {@code }s.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public <T> Vector<T> vector(int d0) ;

    /**
     * Factory method to get a vector with indices accessible by ids.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <T,I> IdVector<T,I> vector(List<I> ids, int d0) ;

    /**
     * Factory method to get a vector with indices accessible by ids.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <T,I> IdVector<T,I> vector(I[] ids, int d0) ;

    /**
     * Factory method to get a vector which is initially filled with a specified value.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1.
     */
    public <T> Vector<T> initializedVector(T value, int d0) ;

    /**
     * Factory method to get a vector with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <T,I> IdVector<T,I> initializedVector(T value, List<I> ids, int d0) ;

    /**
     * Factory method to get a vector with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the vector with.
     *
     * @param ids
     *        The ids for the vector.
     *
     * @param d0
     *        The size of the vector.
     *
     * @return a vector with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} is less than 1, if the length of {@code ids} does not equal {@code d0},
     *                                  or if {@code ids} contains repeated elements.
     */
    public <T,I> IdVector<T,I> initializedVector(T value, I[] ids, int d0) ;

    /**
     * Factory method to get a matrix that holds {@code }s.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public <T> Matrix<T> matrix(int d0, int d1) ;

    /**
     * Factory method to get a matrix with indices accessible by ids.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <T,I> IdMatrix<T,I> matrix(List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix with indices accessible by ids.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <T,I> IdMatrix<T,I> matrix(I[] d0Ids, I[] d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix which is initially filled with a specified value.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1.
     */
    public <T> Matrix<T> initializedMatrix(T value, int d0, int d1) ;

    /**
     * Factory method to get a matrix with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <T,I> IdMatrix<T,I> initializedMatrix(T value,List<I> d0Ids, List<I> d1Ids, int d0, int d1) ;

    /**
     * Factory method to get a matrix with indices accessible by ids and which is initially filled
     * with a specified value.
     *
     * @param <T>
     *        The type held by the tensor.
     *
     * @param <I>
     *        The type of the tensor ids.
     *
     * @param value
     *        The value to fill the matrix with.
     *
     * @param d0Ids
     *        The ids for the matrix.
     *
     * @param d1Ids
     *        The ids for the matrix.
     *
     * @param d0
     *        The size of the first dimension of the matrix.
     *
     * @param d1
     *        The size of the second dimension of the matrix.
     *
     * @return a matrix with the specified size.
     *
     * @throws IllegalArgumentException if {@code d0} or {@code d1} is less than 1, if the length of {@code d0Ids}/{@code d0Ids}
     *                                  does not equal {@code d0}/{@code d1}, or if the ids contain repeated elements.
     */
    public <T,I> IdMatrix<T,I> initializedMatrix(T value, I[] d0Ids, I[] d1Ids, int d0, int d1) ;
}