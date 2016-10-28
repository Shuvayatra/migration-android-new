package com.taf.data.entity;

/**
 * Created by julian on 10/28/16.
 */

public class UpdateRequestEntity {
    String type;

    public UpdateRequestEntity(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
