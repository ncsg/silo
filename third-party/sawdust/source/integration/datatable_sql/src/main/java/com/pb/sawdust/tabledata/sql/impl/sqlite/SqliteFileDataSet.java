package com.pb.sawdust.tabledata.sql.impl.sqlite;

import com.pb.sawdust.tabledata.sql.impl.JdbcFileSqlDataSet;
import com.pb.sawdust.tabledata.sql.SqlTableDataException;
import com.pb.sawdust.tabledata.sql.SqlDataSet;
import com.pb.sawdust.tabledata.TableDataException;
import com.pb.sawdust.tabledata.metadata.DataType;
import com.pb.sawdust.util.sql.IsolatedResultSet;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

/**
 * The {@code SqliteFileDataSet} class provides a {@code SqlDataSet} implementation which connects to a Sqlite file-based
 * database.
 *
 * @see com.pb.sawdust.tabledata.sql.impl.sqlite.SqliteDataSet
 * 
 * @author crf <br/>
 *         Started: Dec 2, 2008 8:26:58 AM
 */
public class SqliteFileDataSet extends SqliteDataSet {
    private final String databaseFilePath;

    /**
     * Constructor specifying the path to the Sqlite database file. If the specified database file does not exist, a new
     * one will be created.
     *
     * @param databaseFilePath
     *        The path to the Sqlite database file.
     */
    public SqliteFileDataSet(String databaseFilePath) {
        super();
        this.databaseFilePath = cleanFilePath(databaseFilePath);
        setResultSetType(ResultSet.TYPE_FORWARD_ONLY);
    }

    private String cleanFilePath(String filePath) {
        return filePath.replace("\\","/");
    }

    //sqlite jdbc has problems with reusing connections after statements are closed
    // so have to create a new one each time
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + databaseFilePath);
        } catch (SQLException e) {
            throw new SqlTableDataException(e);
        }
    }
}