package com.taf.shuvayatra.ui.interfaces;

import com.taf.model.BaseModel;

/**
 * Delegate recycler view item clicks to Fragment(s) or Activity(s)
 */
public interface ListItemClickWithDataTypeListener {

    /**
     * Callback for list item click
     *
     * @param model           the data model for list item
     * @param dataType        the data type for list item
     * @param adapterPosition the adapter position for list item
     */
    void onListItemSelected(BaseModel model, int dataType, int adapterPosition);

    void onDeeplinkSelected(String deeplink, BaseModel model, int dataType, int adapterPosition);
}
