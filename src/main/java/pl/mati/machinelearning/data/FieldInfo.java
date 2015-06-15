package pl.mati.machinelearning.data;

import java.util.Collections;
import java.util.Set;

public class FieldInfo {
    private FieldType fieldType;
    private Set<String> allowedValues;

    public FieldInfo(FieldType fieldType, Set<String> allowedValues) {
        this.fieldType = fieldType;
        this.allowedValues = allowedValues;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public Set<String> getAllowedValues() {
        return Collections.unmodifiableSet(allowedValues);
    }

}
