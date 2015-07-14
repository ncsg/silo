package com.pb.sawdust.tabledata.read;

/**
 * The {@code CsvTableReader} is a delimited data table reader used with comma-separated value (CSV) files.
 * 
 * @author crf <br/>
 *         Started: Nov 3, 2008 6:42:31 AM
 */
public class CsvTableReader extends DelimitedTextTableReader {

    /**
     * Constructor specifying the file and table name for the csv data.
     *
     * @param filePath
     *        The path to the csv data file.
     *
     * @param tableName
     *        The name to use for the table.
     *
     * @param multiline
     *        Boolean specifying if {@code DelimitedDataReader} reader should allow multiline data entries or not.
     *
     * @throws com.pb.sawdust.util.exceptions.RuntimeIOException if {@code fileName} was not found.
     */
    public CsvTableReader(String filePath, String tableName, boolean multiline) {
        super(filePath, tableName,',',multiline);
    }

    /**
     * Constructor specifying the file and table name for the csv data. Multiline data entries will not be allowed.
     *
     * @param filePath
     *        The path to the csv data file.
     *
     * @param tableName
     *        The name to use for the table.
     *
     * @throws com.pb.sawdust.util.exceptions.RuntimeIOException if {@code fileName} was not found.
     */
    public CsvTableReader(String filePath, String tableName) {
        super(filePath,tableName,',');
    }

    /**
     * Constructor specifying the file the csv data. The file name (excluding directories) will be used as the
     * table name.
     *
     * @param filePath
     *        The path to the csv data file.
     *
     * @param multiline
     *        Boolean specifying if {@code DelimitedDataReader} reader should allow multiline data entries or not.
     *
     * @throws com.pb.sawdust.util.exceptions.RuntimeIOException if {@code filePath} was not found.
     */
    public CsvTableReader(String filePath, boolean multiline) {
        super(filePath,',',multiline);
    }

    /**
     * Constructor specifying the file the csv data. The file name (excluding directories) will be used as the
     * table name. Multiline data entries will not be allowed.
     *
     * @param filePath
     *        The path to the csv data file.
     *
     * @throws com.pb.sawdust.util.exceptions.RuntimeIOException if {@code filePath} was not found.
     */
    public CsvTableReader(String filePath) {
        super(filePath,',');
    }
}