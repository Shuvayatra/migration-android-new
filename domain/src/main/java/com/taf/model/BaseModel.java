package com.taf.model;

import java.io.Serializable;

public class BaseModel implements Serializable {
    Long id;
    int mDataType;

    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
    }

    public int getDataType() {
        return mDataType;
    }

    public void setDataType(int pDataType) {
        mDataType = pDataType;
    }
}
