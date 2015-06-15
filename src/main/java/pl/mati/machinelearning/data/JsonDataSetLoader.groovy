package pl.mati.machinelearning.data

import groovy.json.JsonSlurper


class JsonDataSetLoader extends AbstractDataSetLoader {

    @Override
    DataSet doLoadData(InputStream dataStream) {
        JsonSlurper slurper = new JsonSlurper()
        def json = slurper.parse(dataStream)
        Map<Integer, FieldInfo> infoMap = getInfoMap(json);
        Map<String, Integer> columnMap = getColumnMap(json);
        List<DataRow> rows = json.data.collect{
            new DataRow(columnMap, createCellList(it, infoMap))
        }
        return new DataSet(columnMap, infoMap, rows)
    }

    private static Map<Integer, FieldInfo> getInfoMap(def json) {
        Map<Integer, FieldInfo> map = [:]
        json.metadata.eachWithIndex {
            val,index ->
                map.put(index as Integer, new FieldInfo(val.type as FieldType, val.allowedValues.collect{it as String} as Set<String>))
        }
        map
    }

    private static Map<String, Integer> getColumnMap(def json) {
        Map<String, Integer> map = [:]
        json.metadata.eachWithIndex {
            val, index ->
                map.put(val.name as String, index as Integer)
        }
        map
    }

    private static List<Cell> createCellList(List list, Map<Integer, FieldInfo> integerFieldInfoMap) {
        def l = new ArrayList<Cell>()
        list.eachWithIndex  {
            val, i ->
                def info = integerFieldInfoMap[i as Integer]
                if(info.fieldType == FieldType.NUMBER){
                    l << new Cell<Double>(info, val as Double)
                } else {
                    l << new Cell<String>(info, val as String)
                }
        }
        return l;
    }
}
