package pl.mati.machinelearning.data;


import java.io.*;

public abstract class AbstractDataSetLoader implements DataSetLoader {
    @Override
    public DataSet loadData(InputStream dataStream) {
        return doLoadData(dataStream);
    }

    @Override
    public DataSet loadData(String data) {
        return loadData(new ByteArrayInputStream(data.getBytes()));
    }

    @Override
    public DataSet loadData(File data) throws FileNotFoundException {
        return loadData(new FileInputStream(data));
    }

    abstract DataSet doLoadData(InputStream dataStream);
}
