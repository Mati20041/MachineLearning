package pl.mati.machinelearning.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public interface DataSetLoader {
    public DataSet loadData(InputStream dataStream);
    public DataSet loadData(String data);
    public DataSet loadData(File data) throws FileNotFoundException;
}
