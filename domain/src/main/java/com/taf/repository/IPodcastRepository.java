package com.taf.repository;

import com.taf.model.Podcast;

import java.util.List;

import rx.Observable;

/**
 * Created by julian on 10/18/16.
 */

public interface IPodcastRepository {
    Observable<List<Podcast>> getPodcasts();
}
