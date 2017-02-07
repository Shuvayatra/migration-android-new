package com.taf.model;


import com.taf.util.MyConstants;

/**
 * Created by julian on 10/20/16.
 */
public class Notice extends BaseModel {

    String title;
    String description;
    String image;
    String deeplink;
    boolean fromDismiss;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }

    public boolean isFromDismiss() {
        return fromDismiss;
    }

    public void setFromDismiss(boolean fromDismiss) {
        this.fromDismiss = fromDismiss;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public static Block convertToBlock(Notice notice) {
        Block block = new Block();
        block.setId(notice.getId());
        block.setLayout(Block.TYPE_NOTICE);
        block.setNotice(notice);
        return block;
    }

    @Override
    public int getDataType() {
        return MyConstants.Adapter.TYPE_NOTICE;
    }
}
