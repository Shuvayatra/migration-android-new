package com.taf.shuvayatra.ui.interfaces;


import com.taf.model.BaseModel;

/**
 * Delegate recycler view item clicks to Fragment(s) or Activity(s)
 * <p>
 * todo deprecate {@link ListItemClickListener} in favor of {@link ListItemClickWithDataTypeListener}
 */
public interface ListItemClickListener {
    void onListItemSelected(BaseModel pModel, int pIndex);
}
