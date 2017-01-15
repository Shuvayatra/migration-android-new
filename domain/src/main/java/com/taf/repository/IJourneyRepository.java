package com.taf.repository;

import com.taf.model.Block;
import com.taf.model.base.ApiQueryParams;

import java.util.List;

import rx.Observable;


public interface IJourneyRepository {
    Observable<List<Block>> getBlocks(ApiQueryParams params);
}
