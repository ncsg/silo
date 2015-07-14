package transcad;

import java.io.*;

/**
 This {@code Matrix} class is a placeholder for the actual class in the <code>TranscadMatrix.jar</code> file distributed with the 
 TransCAD program (located in the <code>[transcad_installation_dir]/GISDK/Matrices/</code> directory). This class (and the others
 in this package) are to allow dependent classes the ability to compile when a TransCAD installation is not available (or so as not
 to hardcode a TransCAD installation path). If this class is used in production code, then the actual <code>TranscadMatrix.jar</code>
 must be included in the classpath <i>before</i> the resource listing this class for the dependent production code to operate
 correctly.
 **/
public class Matrix {

    private Matrix() {
        throw new RuntimeException("You are calling TranscadMatrix api from a placeholder class." +
                                   "Be sure to place the actual TranscadMatrix.jar " +
                                   "(from the [transcad_installation_dir]/GISDK/Matrics/ directory) before this class in your classpath.");
    }

    public Matrix(String FileName) throws FileNotFoundException {
        this();
    }

    public Matrix(String FileName, String MatrixLabel, short NCores, long NRows, long NCols, byte DataType, short Compression, String[] CoreNames) throws IOException {
        this();
    }

    public Matrix(String FileName, String MatrixLabel, short NCores, long NRows, long NCols, byte DataType, short Compression, String[] CoreNames, long[] RowIDs, long[] ColIDs) throws IOException, ArrayIndexOutOfBoundsException {
        this();
    }

    public boolean Copy(String FileName, long[] CoresToCopy) throws IllegalArgumentException {
        return false;
    }

    public int dispose() {
     return 0;
    }

    public short GetNIndices(int dim) {
        return 0;
    }

    public short getNCores() {
        return 0;
    }

    public short setCore(int CoreNumber) throws IllegalArgumentException {
        return 0;
    }

    public short GetCore() {
        return 0;
    }

    public short setIndex(int Dimension, short Index) {
        return 0;
    }

    public long getBaseNRows() {
        return 0;
    }

    public long getBaseNCols() {
        return 0;
    }

    public long getNRows() {
        return 0;
    }

    public long getNCols() {
        return 0;
    }

    public String getFileName() {
        return "";
   }

    public byte getElementType() {
        return 0;
    }

    public short setElement(long Row, long Col, short Value) {
        return 0;
    }

    public short setElement(long Row, long Col, long Value) {
        return 0;
    }

    public short setElement(long Row, long Col, float Value) {
        return 0;
    }

    public short setElement(long Row, long Col, double Value) {
        return 0;
    }

    public short getElementShort(long Row, long Col) {
        return 0;
   }

    public long getElementLong(long Row, long Col) {
        return 0;
    }

    public float getElementFloat(long Row, long Col) {
        return 0.0f;
    }

    public double getElementDouble(long Row, long Col) {
        return 0.0;
    }

    public short getBaseVector(int Dimension, long Position, Object ArrayToFill) {
        return 0;
    }

    public short getVector(int Dimension, long ID, Object ArrayToFill) {
        return 0;
    }

    public short setBaseVector(int Dimension, long Position, Object ArrayToCopy) {
        return 0;
    }

    public short setVector(int Dimension, long ID, Object ArrayToCopy) {
        return 0;
    }

    public short GetIDs(int Dimension, int[] ArrayToFill) {
        return 0;
    }

    public short SetLabel(int CoreNumber, String label) {
        return 0;
    }

    public String GetLabel(int CoreNumber) {
        return "";
    }

    public String getStatusString() {
        return "";
    }

    public int getStatus() {
        return 0;
    }

    public static void main(String args[]) throws Throwable {
        new Matrix();
    }

    public static short SHORT_MISS;
    public static long LONG_MISS;
    public static float FLOAT_MISS;
    public static double DOUBLE_MISS;
    public static int _MAX_FLABEL;
    public static int _MAX_PATH;
 }