package com.taf.shuvayatra.ui.interfaces;

import com.taf.model.BaseModel;

import java.util.List;

/**
 * Created by julian on 4/19/16.
 */
public interface ListItemClickListener {
    void onListItemSelected(BaseModel pModel);
    void onListItemSelected(List<BaseModel> pCollection, int pIndex);
}
