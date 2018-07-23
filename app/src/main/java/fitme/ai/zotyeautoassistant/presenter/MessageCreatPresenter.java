package fitme.ai.zotyeautoassistant.presenter;

import android.content.Context;

import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.model.MessageCreatModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IMessageCreatPresenter;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;
import fitme.ai.zotyeautoassistant.view.impl.IMessageManageService;


/**
 * Created by hongy on 2017/12/19.
 */

public class MessageCreatPresenter implements IMessageCreatPresenter,MessageCreatModel.messageCreatListener {

    private MessageCreatModel messageCreatModel;
    private IMessageManageService iMessageManageService;

    public MessageCreatPresenter(IMessageManageService iMessageManageService) {
        this.iMessageManageService = iMessageManageService;
        this.messageCreatModel = new MessageCreatModel(this);
    }

    @Override
    public void messageCreat(String speech, Context context) {
        messageCreatModel.messageCreat(speech,context);
    }

    @Override
    public void onSucess(Status status) {
        iMessageManageService.getMessageCreatResp(status);
    }

    @Override
    public void onFailure() {

    }
}
