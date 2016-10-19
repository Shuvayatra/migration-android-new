package com.taf.repository;

import com.taf.model.Block;

import java.util.List;

import rx.Observable;

/**
 * Created by julian on 10/18/16.
 */

public interface IHomeRepository {
    Observable<List<Block>> getBlocks();
}
