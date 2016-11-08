package com.taf.shuvayatra.ui.views;

import com.taf.model.Channel;

import java.util.List;

/**
 * Created by ngima on 11/3/16.
 */

public interface ChannelView extends LoadDataView {
    void renderChannel(List<Channel> channelList);
}
