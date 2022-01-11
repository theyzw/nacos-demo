package com.yk.mysql.maker.bean;

import java.util.List;
import java.util.Set;

public class Table {

    private Column columnKey;
    private List<Column> columnList;
    private Set<String> dataTypeSet;

    public Table() {
        super();
    }

    public Table(Column columnKey, List<Column> columnList, Set<String> dataTypeSet) {
        super();
        this.columnKey = columnKey;
        this.columnList = columnList;
        this.dataTypeSet = dataTypeSet;
    }

    public Column getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(Column columnKey) {
        this.columnKey = columnKey;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public Set<String> getDataTypeSet() {
        return dataTypeSet;
    }

    public void setDataTypeSet(Set<String> dataTypeSet) {
        this.dataTypeSet = dataTypeSet;
    }

}
