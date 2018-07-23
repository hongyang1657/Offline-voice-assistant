package fitme.ai.zotyeautoassistant.presenter;


import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.model.MediaContinueModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IMediaContinuePresenter;
import fitme.ai.zotyeautoassistant.view.impl.IMediaPlayerView;

/**
 * Created by hongy on 2017/12/22.
 */

public class MediaContinuePresenter implements IMediaContinuePresenter,MediaContinueModel.mediaContinueListener{

    private MediaContinueModel mediaContinueModel;
    private IMediaPlayerView iMediaPlayerView;

    public MediaContinuePresenter(IMediaPlayerView iMediaPlayerView) {
        this.iMediaPlayerView = iMediaPlayerView;
        mediaContinueModel = new MediaContinueModel(this);
    }

    @Override
    public void mediaContinue() {
        mediaContinueModel.mediaContinue();
    }

    @Override
    public void onSucess(Status responseBody) {
        iMediaPlayerView.getMediaContinue(responseBody);
    }

    @Override
    public void onFailure() {

    }
}
