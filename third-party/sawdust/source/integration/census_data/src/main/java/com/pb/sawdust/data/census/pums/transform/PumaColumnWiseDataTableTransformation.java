package com.pb.sawdust.data.census.pums.transform;

import com.pb.sawdust.data.census.pums.PumaDataDictionary;
import com.pb.sawdust.data.census.pums.PumaDataType;
import com.pb.sawdust.tabledata.DataTable;
import com.pb.sawdust.tabledata.transform.column.ColumnWiseDataTableTransformation;

import java.util.Set;

/**
 * The {@code ColumnWisePumaDataTableTransformation} is a PUMA column-wise data table transformation which wraps another
 * column-wise data table transformation so that it can be applied to PUMA data tables via the {@code PumaDataTableTransformation}
 * interface.
 *
 * @author crf
 *         Started 1/20/12 9:46 AM
 */
public class PumaColumnWiseDataTableTransformation extends ColumnWiseDataTableTransformation implements PumaDataTableTransformation {
    private final ColumnWiseDataTableTransformation transformation;
    private final PumaDataDictionary<?,?> dictionary;
    private final PumaDataType transformationType;

    /**
     * Constructor specifying the transformation, the PUMA data dictionary corresponding to this transformation, and what
     * PUMA data type this transformation applies to.
     *
     * @param transformation
     *        The transformation that this instance will wrap.
     *
     * @param dictionary
     *        The data dictionary for this transformation.
     *
     * @param transformationType
     *        The type of PUMA data this transformation acts on.
     */
    public PumaColumnWiseDataTableTransformation(ColumnWiseDataTableTransformation transformation, PumaDataDictionary<?,?> dictionary, PumaDataType transformationType) {
        this.transformation = transformation;
        this.dictionary = dictionary;
        this.transformationType = transformationType;
    }

    @Override
    public Set<String> getColumnsToTransform() {
        return transformation.getColumnsToTransform();
    }

    @Override
    public DataTable transformColumns(Set<String> column, DataTable table) {
        return transformation.transformColumns(column,table);
    }

    @Override
    public PumaDataDictionary<?,?> getDataDictionary() {
        return dictionary;
    }

    @Override
    public PumaDataType getTransformationType() {
        return transformationType;
    }
}