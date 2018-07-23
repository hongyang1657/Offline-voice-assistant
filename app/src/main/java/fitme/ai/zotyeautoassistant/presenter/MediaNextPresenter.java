package fitme.ai.zotyeautoassistant.presenter;


import fitme.ai.zotyeautoassistant.bean.StatusWithUrl;
import fitme.ai.zotyeautoassistant.model.MediaNextModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IMediaNextPresenter;
import fitme.ai.zotyeautoassistant.view.impl.IMediaPlayerView;

/**
 * Created by hongy on 2017/12/22.
 */

public class MediaNextPresenter implements IMediaNextPresenter,MediaNextModel.mediaNextListener{

    private MediaNextModel mediaNextModel;
    private IMediaPlayerView iMediaPlayerView;

    public MediaNextPresenter(IMediaPlayerView iMediaPlayerView) {
        this.iMediaPlayerView = iMediaPlayerView;
        mediaNextModel = new MediaNextModel(this);
    }

    @Override
    public void mediaNext() {
        mediaNextModel.mediaNext();
    }

    @Override
    public void onSucess(StatusWithUrl responseBody) {
        iMediaPlayerView.getMediaNext(responseBody);
    }

    @Override
    public void onFailure() {

    }
}
