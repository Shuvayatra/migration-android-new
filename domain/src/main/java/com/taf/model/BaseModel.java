package com.taf.model;

import java.io.Serializable;

/**
 * Created by julian on 12/7/15.
 */
public abstract class BaseModel implements Serializable {
    Long mId;
    int mDataType;

    public Long getId() {
        return mId;
    }

    public void setId(Long pId) {
        mId = pId;
    }

    public int getDataType() {
        return mDataType;
    }

    public void setDataType(int pDataType) {
        mDataType = pDataType;
    }
}
