package com.taf.data.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.taf.data.database.dao.DbCategory;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DB_CATEGORY".
*/
public class DbCategoryDao extends AbstractDao<DbCategory, Long> {

    public static final String TABLENAME = "DB_CATEGORY";

    /**
     * Properties of entity DbCategory.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Icon = new Property(2, String.class, "icon", false, "ICON");
        public final static Property DetailImage = new Property(3, String.class, "detailImage", false, "DETAIL_IMAGE");
        public final static Property DetailIcon = new Property(4, String.class, "detailIcon", false, "DETAIL_ICON");
        public final static Property ParentId = new Property(5, Long.class, "parentId", false, "PARENT_ID");
        public final static Property Position = new Property(6, Long.class, "position", false, "POSITION");
        public final static Property CategoryId = new Property(7, Long.class, "categoryId", false, "CATEGORY_ID");
        public final static Property SectionName = new Property(8, String.class, "sectionName", false, "SECTION_NAME");
    };


    public DbCategoryDao(DaoConfig config) {
        super(config);
    }
    
    public DbCategoryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DB_CATEGORY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"ICON\" TEXT," + // 2: icon
                "\"DETAIL_IMAGE\" TEXT," + // 3: detailImage
                "\"DETAIL_ICON\" TEXT," + // 4: detailIcon
                "\"PARENT_ID\" INTEGER," + // 5: parentId
                "\"POSITION\" INTEGER," + // 6: position
                "\"CATEGORY_ID\" INTEGER," + // 7: categoryId
                "\"SECTION_NAME\" TEXT);"); // 8: sectionName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DB_CATEGORY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DbCategory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(3, icon);
        }
 
        String detailImage = entity.getDetailImage();
        if (detailImage != null) {
            stmt.bindString(4, detailImage);
        }
 
        String detailIcon = entity.getDetailIcon();
        if (detailIcon != null) {
            stmt.bindString(5, detailIcon);
        }
 
        Long parentId = entity.getParentId();
        if (parentId != null) {
            stmt.bindLong(6, parentId);
        }
 
        Long position = entity.getPosition();
        if (position != null) {
            stmt.bindLong(7, position);
        }
 
        Long categoryId = entity.getCategoryId();
        if (categoryId != null) {
            stmt.bindLong(8, categoryId);
        }
 
        String sectionName = entity.getSectionName();
        if (sectionName != null) {
            stmt.bindString(9, sectionName);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DbCategory readEntity(Cursor cursor, int offset) {
        DbCategory entity = new DbCategory( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // icon
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // detailImage
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // detailIcon
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // parentId
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // position
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // categoryId
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // sectionName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DbCategory entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIcon(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDetailImage(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDetailIcon(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setParentId(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setPosition(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setCategoryId(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setSectionName(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DbCategory entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DbCategory entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}