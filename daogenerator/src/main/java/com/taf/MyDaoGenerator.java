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
        createPostTable(pSchema);
    }

    private static void createPostTable(Schema pSchema){
        Entity post = pSchema.addEntity("DbPost");
        post.addIdProperty();
        post.addLongProperty("createdAt");
        post.addLongProperty("updatedAt");
        post.addStringProperty("tags");
        post.addStringProperty("type");
        post.addStringProperty("description");
        post.addStringProperty("title");
    }
}


