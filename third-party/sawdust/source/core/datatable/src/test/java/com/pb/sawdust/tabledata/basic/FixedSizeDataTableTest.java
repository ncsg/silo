package com.pb.sawdust.tabledata.basic;

import com.pb.sawdust.tabledata.DataTableTest;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The {@code FixedSizeDataTableTest} ...
 *
 * @author crf
 *         Started 9/30/11 12:07 PM
 */
public abstract class FixedSizeDataTableTest extends DataTableTest {

    @Test @Ignore //adds a row
    public void testSetPrimaryKeyFailureNonUnique() {}

    @Test(expected=UnsupportedOperationException.class)
    public void testAddRow() {
        super.testAddRow();
    }

    @Test @Ignore
    public void testAddRowReturn() {}

    @Test @Ignore
    public void testAddRowCount() {}

    @Test(expected=UnsupportedOperationException.class)
    public void testAddDataRow() {
        super.testAddDataRow();
    }

    @Test @Ignore
    public void testAddDataRowCount() {}

    @Test(expected=UnsupportedOperationException.class)
    public void testAddDataByRows() {
        super.testAddDataByRows();
    }

    @Test @Ignore
    public void testAddDataByRowsCount() {}

    @Test(expected=UnsupportedOperationException.class)
    public void testAddColumn() {
        super.testAddColumn();
    }

    @Test @Ignore
    public void testAddColumnReturn() {}

    @Test @Ignore
    public void testAddColumnCount() {}

    @Test @Ignore
    public void testAddColumnLabel() {}

    @Test @Ignore
    public void testAddColumnFailure() {}

    @Test @Ignore
    public void testAddColumnWrongType() {}

    @Test(expected=UnsupportedOperationException.class)
    public void testAddDataByColumns() {
        super.testAddDataByColumns();
    }

    @Test @Ignore
    public void testAddDataByColumnsCount() {}

    @Test(expected=UnsupportedOperationException.class)
    public void testAddDataByColumnsPrimitive() {
        super.testAddDataByColumnsPrimitive();
    }

    @Test @Ignore
    public void testAddDataByColumnsNotArray() {}

    @Test @Ignore
    public void testAddDataByColumnsRaggedArray() {}

    @Test @Ignore
    public void testAddDataByColumnsWrongColumnCount() {}

    @Test @Ignore
    public void testAddDataByColumnsWrongColumnType() {}

    @Test @Ignore
    public void testAddDataRowsCount() {}

    @Test(expected=UnsupportedOperationException.class)
    public void testDeleteRowReturn() {
        super.testDeleteRowReturn();
    }

    @Test @Ignore
    public void testDeleteRowCount() {}

    @Test @Ignore
    public void testDeleteRowFailureTooLow() {}

    @Test @Ignore
    public void testDeleteRowFailureTooHigh() {}

    @Test(expected=UnsupportedOperationException.class)
    public void testDeleteRowByKeyReturn() {
        super.testDeleteRowByKeyReturn();
    }

    @Test @Ignore
    public void testDeleteRowByKeyCount() {}

    @Test @Ignore
    public void testDeleteRowByKeyFailureTooLow() {}

    @Test(expected=UnsupportedOperationException.class)
    public void testDeleteColumnReturn() {
        super.testDeleteColumnReturn();
    }

    @Test @Ignore
    public void testDeleteColumnCount() {}

    @Test @Ignore
    public void testDeleteColumnLabel() {}

    @Test @Ignore
    public void testDeleteColumnSchemaCount() {}

    @Test @Ignore
    public void testDeleteColumnSchemaLabel() {}

    @Test @Ignore
    public void testDeleteColumnFailureTooLow() {}

    @Test @Ignore
    public void testDeleteColumnFailureTooHigh() {}

    @Test(expected=UnsupportedOperationException.class)
    public void testDeleteColumnLabelReturn() {
        super.testDeleteColumnLabel();
    }

    @Test @Ignore
    public void testDeleteColumnLabelCount() {}

    @Test @Ignore
    public void testDeleteColumnLabelLabel() {}

    @Test @Ignore
    public void testDeleteColumnLabelSchemaCount() {}

    @Test @Ignore
    public void testDeleteColumnLabelSchemaLabel() {}

    @Test @Ignore
    public void testDeleteColumnLabelFailure() {}
}