package com.taf;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.taf.data.database.dao");
        createDB(schema);
        new DaoGenerator().generateAll(schema, args[0]);
    }

    private static void createDB(Schema pSchema) {
        pSchema.enableKeepSectionsByDefault();
        createPostTable(pSchema);
        createSectionTable(pSchema);
        createCategories(pSchema);
        createPostCategoryTable(pSchema);
    }

    private static void createPostTable(Schema pSchema){
        Entity post = pSchema.addEntity("DbPost");
        post.addIdProperty();
        post.addStringProperty("title");
        post.addStringProperty("description");
        post.addStringProperty("type");
        post.addStringProperty("data");
        post.addStringProperty("source");
        post.addStringProperty("tags");
        post.addLongProperty("createdAt");
        post.addLongProperty("updatedAt");
        post.addIntProperty("favouriteCount");
        post.addIntProperty("shareCount");
        post.addBooleanProperty("isFavourite");
        post.addBooleanProperty("isSynced");
        post.addBooleanProperty("isDownloaded");
        post.addLongProperty("downloadReference");
    }

    private static void createPostCategoryTable(Schema pSchema){
        Entity postCategory = pSchema.addEntity("DbPostCategory");
        postCategory.addIdProperty();
        postCategory.addLongProperty("postId");
        postCategory.addLongProperty("categoryId");
    }

    private static void createSectionTable(Schema pSchema){
        Entity section = pSchema.addEntity("DbSection");
        section.addIdProperty();
        section.addLongProperty("sectionId");
        section.addStringProperty("name");
        section.addStringProperty("display_name");
    }

    private static void createCategories(Schema pSchema){
        Entity category = pSchema.addEntity("DbCategory");
        category.addIdProperty();
        category.addStringProperty("name");
        category.addStringProperty("icon");
        category.addStringProperty("detailImage");
        category.addStringProperty("detailIcon");
        category.addLongProperty("parentId");
        category.addLongProperty("position");
        category.addLongProperty("categoryId");
        category.addStringProperty("sectionName");
    }
}


