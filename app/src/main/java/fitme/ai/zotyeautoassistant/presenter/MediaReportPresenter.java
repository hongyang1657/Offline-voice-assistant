package fitme.ai.zotyeautoassistant.presenter;


import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.model.MediaReportModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IMediaReportPresenter;
import fitme.ai.zotyeautoassistant.view.impl.IMediaPlayerView;

/**
 * Created by hongy on 2017/12/22.
 */

public class MediaReportPresenter implements IMediaReportPresenter,MediaReportModel.mediaReportListener{

    private MediaReportModel mediaReportModel;
    private IMediaPlayerView iMediaPlayerView;

    public MediaReportPresenter(IMediaPlayerView iMediaPlayerView) {
        this.iMediaPlayerView = iMediaPlayerView;
        mediaReportModel = new MediaReportModel(this);
    }

    @Override
    public void mediaReport(String url, int position, String musicPlayerStatus) {
        mediaReportModel.mediaReport(url,position,musicPlayerStatus);
    }

    @Override
    public void onSucess(Status responseBody) {
        iMediaPlayerView.getMediaReport(responseBody);
    }

    @Override
    public void onFailure() {

    }
}
