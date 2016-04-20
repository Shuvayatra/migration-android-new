package com.taf.data.database;

import com.taf.data.database.dao.DaoSession;
import com.taf.data.database.dao.dbPost;
import com.taf.data.database.dao.dbPostDao;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.entity.mapper.DataMapper;

import java.util.List;

import javax.inject.Inject;

public class DatabaseHelper{

    private final DaoSession mDaoSession;
    private final DataMapper mDataMapper;

    @Inject
    public DatabaseHelper(DaoSession pDaoSession, DataMapper pDataMapper){
        mDaoSession = pDaoSession;
        mDataMapper = pDataMapper;
    }

    public void clearCache(DaoSession pDaoSession) {
        pDaoSession.clear();
    }

    public void insertUpdate(LatestContentEntity pEntity){
        insertUpdate(pEntity.getPosts());
    }

    public void insertUpdate(List<PostEntity> pEntities){
        dbPostDao postDao = mDaoSession.getDbPostDao();
        for (PostEntity entity : pEntities) {
            dbPost post = mDataMapper.transformPostForDB(entity);
            if(post != null) {
                postDao.insertOrReplace(post);
            }
        }
    }

}
