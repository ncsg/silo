package com.pb.sawdust.tabledata.basic;

import com.pb.sawdust.tabledata.AbstractDataTable;
import com.pb.sawdust.tabledata.DataRow;
import com.pb.sawdust.tabledata.DataTable;
import com.pb.sawdust.tabledata.TableDataException;
import com.pb.sawdust.tabledata.metadata.DataType;
import com.pb.sawdust.tabledata.metadata.TableSchema;
import com.pb.sawdust.util.array.ArrayUtil;

import java.util.Arrays;


/**
 * The {@code PieceWiseDataTable} class provides a data table which consists of a (non-contiguous) selection of rows from
 * another data table. The selection of rows need not be sequential, nor unique, and the piecewise data table instance
 * will always be a <i>view</i> on the source data table, not a copy of it. That means that changes to the source data
 * table will be reflected through to the piecewise data table, and operations such as adding or removing rows may cause
 * the piecewise data table to change fundamentally (point to different rows) or invalid (point to non-existent rows).
 * <p>
 * The {@code PieceWiseDataTable} is a fixed-size view on the source data table, so any mutating operations on the
 * table (adding/removing rows or columns) will throw an {@code UnsupportedOperationException}.
 *
 * @author crf
 *         Started 9/30/11 8:14 AM
 */
public class PieceWiseDataTable extends FixedSizeDataTable {
    /**
     * Constructor specifying the source table and the rows from which the piecewise table will be built. The order of the
     * rows in the piecewise table will be the same as in the {@code rows} argument passed to this constructor.
     *
     * @param sourceTable
     *        The source data table.
     *
     * @param rows
     *        The rows, in order, from which the piecewise data table will be formed. At least one row must be specified
     *
     * @throws  IllegalArgumentException if any row in {@code rows} is less than zero or greater than or equal to the number
     *                                   of rows in {@code sourceTable}, or if {@code rows.length == 0}.
     */
    public PieceWiseDataTable(DataTable sourceTable, int ... rows) {
        super(getAbTable(sourceTable,rows));
        int max = sourceTable.getRowCount();
        if (rows.length == 0)
            throw new IllegalArgumentException("At least one row must be specified for a piecewise data table");
        for (int i : rows)
            if (i < 0 || i > max)
                throw new IllegalArgumentException(String.format("Row index out of bounds (%d) for source table of length %d",i,max));
    }

    private static DataTable getAbTable(final DataTable table, final int [] tableRows) {
        return new AbstractDataTable() {
            private final int[] rows = ArrayUtil.copyArray(tableRows);

            @Override
            protected <A> boolean addColumnToData(String columnLabel, A columnDataArray, DataType type) {
                throw new UnsupportedOperationException("Table is read-only");
            }

            @Override
            protected boolean addRow(int nextRowIndex, Object... rowData) {
                throw new UnsupportedOperationException("Table is read-only");
            }

            @Override
            protected void deleteColumnFromData(int columnNumber) {
                throw new UnsupportedOperationException("Table is read-only");
            }

            @Override
            protected void deleteRowFromData(int rowNumber) {
                throw new UnsupportedOperationException("Table is read-only");
            }

            @Override
            protected boolean setCellValueInData(int rowNumber, int columnIndex, Object value) {
                return table.setCellValue(rows[rowNumber],columnIndex,value);
            }

            @Override
            public int getRowCount() {
                return rows.length;
            }

            @Override
            public int getColumnCount() {
                return table.getColumnCount();
            }

            @Override
            public TableSchema getSchema() {
                return table.getSchema();
            }

            @Override
            public DataRow getRow(int rowIndex) {
                try {
                    return table.getRow(rows[rowIndex]);
                } catch (IndexOutOfBoundsException e) {
                    throw new TableDataException(TableDataException.ROW_NUMBER_OUT_OF_BOUNDS,rowIndex);
                }
            }
        };
    }


}