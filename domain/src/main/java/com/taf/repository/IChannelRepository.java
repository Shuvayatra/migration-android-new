package com.taf.repository;

import com.taf.model.Block;
import com.taf.model.Channel;

import java.util.List;

import rx.Observable;

/**
 * Created by ngima on 11/3/16.
 */

public interface IChannelRepository {
    Observable<List<Channel>> getChannelList();
}
