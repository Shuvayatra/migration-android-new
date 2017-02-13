package com.taf.model;

/**
 * Created by umesh on 1/13/17.
 */

public class ScreenModel extends BaseModel {

    private String title;
    private String icon;
    private String endPoint;
    private String type;
    private int order;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.....");
        System.out.println("SCREEN MODEL, EQUALS");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.....");
        if (o == null || getClass() != o.getClass()) return false;

        ScreenModel model = (ScreenModel) o;

        if (id.equals(((ScreenModel) o).id)) return true;
        if (title != null ? !title.equals(model.title) : model.title != null) return false;
        if (icon != null ? !icon.equals(model.icon) : model.icon != null) return false;
        if (endPoint != null ? !endPoint.equals(model.endPoint) : model.endPoint != null)
            return false;
        return type != null ? type.equals(model.type) : model.type == null;

    }
}
