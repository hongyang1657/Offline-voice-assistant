package fitme.ai.zotyeautoassistant.presenter;

import android.content.Context;

import fitme.ai.zotyeautoassistant.model.SmartSceneControlModel;
import fitme.ai.zotyeautoassistant.presenter.impl.ISmartSceneControlPresenter;
import fitme.ai.zotyeautoassistant.view.impl.IMessageManageService;
import okhttp3.ResponseBody;

/**
 * Created by hongy on 2017/12/22.
 */

public class SmartSceneControlPresenter implements ISmartSceneControlPresenter,SmartSceneControlModel.sceneControlListener{

    private SmartSceneControlModel smartSceneControlModel;
    private IMessageManageService iMessageManageService;

    public SmartSceneControlPresenter(IMessageManageService iMessageManageService) {
        this.iMessageManageService = iMessageManageService;
        smartSceneControlModel = new SmartSceneControlModel(this);
    }

    @Override
    public void sceneActivate(String words, Context context) {
        smartSceneControlModel.sceneActivate(words,context);
    }

    @Override
    public void onSucess(ResponseBody responseBody) {
        iMessageManageService.getSceneActivate(responseBody);
    }

    @Override
    public void onFailure() {

    }
}
