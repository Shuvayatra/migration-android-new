package com.taf.data.repository.deprecated;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DBDataStore;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.Category;
import com.taf.repository.deprecated.ISectionRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by Nirazan-PC on 4/21/2016.
 */
public class SectionCategoryRepository implements ISectionRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public SectionCategoryRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataStoreFactory = pDataStoreFactory;
        mDataMapper = pDataMapper;
    }

    @Override
    public Observable<List<Category>> getList(int pLimit, int pOffset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<Category> getSingle(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<List<Category>> getListBySectionName(String section, boolean isCategory, Long pParentId) {
        DBDataStore dataStore = mDataStoreFactory.createDBDataStore();
        return dataStore.getCategoriesBySection(section, isCategory, pParentId)
                .map(pDbCategories -> mDataMapper.transformCategoryFromDb(pDbCategories));
    }
}
