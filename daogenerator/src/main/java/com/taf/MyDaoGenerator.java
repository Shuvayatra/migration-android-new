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
        Entity post = createPostTable(pSchema);
        createCategoryTable(pSchema);

        createPostCategoryTable(pSchema);
        createNotificationTable(pSchema);
        createTagsTable(pSchema);
    }

    private static Entity createPostTable(Schema pSchema) {
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
        post.addIntProperty("viewCount");
        post.addIntProperty("unsyncedViewCount");
        post.addStringProperty("featuredImage");
        post.addIntProperty("unsyncedShareCount");
        return post;
    }

    private static Entity createPostCategoryTable(Schema pSchema) {
        Entity postCategory = pSchema.addEntity("DbPostCategory");
        postCategory.addIdProperty();
        postCategory.addLongProperty("postId");
        postCategory.addLongProperty("categoryId");
        return postCategory;
    }

    private static Entity createCategoryTable(Schema pSchema) {
        Entity category = pSchema.addEntity("DbCategory");
        category.addIdProperty();
        category.addStringProperty("title");
        category.addStringProperty("alias");
        category.addStringProperty("iconUrl");
        category.addStringProperty("smallIconUrl");
        category.addStringProperty("coverImageUrl");
        category.addIntProperty("leftIndex");
        category.addIntProperty("rightIndex");
        category.addIntProperty("depth");
        category.addLongProperty("parentId");
        category.addLongProperty("position");
        category.addLongProperty("createdAt");
        category.addLongProperty("updatedAt");
        return category;
    }

    private static Entity createNotificationTable(Schema pSchema) {
        Entity notification = pSchema.addEntity("DbNotification");
        notification.addIdProperty();
        notification.addStringProperty("title");
        notification.addStringProperty("description");
        notification.addLongProperty("createdAt");
        notification.addLongProperty("updatedAt");
        return notification;
    }

    private static Entity createTagsTable(Schema pSchema) {
        Entity tag = pSchema.addEntity("DbTag");
        tag.addIdProperty();
        tag.addStringProperty("title");
        return tag;
    }
}


