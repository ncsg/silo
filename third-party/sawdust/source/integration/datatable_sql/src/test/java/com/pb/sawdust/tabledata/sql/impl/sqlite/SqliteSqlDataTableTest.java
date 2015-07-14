package com.pb.sawdust.tabledata.sql.impl.sqlite;

import com.pb.sawdust.tabledata.sql.SqlDataTableTest;
import com.pb.sawdust.tabledata.sql.SqlDataSet;
import com.pb.sawdust.tabledata.TableDataException;
import com.pb.sawdust.tabledata.sql.impl.SqlImplTestUtil;
import com.pb.sawdust.util.test.TestBase;
import org.junit.Test;
import org.junit.Ignore;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author crf <br/>
 *         Started: Dec 2, 2008 7:21:57 PM
 */
public class SqliteSqlDataTableTest extends SqlDataTableTest {

    public static void main(String ... args) {
        TestBase.main();
        if (SqlImplTestUtil.shouldPerformTestFinishOperations(args))
            SqlImplTestUtil.performTestFinishOperations();
    }

    protected Collection<Class<? extends TestBase>> getAdditionalTestClasses() {
        List<Map<String,Object>> context = new LinkedList<Map<String,Object>>();
        for (SqlitePackageTests.SqliteDataSetType dataSetType : SqlitePackageTests.SqliteDataSetType.values())
            context.add(buildContext(SqlitePackageTests.SQLITE_DATA_SET_TYPE_KEY,dataSetType));
        addClassRunContext(this.getClass(),context);
        return super.getAdditionalTestClasses();
    }

    protected SqlDataSet getSqlDataSet() {
        return SqlitePackageTests.getDataSet((SqlitePackageTests.SqliteDataSetType) getTestData(SqlitePackageTests.SQLITE_DATA_SET_TYPE_KEY));
    }

    @Test(expected=TableDataException.class)
    public void testDeleteColumnReturn() {
        super.testDeleteColumnReturn();
    }

    @Test(expected=TableDataException.class)
    public void testDeleteColumnCount() {
        super.testDeleteColumnCount();
    }

    @Test(expected=TableDataException.class)
    public void testDeleteColumnLabel() {
        super.testDeleteColumnLabel();
    }

    @Test(expected=TableDataException.class)
    public void testDeleteColumnSchemaCount() {
        super.testDeleteColumnSchemaCount();
    }

    @Test(expected=TableDataException.class)
    public void testDeleteColumnSchemaLabel() {
        super.testDeleteColumnSchemaLabel();
    }

    @Ignore
    @Test
    public void testDeleteColumnFailureTooLow() {
        //columns can't be deleted using sqlite
    }

    @Ignore
    @Test
    public void testDeleteColumnFailureTooHigh() {
        //columns can't be deleted using sqlite
    }

    @Test(expected=TableDataException.class)
    public void testDeleteColumnLabelReturn() {
        super.testDeleteColumnReturn();
    }

    @Test(expected=TableDataException.class)
    public void testDeleteColumnLabelCount() {
        super.testDeleteColumnLabelCount();
    }

    @Test(expected=TableDataException.class)
    public void testDeleteColumnLabelLabel() {
        super.testDeleteColumnLabelLabel();
    }

    @Test(expected=TableDataException.class)
    public void testDeleteColumnLabelSchemaCount() {
        super.testDeleteColumnLabelSchemaCount();
    }

    @Test(expected=TableDataException.class)
    public void testDeleteColumnLabelSchemaLabel() {
        super.testDeleteColumnLabelSchemaLabel();
    }

    @Ignore
    @Test
    public void testDeleteColumnLabelFailure() {
        //columns can't be deleted using sqlite
    }
}

