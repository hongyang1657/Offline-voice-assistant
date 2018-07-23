package fitme.ai.zotyeautoassistant.presenter;


import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.model.MessageArrivedModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IMessageArrivedPresenter;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;
import fitme.ai.zotyeautoassistant.view.impl.IMessageManageService;

/**
 * Created by hongy on 2017/12/19.
 */

public class MessageArrivedPresenter implements IMessageArrivedPresenter,MessageArrivedModel.messageArrivedListener{

    private MessageArrivedModel messageArrivedModel;
    private IMessageManageService iMessageManageService;

    public MessageArrivedPresenter(IMessageManageService iMessageManageService) {
        this.iMessageManageService = iMessageManageService;
        messageArrivedModel = new MessageArrivedModel(this);
    }

    @Override
    public void messageArrived(String messageId) {
        messageArrivedModel.messageArrived(messageId);
    }


    @Override
    public void onSucess(Status status) {
        iMessageManageService.getMessageArrivedResp(status);
    }

    @Override
    public void onFailure() {

    }

}
