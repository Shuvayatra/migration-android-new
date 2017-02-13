package com.taf.shuvayatra.ui.views;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.taf.model.BaseModel;
import com.taf.shuvayatra.ui.adapter.BlocksAdapter;

/**
 * ViewHolder for {@link BlocksAdapter}
 */
public abstract class BlockViewHolder<T extends ViewDataBinding, D extends BaseModel> extends RecyclerView.ViewHolder {

    public T binding;

    /**
     * Gets model for {@link BlocksAdapter#mBlocks}.
     *
     * @return the model
     */
    public abstract D getModel();

    public abstract void setModel(D model);

    /**
     * Data type for {@link BlocksAdapter#mBlocks}.
     *
     * @return the int
     */
    public abstract int getDataType();

    public BlockViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public T getBinding() {
        return binding;
    }
}
