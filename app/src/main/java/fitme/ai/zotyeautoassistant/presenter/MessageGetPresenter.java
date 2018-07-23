package fitme.ai.zotyeautoassistant.presenter;


import fitme.ai.zotyeautoassistant.bean.MessageGet;
import fitme.ai.zotyeautoassistant.model.MessageGetModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IMessageGetPresenter;
import fitme.ai.zotyeautoassistant.view.impl.IMessageManageService;

/**
 * Created by hongy on 2017/12/19.
 */

public class MessageGetPresenter implements IMessageGetPresenter,MessageGetModel.messageGetListener{

    private MessageGetModel messageGetModel;
    private IMessageManageService iMessageManageService;


    public MessageGetPresenter(IMessageManageService iMessageManageService) {
        this.iMessageManageService = iMessageManageService;
        messageGetModel = new MessageGetModel(this);
    }

    @Override
    public void messageGet() {
        messageGetModel.messageGet();
    }

    @Override
    public void onSucess(MessageGet messageGet) {
        iMessageManageService.getMessageResp(messageGet);
    }

    @Override
    public void onFailure() {

    }
}
