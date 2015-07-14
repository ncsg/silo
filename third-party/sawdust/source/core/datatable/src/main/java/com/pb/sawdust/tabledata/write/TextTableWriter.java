package com.pb.sawdust.tabledata.write;

import com.pb.sawdust.util.exceptions.RuntimeIOException;
import com.pb.sawdust.util.format.TextFormat;
import static com.pb.sawdust.util.Range.*;
import com.pb.sawdust.tabledata.metadata.DataType;
import com.pb.sawdust.tabledata.DataTable;
import com.pb.sawdust.tabledata.DataRow;
import com.pb.sawdust.io.RuntimeCloseable;

import java.io.*;
import java.nio.charset.Charset;

/**
 * The {@code TextTableWriter} class provides a framework for writing data tables to text.
 *
 * @author crf <br/>
 *         Started: Jul 28, 2008 9:42:17 AM
 */
public abstract class TextTableWriter implements TableWriter,RuntimeCloseable {
    /**
     * The print writer which writes the table data.
     */
    protected final PrintWriter writer;

    private TextTableWriter(PrintWriter writer) {
        this.writer = writer;
    }

    /**
     * Constructor specifying the writer to which the table data will be written.
     *
     * @param writer
     *        The writer which will write the table data.
     */
    public TextTableWriter(Writer writer) {
        this(writer instanceof  PrintWriter ? (PrintWriter) writer : new PrintWriter(writer));
    }

    /**
     * Cosntructor specifying the file to which the table data will be written and the character set used to encode the
     * data.
     *
     * @param file
     *        The file to which the data table will be written.
     *
     * @param charset
     *        The character set to use when writing the table.
     *
     * @throws RuntimeIOException if {@code file} is not found or cannot be created.
     */
    public TextTableWriter(File file, Charset charset) {
        try {
            writer = new PrintWriter(file,charset.name());
        } catch (FileNotFoundException e) {
            throw new RuntimeIOException(e);
        }   catch (UnsupportedEncodingException e) {
            throw new RuntimeIOException(e);
        }
    }

    /**
     * Cosntructor specifying the file to which the table data will be written and the character set used to encode the
     * data.
     *
     * @param filePath
     *        The path to the file to which the data table will be written.
     *
     * @param charset
     *        The character set to use when writing the table.
     *
     * @throws com.pb.sawdust.util.exceptions.RuntimeIOException if {@code file} is not found or cannot be created.
     */
    public TextTableWriter(String filePath, Charset charset) {
        this(new File(filePath),charset);
    }

    /**
     * Cosntructor specifying the file to which the table data will be written. The default character encoding will be used.
     *
     * @param file
     *        The file to which the data table will be written.
     *
     * @throws com.pb.sawdust.util.exceptions.RuntimeIOException if {@code file} is not found or cannot be created.
     */
    public TextTableWriter(File file) {
        this(file,Charset.defaultCharset());
    }

    /**
     * Cosntructor specifying the file to which the table data will be written. The default character encoding will be used.
     *
     * @param filePath
     *        The path to the file to which the data table will be written.
     *
     * @throws com.pb.sawdust.util.exceptions.RuntimeIOException if {@code file} is not found or cannot be created.
     */
    public TextTableWriter(String filePath) {
        this(new File(filePath));
    }

    /**
     * Constructor specifying the output stream to which the table data will be sent.
     *
     * @param outStream
     *        The output stream to which the table data will be sent.
     */
    public TextTableWriter(OutputStream outStream) {
        this(new PrintWriter(outStream));
    }

    /**
     * Write the table header. This method will be called before any table data is written.
     *
     * @param table
     *        The data table for which the header is to be written.
     */
    protected abstract void writeHeader(DataTable table);

    /**
     * Write a row of data with the specified formats. It can (should) be assumed that the number of row data elements
     * and formats will be equal (<i>i.e.</i> that bounds checking has already been performed).
     *
     * @param dataRow
     *        The row data to be written.
     *
     * @param format
     *        The formats for each element (column) in the data row.
     */
    protected abstract void writeRow(Object[] dataRow, TextFormat[] format);

    /**
     * Write the table footer. This method will be called after all table data has been written.
     *
     * @param table
     *        The data table for which the footer is to be written.
     */
    protected abstract void writeFooter(DataTable table);

    private TextFormat getDefaultFormat(DataType dataType) {
        switch (dataType) {
            case BYTE :
            case SHORT :
            case INT :
            case LONG : return new TextFormat(TextFormat.Conversion.INTEGER);
            case FLOAT :
            case DOUBLE : return TextFormat.getPrecisionFormat(TextFormat.Conversion.FLOATING_POINT, 2);
            case BOOLEAN : return new TextFormat(TextFormat.Conversion.BOOLEAN);
            case STRING :
            default : return new TextFormat(TextFormat.Conversion.STRING);
        }
    }

    private TextFormat[] getDefaultFormats(DataType[] types) {
            TextFormat[] formats = new TextFormat[types.length];
            for (int i : range(types.length))
                formats[i] = getDefaultFormat(types[i]);
        return formats;
    }

    /**
     * Write the data table with default formats. The default formatting is as follows:
     *
     * <table  border="1">
     *     <tr>
     *         <th>
     *             Data Type
     *         </th>
     *         <th>
     *             Format
     *         </th>
     *     </tr>
     *     <tr>
     *         <td>
     *             <code>DataType.BYTE</code>
     *         </td>
     *         <td rowspan="4">
     *             <code>TextFormat.Conversion.INTEGER</code>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             <code>DataType.SHORT</code>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             <code>DataType.INT</code>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             <code>DataType.LONG</code>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             <code>DataType.FLOAT</code>
     *         </td>
     *         <td rowspan="2">
     *             <code>TextFormat.getPrecisionFormat(TextFormat.Conversion.FLOATING_POINT, 2)</code>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             <code>DataType.DOUBLE</code>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             <code>DataType.BOOLEAN</code>
     *         </td>
     *         <td>
     *             <code>TextFormat.Conversion.BOOLEAN</code>
     *         </td>
     *     </tr>
     *     <tr>
     *         <td>
     *             <code>DataType.STRING</code>
     *         </td>
     *         <td>
     *             <code>TextFormat.Conversion.STRING</code>
     *         </td>
     *     </tr>
     * </table>
     *
     * @param table
     *        The data table to write out.
     */
    public void writeTable(DataTable table) {
        writeTable(table,getDefaultFormats(table.getColumnTypes()));
    }

    /**
     * Write the data table with specified formats.
     *
     * @param table
     *        The data table to write out.
     *
     * @param formats
     *        The formats to use for each column.
     *
     * @throws IllegalArgumentException if the number of formats in {@code formats} does not equal the number of columns
     *                                  in {@code table}.
     */
    public void writeTable(DataTable table, TextFormat ... formats) {
        if (formats.length != table.getColumnCount())
            throw new IllegalArgumentException("Format count must match table column count; expected " + table.getColumnCount() + ", found " + formats.length);
        writeHeader(table);
        for (DataRow row : table)
            writeRow(row.getData(),formats);
        writeFooter(table);
        writer.flush();
    }

    public void close() {
        writer.close();
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

}