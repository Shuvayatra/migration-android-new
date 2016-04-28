package com.taf;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.taf.data.database.dao");
        createDB(schema);
        new DaoGenerator().generateAll(schema, args[0]);
    }

    private static void createDB(Schema pSchema) {
        pSchema.enableKeepSectionsByDefault();
        Entity post = createPostTable(pSchema);
        Entity section = createSectionTable(pSchema);
        Entity category = createCategoryTable(pSchema);

        // define one-to-many for section-category
        Property sectionId = category.addLongProperty("sectionId").notNull().getProperty();
        category.addToOne(section, sectionId).setName("section");
        ToMany sectionToCategories = section.addToMany(category, sectionId);
        sectionToCategories.setName("categoryList");
        sectionToCategories.orderAsc(category.getProperties().get(6));

        createPostCategoryTable(pSchema);
        createNotificationTable(pSchema);
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
        return post;
    }

    private static Entity createPostCategoryTable(Schema pSchema) {
        Entity postCategory = pSchema.addEntity("DbPostCategory");
        postCategory.addIdProperty();
        postCategory.addLongProperty("postId");
        postCategory.addLongProperty("categoryId");
        return postCategory;
    }

    private static Entity createSectionTable(Schema pSchema) {
        Entity section = pSchema.addEntity("DbSection");
        section.addIdProperty();
        section.addStringProperty("title");
        section.addStringProperty("alias");
        section.addLongProperty("createdAt");
        section.addLongProperty("updatedAt");
        return section;
    }

    private static Entity createCategoryTable(Schema pSchema) {
        Entity category = pSchema.addEntity("DbCategory");
        category.addIdProperty();
        category.addStringProperty("title");
        category.addStringProperty("iconUrl");
        category.addStringProperty("smallIconUrl");
        category.addStringProperty("coverImageUrl");
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

}


