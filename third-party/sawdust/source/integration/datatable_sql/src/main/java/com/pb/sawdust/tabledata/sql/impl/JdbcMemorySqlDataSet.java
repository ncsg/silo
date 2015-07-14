package com.pb.sawdust.tabledata.sql.impl;

/**
 * The {@code JdbcMemorySqlDataSet} provides a skeletal framework for {@code SqlDataSet} implementations using an
 * in-memory (embedded) database.
 *
 * @author crf <br/>
 *         Started: Dec 1, 2008 10:24:28 AM
 */
public abstract class JdbcMemorySqlDataSet extends JdbcSqlDataSet {
    private final String databaseName;

    /**
     * Get the URL used to connect to an in-memory database with a specified name. Though not enforced, it is assumed
     * that different data set instances within a single JVM connecting to a database with the same name will connect
     * to the same database.
     *
     * @param databaseName
     *        The name of the database to connect to.
     *
     * @return the appropriate connection URL to connect to the {@code databaseName} database.
     */
    protected abstract String formConnectionUrl(String databaseName);

    /**
     * Constructor specifying the database name and maximum number of simultaneous connections allowed to the database
     * from this data set. Because this class connects to embedded databases, if an in-memory database named
     * {@code databaseName} does not exist, then one should be created. As for the connection limit, once the maximum
     * number of connections have been made, then requests for more connections will require that previously made
     * connections be recycled; if no previously opened connections are available, then an exception may be thrown, or
     * a deadlock may result.
     *
     * @param databaseName
     *        The name of the database to connect to.
     *
     * @param connectionLimit
     *        The maximum number of simultaneous connections that can be made through this data set.
     */
    public JdbcMemorySqlDataSet(String databaseName, int connectionLimit) {
        super(connectionLimit);
        this.databaseName = databaseName;
    }     

    protected String formConnectionUrl() {
        return formConnectionUrl(databaseName);
    }

}