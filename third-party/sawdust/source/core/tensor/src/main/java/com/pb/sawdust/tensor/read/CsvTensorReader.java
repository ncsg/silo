package com.pb.sawdust.tensor.read;

import com.pb.sawdust.io.DelimitedDataReader;
import com.pb.sawdust.io.IterableReader;
import com.pb.sawdust.tensor.ArrayTensor;
import com.pb.sawdust.tensor.StandardTensorMetadataKey;
import com.pb.sawdust.tensor.Tensor;
import com.pb.sawdust.tensor.TensorUtil;
import com.pb.sawdust.tensor.decorators.primitive.*;
import com.pb.sawdust.tensor.factory.TensorFactory;
import com.pb.sawdust.tensor.index.BaseIndex;
import com.pb.sawdust.tensor.index.Index;
import com.pb.sawdust.tensor.index.IndexFactory;
import com.pb.sawdust.tensor.index.StandardIndex;
import com.pb.sawdust.tensor.read.id.IdReader;
import com.pb.sawdust.tensor.read.id.IdTransfers;
import com.pb.sawdust.tensor.read.id.UniformIdReader;
import com.pb.sawdust.util.JavaType;

import java.io.File;
import java.util.*;

import static com.pb.sawdust.util.Range.range;

/**
 * The {@code CsvTensorReader} is used to read tensors from comma-separated value (csv) files. This class can read one or
 * more tensors with any number of dimensions. The only restriction is that all tensors presented in the csv file have the
 * same shape/dimensionality. The format this class reads is:
 * <pre><code>
 *     id0,id1,...,idn,t0,t1,...,tn
 * </code></pre>
 * where <code>idX</code> is the index id for (0-based) dimension <code>X</code> and <code>tX</code> is the value for (0-based)
 * tensor <code>X</code>.
 * <p>
 * If the csv file read by this class has a header, then the names of the id columns are added as metadata to the index
 * ({@link StandardTensorMetadataKey#DIMENSION_NAME}), and the names of the tensor(s) are added as metadata to the tensor(s)
 * ({@link StandardTensorMetadataKey#NAME}). If a subset of tensors is being read in (see {@link #setTensorsToRead(java.util.List)})
 * then the header will be used to locate which data columns are to be read in. The csv file does not need to have a header;
 * if it lacks one, then default tensor names (see {@link #formDefaultTensorName(int)}) will be used.
 * <p>
 * If only one tensor is to be read (that is, via the {@link TensorReader} interface), then the tensor to read can be specified
 * using the {@link #setCurrentTensor(String)} method. If multiple tensors are to be read (as a tensor group), then the
 * {@link #setTensorsToRead(java.util.List)} method is used to specify a subset of tensors to read into the tensor group
 * (otherwise, the entire set of tensors in the file are read into the group).
 * <p>
 * This reader will only allow one index to be specified for all of the tensors it reads in; however, there is some flexibility
 * in the specification of this index:
 * <ol>
 *     <li>
 *         The index is specified when the reader is constructed. It is assumed the index will be consistent with data in
 *         the file.
 *     </li>
 *     <li>
 *         An {@link IdReader} is specified when the reader is constructed. The id reader specifieds how index ids are parsed
 *         from the csv file, and the index will be built when the file is read.
 *     </li>
 *     <li>
 *         Neither an index nor an {@code IdReader} is specified. This indicates that a standard index (0-based, sequential
 *         integer) should be used/assumed. This index will be built when the file is read.
 *     </li>
 * </ol>
 * Once an index is specified or built, it will be used for the entire lifetime of the reader. To use or specify a different
 * index, a new reader instance needs to be constructed.
 * <p>
 * This reader can read tensors stored in a "condensed" format. Tensors written in a condensed format skip the writing step
 * for all entries that are equal to a specified value. The condensed format values default to the default Java values
 * for primitives (or {@code null} for objects), but can be respecified if desired. If a reader is set to read in condensed
 * mode, and no index is specified, then an attempt at building an index based on the present id values will be made, but
 * generally this functionality should not be relied upon, as it is difficult to confirm that the full index has been built
 * correctly. That is, if a csv tensor file is in condensed mode, then an index or the dimensions of the tensor(s) should
 * be specified before reading in the data.
 *
 * @author crf <br/>
 *         Started 1/25/11 9:13 PM
 */
public class CsvTensorReader<T,I> implements TensorReader<T,I>,TensorGroupReader<T,I> {
    //csv format: index0,index1,...,indexN,tensorA,tensorB,...
    private static final String DEFAULT_TENSOR_NAME_PREFIX = "tensor";

    /**
     * The index name which will be used for the index built by this class when reading a file.
     */
    public static final String DEFAULT_INDEX_NAME = "index";

    /**
     * Get the default tensor name for a given numbered tensor. The default is to concatenate {@link #DEFAULT_TENSOR_NAME_PREFIX}
     * and the tensor number.
     * 
     * @param tensorNumber
     *        The tensor number.
     *        
     * @return the default tensor name for {@code tensorNumber}.
     */
    public static String formDefaultTensorName(int tensorNumber) {
        return DEFAULT_TENSOR_NAME_PREFIX + tensorNumber;
    }

    /*
    states:
    index,idreader specified: all reads go through it

    dimensions,idreader specified: index created/built on the fly
                                   ids go with index # in order they are encountered

    dimensions specified: index prebuilt as standard
     */
    
    private static enum ReaderMode {
        STANDARD,
        SPECIFIED_INDEX,
        BUILD_INDEX
    }

    private final String csvFile;
    private Index<I> index;
    private final IdReader<String,I> idReader;
    private final JavaType type;
    private final int[] dimensions;
    private List<String> tensors = null;
    private List<String> tensorsToRead = null;
    private String currentTensor = null;
    private final ReaderMode mode;
    private boolean condensedMode;
    private boolean header = true;
    private Object defaultValue = null;
    

    @SuppressWarnings("unchecked") //cannot ensure I will be Integer for standard mode, but if not it is a user definition error, so suppressing
    CsvTensorReader(String csvFile, Index<I> index, IdReader<String, I> idReader, JavaType type, int[] dimensions) {
        mode = idReader == null ? ReaderMode.STANDARD : (index == null ? ReaderMode.BUILD_INDEX : ReaderMode.SPECIFIED_INDEX);
        this.csvFile = csvFile;
        this.idReader = mode == ReaderMode.STANDARD ? (IdReader<String,I>) new UniformIdReader<String,Integer>(IdTransfers.STRING_TO_INT_TRANSFER) : idReader;
        this.type = type;
        this.dimensions = dimensions;
        this.index = mode == ReaderMode.STANDARD && index == null ? (Index<I>)  new StandardIndex(dimensions) : index;
    }

    /**
     * Constructor specifying the csv file, the type of the tensor, the index to use when building the tensor, and the id
     * reader to use to read the ids.
     *
     * @param csvFile
     *        The csv file.
     *
     * @param type
     *        The type the tensor will hold.
     *
     * @param index
     *        The index to use when building the tensor.
     *
     * @param idReader
     *        The reader to use to read the tensor ids.
     */
    public CsvTensorReader(String csvFile, JavaType type, Index<I> index, IdReader<String, I> idReader) {
        this(csvFile,index,idReader,type,index.getDimensions());
    }

    /**
     * Constructor specifying the csv file, the type of the tensor, the dimensions of the tensor, and the id
     * reader to use to read the ids. A default index built from the specified dimensions will be used to build the tensor.
     *
     * @param csvFile
     *        The csv file.
     *
     * @param type
     *        The type the tensor will hold.
     *
     * @param dimensions
     *        The dimensions of the tensor.
     *
     * @param idReader
     *        The reader to use to read the tensor ids.
     */
    public CsvTensorReader(String csvFile, JavaType type, int[] dimensions, IdReader<String, I> idReader) {
        this(csvFile,null,idReader,type,dimensions);
    }

    /**
     * Constructor specifying the csv file, the type of the tensor, and the dimensions of the tensor. A default index built
     * from the specified dimensions will be used to build the tensor, and a standard index reader will be used to read
     * the tensor indices.
     *
     * @param csvFile
     *        The csv file.
     *
     * @param type
     *        The type the tensor will hold.
     *
     * @param dimensions
     *        The dimensions of the tensor.
     */
    public CsvTensorReader(String csvFile, JavaType type, int[] dimensions) {
        this(csvFile,null,null,type,dimensions);
    }

    /**
     * Set the names of the tensors held in this file. If the file has a header, this is used as a replacement for the tensor
     * names specified there. If the file has no header, then this method sets the names of the tensors held in the file.
     *
     * @param tensors
     *        The tensor names to use.
     */
    public void setTensorNames(List<String> tensors) {
        this.tensors = tensors;
    }

    /**
     * Set the tensors to read. This method can be used to specify a subset of tensors to read from the csv file. This method
     * does not check to verify that the tensor names are valid. However, if, when reading the tensors, they are invalid
     * (either because they do not match default or header names, or the names specified by the {@link #setTensorNames(java.util.List)})
     * then an exception will be thrown.
     *
     * @param tensors
     *        The tensors to read.
     */
    public void setTensorsToRead(List<String> tensors) {
        tensorsToRead = new LinkedList<String>(tensors);
    }

    /**
     * Set whether this reader should reader a header from the file or not. If not, then it is assumed that the file
     * has no header.
     *
     * @param header
     *        If (@code true} then this reader will read a header, if {@code false} it will not.
     */
    public void setHeader(boolean header) {
        this.header = header;
    }

    /**
     * Set whether this reader should assume the file was written in condensed mode or not.
     *
     * @param condensedMode
     *        If {@code true}, then this reader will assume the file was written in condensed mode, else if {@code false},
     *        this reader will assume the full tensor(s) were written to the file.
     */
    public void setCondensedMode(boolean condensedMode) {
        this.condensedMode = condensedMode;
    }

    /**
     * Set the default value for use when reading in tensors in condensed mode. If condensed mode is turned off, then this
     * value is ignored. To reset to the default, set the value equal to {@code null}.
     * 
     * @param value
     *        The default value to use for condensed tensors.
     */
    public void setDefaultValue(Object value) {
        defaultValue = value;
    }
    
    private int[] updateIndex(List<List<I>> ids, I[] currentIds) {
        int[] indices = new int[currentIds.length];
        int counter = 0;
        for (List<I> dimIds : ids) {
            I currentId = currentIds[counter];
            int index = dimIds.indexOf(currentId);
            if (index < 0) {
                dimIds.add(currentId);
                index = dimIds.size()-1;
            }                                     
            indices[counter++] = index;
        }
        return indices;
    }

    private List<String> getTensors() {
        if (tensors == null) {
            IterableReader<String[]> it = null;
            try {
                it = new DelimitedDataReader(',').getLineIterator(new File(csvFile));
                String[] h = it.iterator().next();
                if (header) {
                    tensors = Arrays.asList(Arrays.copyOfRange(h,dimensions.length,h.length));
                } else {
                    tensors = new LinkedList<String>();
                    for (int i : range(h.length-dimensions.length))
                        tensors.add(DEFAULT_TENSOR_NAME_PREFIX + i);
                }
            } finally {
                if (it != null)
                    it.close();
            }
        }
        if (currentTensor == null)
            currentTensor = tensors.get(0);
        return tensors;
    }

    private List<String> getTensorsToRead() {
        getTensors();
        if (tensorsToRead == null) {
            tensorsToRead = new LinkedList<String>();
            for (String tensor : tensors)
                tensorsToRead.add(tensor);
        }
        checkTensorsToRead();
        return tensorsToRead;
    }

    private void checkTensorsToRead() {
        for (String tensor : tensorsToRead)
            if (!tensors.contains(tensor))
                throw new IllegalStateException("Tensor to read not found in tensor list: " + tensor + " (list: " + tensors + ")");
    }

    /**
     * Set the current tensor, for use when reading one tensor at a time.
     *
     * @param tensor
     *        The tensor to use as the current tensor.
     *
     * @throws IllegalArgumentException if {@code tensor} if not found in the tensor names.
     */
    public void setCurrentTensor(String tensor) {
        if (!getTensors().contains(tensor))
            throw new IllegalArgumentException("Tensor not found: " + tensor);
        currentTensor = tensor;
    }

    @SuppressWarnings("unchecked") //types checked internally, so ok
    private Map<String,Tensor<T>> getShellTensorMap(TensorFactory defaultFactory) {
        Map<String,Tensor<T>> tensorMap = new HashMap<String,Tensor<T>>();
        for (String t : getTensorsToRead()) {
            Tensor<T> tensor;
            switch (type) {
                case BYTE : tensor = (Tensor<T>) defaultFactory.byteTensor(dimensions); break;
                case SHORT : tensor = (Tensor<T>) defaultFactory.shortTensor(dimensions); break;
                case INT : tensor = (Tensor<T>) defaultFactory.intTensor(dimensions); break;
                case LONG : tensor = (Tensor<T>) defaultFactory.longTensor(dimensions); break;
                case FLOAT : tensor = (Tensor<T>) defaultFactory.floatTensor(dimensions); break;
                case DOUBLE : tensor = (Tensor<T>) defaultFactory.doubleTensor(dimensions); break;
                case BOOLEAN : tensor = (Tensor<T>) defaultFactory.booleanTensor(dimensions); break;
                case CHAR : tensor = (Tensor<T>) defaultFactory.charTensor(dimensions); break;
                default : tensor = defaultFactory.<T>tensor(dimensions); break;
            }
            setCurrentTensor(t);
            Map<String,Object> md = getTensorMetadata();
            for (String key : md.keySet())
                tensor.setMetadataValue(key,md.get(key));
            tensorMap.put(t,tensor);
        }
        return tensorMap;
    }

    @Override
    public Map<String,Tensor<T>> getTensorMap(TensorFactory defaultFactory) {
        return getShellTensorMap(defaultFactory);
    }

    @Override
    public Map<String,Index<I>> getIndexMap(IndexFactory defaultFactory) {
        getIndex();
        Map<String, Index<I>> indexMap = new HashMap<String, Index<I>>();
        indexMap.put(TensorGroupReader.DEFAULT_INDEX_NAME,index);
        return  indexMap;
    }

    @Override
    public Map<String,Object> getTensorGroupMetadata() {
        Map<String,Object> metadata = new HashMap<String,Object>();
        metadata.put(StandardTensorMetadataKey.SOURCE.getKey(),csvFile);
        return metadata;
    }

    @Override
    public JavaType getType() {
        return type;
    }

    @Override
    public int[] getDimensions() {
        return dimensions;
    }

    @Override
    public List<List<I>> getIds() {
        getIndex();
        return index.getIndexIds();
    }

    private Index<I> getIndex() {
        if (index == null && mode == ReaderMode.BUILD_INDEX)
            buildIndex();
        return index;
    }

    private void buildIndex() {
        readInto(new HashMap<String, Tensor<T>>()); //should trigger a read only for index
    }

    @Override
    public Map<String, Object> getTensorMetadata() {
        Map<String,Object> metadata = new HashMap<String,Object>();
        metadata.put(StandardTensorMetadataKey.SOURCE.getKey(),csvFile);
        metadata.put(StandardTensorMetadataKey.NAME.getKey(),currentTensor);
        return metadata;
    }  
    
    public Tensor<T> fillTensor(Tensor<T> tensor) {
        getTensors(); //to set current tensor, if it hasn't been yet
        Map<String,Tensor<T>> m = new HashMap<String,Tensor<T>>();
        m.put(currentTensor,tensor);
        readInto(m);
        return m.get(currentTensor);
    }

    public Map<String,Tensor<T>> fillTensorGroup(Map<String,Tensor<T>> tensorGroup) {
        readInto(tensorGroup);
        return tensorGroup;
    }

    //used to get extension to improve performance
    Iterator<int[]> getAbacus(int[] dimensions) {
        return null;
    }
    
    @SuppressWarnings("unchecked") //doing some crazy generic stuff here that is internally ok
    private void readInto(Map<String,Tensor<T>> tensorMapping) {
        for (Tensor<T> tensor : tensorMapping.values()) {
            if (!Arrays.equals(getDimensions(),tensor.getDimensions()))
                throw new IllegalArgumentException(String.format("Tensor dimensions (%s) do not match reader dimensions {%s).",Arrays.toString(tensor.getDimensions()),Arrays.toString(getDimensions())));
            if (getType() != tensor.getType())
                throw new IllegalArgumentException(String.format("Tensor type (%s) and reader type (%s) do not match.",tensor.getType(),getType()));
        }
                
        getTensors();
        int[] columnIndex = new int[tensorMapping.size()];
        List<?> gTensors = new LinkedList<Tensor<T>>();
        List<Tensor<T>> objectTensors = (List<Tensor<T>>) gTensors;
        int offset = getDimensions().length;
        int counter = 0;
        for (String tensor : tensorMapping.keySet()) {
            columnIndex[counter++] = tensors.indexOf(tensor)+offset; //so we skip past index columns
            objectTensors.add(tensorMapping.get(tensor));
        }
        
        if (condensedMode && defaultValue != null) {
            //fill tensors with default
            for (Tensor<T> tensor : tensorMapping.values()) {
                switch (type) {
                    case BYTE : TensorUtil.fill((ByteTensor) tensor,(Byte) defaultValue); break;
                    case SHORT : TensorUtil.fill((ShortTensor) tensor,(Short) defaultValue); break;
                    case INT : TensorUtil.fill((IntTensor) tensor,(Integer) defaultValue); break;
                    case LONG : TensorUtil.fill((LongTensor) tensor,(Long) defaultValue); break;
                    case FLOAT : TensorUtil.fill((FloatTensor) tensor,(Float) defaultValue); break;
                    case DOUBLE : TensorUtil.fill((DoubleTensor) tensor,(Double) defaultValue); break;
                    case CHAR : TensorUtil.fill((CharTensor) tensor,(Character) defaultValue); break;
                    case BOOLEAN : TensorUtil.fill((BooleanTensor) tensor,(Boolean) defaultValue); break;
                    default : TensorUtil.fill(tensor,(T) defaultValue); break;
                }
            }
        }
                
        //setup reader, read header if it exists, and add index dimension name metadata
        IterableReader<String[]> reader = null;
        try {
            reader = new DelimitedDataReader(',').getLineIterator(new File(csvFile));
            Iterator<String[]> it = reader.iterator();
            Map<String,Object> indexMetadata = new HashMap<String,Object>();
            indexMetadata.put(StandardTensorMetadataKey.NAME.getKey(),DEFAULT_INDEX_NAME);
            if (header) {
                String[] headerData = it.next();
                for (int i : range(dimensions.length))
                    indexMetadata.put(StandardTensorMetadataKey.DIMENSION_NAME.getDetokenizedKey(i),headerData[i]);
            }

            //setup id reading stuff, if needed
            String[] fakeInput = new String[dimensions.length];
            Arrays.fill(fakeInput,"");
            @SuppressWarnings("unchecked") //cannot guarantee I type, but used internally so it doesn't matter
            I[] ids = (I[]) (mode == ReaderMode.STANDARD ? new Integer[dimensions.length] : idReader.getIdSink(fakeInput));
            List<List<I>> newIndexIds = null;
            int[] cutoffs = null;
            int cutoffsNeeded = dimensions.length;
            if (mode == ReaderMode.BUILD_INDEX) {
                newIndexIds = new LinkedList<List<I>>();
                for (int i : range(dimensions.length))
                    newIndexIds.add(new LinkedList<I>());
                if (!condensedMode) {
                    cutoffs = new int[dimensions.length];
                    cutoffs[cutoffs.length-1] = 1;
                    for (int i : range(cutoffs.length-1,0))
                        cutoffs[i-1] = dimensions[i]*cutoffs[i];
                }
            }

            //generally used variables
            final boolean buildIndex = index == null && mode == ReaderMode.BUILD_INDEX;
            final boolean justBuildIndex = buildIndex && tensorMapping.size() == 0;
            //final Abacus abacus = new Abacus(dimensions);
            final Iterator<int[]> abacus = getAbacus(dimensions);
            final boolean useAbacus = abacus != null;
            int[] ind;
            //go ahead and read the data
            
            //data mirrors
            List<ByteTensor> byteTensors = null;
            List<ShortTensor> shortTensors = null;
            List<IntTensor> intTensors = null;
            List<LongTensor> longTensors = null;
            List<FloatTensor> floatTensors = null;
            List<DoubleTensor> doubleTensors = null;
            List<BooleanTensor> booleanTensors = null;
            List<CharTensor> charTensors = null;
            switch (type) {
                case BYTE : byteTensors = (List<ByteTensor>) gTensors; break;
                case SHORT : shortTensors = (List<ShortTensor>) gTensors; break;
                case INT : intTensors = (List<IntTensor>) gTensors; break;
                case LONG : longTensors = (List<LongTensor>) gTensors; break;
                case FLOAT : floatTensors = (List<FloatTensor>) gTensors; break;
                case DOUBLE : doubleTensors = (List<DoubleTensor>) gTensors; break;
                case BOOLEAN : booleanTensors = (List<BooleanTensor>) gTensors; break;
                case CHAR : charTensors = (List<CharTensor>) gTensors; break;
            }

            int overallCounter = 0;
            while (it.hasNext()) {
                String[] data = it.next();
                if (useAbacus){
                    ind = abacus.next();
                    if (buildIndex && cutoffsNeeded > 0) {
                        for (int i = 0; i < cutoffsNeeded; i++) {
                            if (overallCounter % cutoffs[i] == 0) {
                                I id = idReader.getId(data[i],i);
                                newIndexIds.get(i).add(id);
                                if (newIndexIds.get(i).size() == dimensions[i])
                                    cutoffsNeeded--;
                            }
                        }
                    }
                } else {
                    idReader.getIds(data,0,ids);
                    ind = buildIndex ? updateIndex(newIndexIds,ids) : index.getIndices(ids);
                }
                overallCounter++;
                if (justBuildIndex)
                    continue;
                counter = 0;
                //todo: performance penalty isn't too high doing switching in while loop, but might want to split this out...
                switch (type) {
                    case BYTE : for (ByteTensor t : byteTensors)
                                    t.setCell(Byte.parseByte(data[columnIndex[counter++]]),ind);
                                break;
                    case SHORT : for (ShortTensor t : shortTensors)
                                    t.setCell(Short.parseShort(data[columnIndex[counter++]]),ind);
                                break;
                    case INT : for (IntTensor t : intTensors)
                                    t.setCell(Integer.parseInt(data[columnIndex[counter++]]),ind);
                                break;
                    case LONG : for (LongTensor t : longTensors)
                                    t.setCell(Long.parseLong(data[columnIndex[counter++]]),ind);
                                break;
                    case FLOAT : for (FloatTensor t : floatTensors)
                                    t.setCell(Float.parseFloat(data[columnIndex[counter++]]),ind);
                                break;
                    case DOUBLE : for (DoubleTensor t : doubleTensors)
                                    t.setCell(Double.parseDouble(data[columnIndex[counter++]]),ind);
                                break;
                    case BOOLEAN : for (BooleanTensor t : booleanTensors)
                                    t.setCell(Boolean.parseBoolean(data[columnIndex[counter++]]),ind);
                                break;
                    case CHAR : for (CharTensor t : charTensors)
                                    t.setCell(data[columnIndex[counter++]].charAt(0),ind); //makes simplifying assumption that only wnat (will have?) 1st char
                                break;
                    case OBJECT : for (Tensor<T> t : objectTensors)
                                    t.setValue((T) data[columnIndex[counter++]],ind); //assumes strings
                                break;
                }
            }

            if (buildIndex)
                index = new BaseIndex<I>(newIndexIds);
            if (buildIndex || mode == CsvTensorReader.ReaderMode.SPECIFIED_INDEX)
                for (String t : tensors)
                    tensorMapping.put(t,tensorMapping.get(t).getReferenceTensor(index));
            for (String key : indexMetadata.keySet())
                if (!index.containsMetadataKey(key))
                    index.setMetadataValue(key,indexMetadata.get(key));
        } finally {
            if (reader != null)
                reader.close();
        }
    }
    
    
    
    



    public static void main(String ... args) {
        String file = "D:\\transfers\\az_statewide\\skims.csv";
        String fileo = "D:\\transfers\\az_statewide\\skims.zpt";
        String fileo2 = "D:\\transfers\\az_statewide\\junk.zpt";
        String zmxFile = "D:\\transfers\\az_statewide\\pkautodist.zmx";

        //CsvTensorReader<Double,Integer> r =  new CsvTensorReader<Double,Integer>(file,JavaType.DOUBLE,new int[] {6090,6090},new UniformIdReader<String,Integer>(IdTransfers.STRING_TO_INT_TRANSFER));
        CsvTensorReader<Double,Integer> r =  new CsvTensorReader<Double,Integer>(file,JavaType.DOUBLE,new int[] {6090,6090},new UniformIdReader<String,Integer>(IdTransfers.STRING_TO_INT_TRANSFER));
        r.setTensorNames(Arrays.asList("time","dist"));
        r.setCurrentTensor("time");
        DoubleTensor t = (DoubleTensor) ArrayTensor.getFactory().tensor(r);
        System.out.println(TensorUtil.toString(t));

//        ZipTensorWriter w = new ZipTensorWriter(fileo,true);
//        w.writeTensor(t);
//        w.writeZipTensor();
//
//        ZipTensorReader r = new ZipTensorReader(fileo);
//        DoubleTensor t = (DoubleTensor) ArrayTensor.getFactory().tensor(r);
//        System.out.println(TensorUtil.toString(t));
//
//
//        FloatTensor dd = ArrayTensor.getFactory().floatTensor(3000,3000);
//        ZipTensorWriter w = new ZipTensorWriter(fileo2,true);
//        w.writeTensor(dd);
//        w.writeZipTensor();


//
//        long t = System.currentTimeMillis();
//        ZipTensorReader<Double,?> r = new ZipTensorReader<Double,Object>(fileo);
//        //FloatTensor dd = (FloatTensor) ArrayTensor.getFactory().tensor(r);
//        DoubleTensor dd = (DoubleTensor) ArrayTensor.getFactory().tensor(r);
//        System.out.println(TensorUtil.toString(dd));
//
//
////        ZipMatrixReader r = ZipMatrixReader.getFloatZipMatrixReader(zmxFile);
////        FloatTensor dd = (FloatTensor) ArrayTensor.getFactory().tensor(r);
//        System.out.println(System.currentTimeMillis()-t);
//        System.out.println(Arrays.toString(dd.getDimensions()));
//        System.out.println(TensorUtil.toString(dd));



    }
}