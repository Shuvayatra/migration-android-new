package com.taf.repository;

import com.taf.model.PodcastResponse;

import rx.Observable;

/**
 * Created by julian on 10/18/16.
 */

public interface IPodcastRepository {
    Observable<PodcastResponse> getPodcasts();
}
