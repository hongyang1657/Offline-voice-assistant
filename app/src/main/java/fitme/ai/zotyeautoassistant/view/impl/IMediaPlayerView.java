package fitme.ai.zotyeautoassistant.view.impl;


import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.bean.StatusWithUrl;

/**
 * Created by hongy on 2017/12/22.
 */

public interface IMediaPlayerView {
    void getMediaNext(StatusWithUrl statusWithUrl);
    void getMediaPause(Status responseBody);
    void getMediaReport(Status responseBody);
    void getMediaContinue(Status responseBody);
}
