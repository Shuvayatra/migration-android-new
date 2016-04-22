package com.taf.repository;

import com.taf.model.Category;

import java.util.List;

import rx.Observable;

/**
 * Created by Nirazan-PC on 4/21/2016.
 */
public interface ISectionRepository extends IBaseRepository<Category> {
    Observable<List<Category>> getListBySectionName(String name);

}
