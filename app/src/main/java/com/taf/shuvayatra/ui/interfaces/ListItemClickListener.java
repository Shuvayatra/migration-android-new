package com.taf.shuvayatra.ui.interfaces;

import com.taf.model.BaseModel;

import java.util.List;

public interface ListItemClickListener {
    void onListItemSelected(BaseModel pModel, int pIndex);

    void onListItemSelected(List<BaseModel> pCollection, int pIndex);
}
