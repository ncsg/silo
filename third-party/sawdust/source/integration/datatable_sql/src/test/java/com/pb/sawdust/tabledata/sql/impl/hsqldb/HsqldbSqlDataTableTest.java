package com.pb.sawdust.tabledata.sql.impl.hsqldb;

import com.pb.sawdust.tabledata.sql.SqlDataTableTest;
import com.pb.sawdust.tabledata.sql.SqlDataSet;
import com.pb.sawdust.tabledata.sql.impl.SqlImplTestUtil;
import com.pb.sawdust.util.test.TestBase;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author crf <br/>
 *         Started: Dec 2, 2008 6:41:11 PM
 */
public class HsqldbSqlDataTableTest extends SqlDataTableTest {

    public static void main(String ... args) {
        TestBase.main();
        if (SqlImplTestUtil.shouldPerformTestFinishOperations(args))
            HsqldbPackageTests.performTestFinishOperations();
    }

    protected Collection<Class<? extends TestBase>> getAdditionalTestClasses() {
        List<Map<String,Object>> context = new LinkedList<Map<String,Object>>();
        for (HsqldbPackageTests.HsqldbDataSetType dataSetType : HsqldbPackageTests.HsqldbDataSetType.values())
            context.add(buildContext(HsqldbPackageTests.HSQLDB_DATA_SET_TYPE_KEY,dataSetType));
        addClassRunContext(this.getClass(),context);
        return super.getAdditionalTestClasses();
    }

    protected SqlDataSet getSqlDataSet() {
        return HsqldbPackageTests.getDataSet((HsqldbPackageTests.HsqldbDataSetType) getTestData(HsqldbPackageTests.HSQLDB_DATA_SET_TYPE_KEY));
    }
}