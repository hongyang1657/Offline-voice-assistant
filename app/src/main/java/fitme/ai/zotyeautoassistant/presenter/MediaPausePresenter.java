package fitme.ai.zotyeautoassistant.presenter;


import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.model.MediaPauseModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IMediaPausePresenter;
import fitme.ai.zotyeautoassistant.view.impl.IMediaPlayerView;

/**
 * Created by hongy on 2017/12/22.
 */

public class MediaPausePresenter implements IMediaPausePresenter,MediaPauseModel.mediaPauseListener{

    private MediaPauseModel mediaPauseModel;
    private IMediaPlayerView iMediaPlayerView;

    public MediaPausePresenter(IMediaPlayerView iMediaPlayerView) {
        this.iMediaPlayerView = iMediaPlayerView;
        mediaPauseModel = new MediaPauseModel(this);
    }

    @Override
    public void mediaPause(int position) {
        mediaPauseModel.mediaPause(position);
    }

    @Override
    public void onSucess(Status responseBody) {
        iMediaPlayerView.getMediaPause(responseBody);
    }

    @Override
    public void onFailure() {

    }
}
