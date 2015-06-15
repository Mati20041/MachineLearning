package pl.mati.machinelearning.data;

public class Cell<T> {
    private FieldInfo info;
    private T value;

    public Cell(FieldInfo info, T value) {
        this.info = info;
        this.value = value;
    }

    public FieldInfo getInfo() {
        return info;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Cell && value.equals(((Cell) obj).getValue());
    }

    @Override
    public int hashCode() {
        int result = info != null ? info.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
