package fitme.ai.zotyeautoassistant.utils;

import android.content.Context;
import android.os.AsyncTask;

import fitme.ai.zotyeautoassistant.utils.impl.PushResToSDCardListener;

public class PushIflytekResToSDCardTask extends AsyncTask<Void,Integer,Integer>{

    private Context mContext;
    private PushResToSDCardListener listener;

    public PushIflytekResToSDCardTask(Context mContext,PushResToSDCardListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        L.i("doInBackground,正在复制离线资源到sd卡");
        AssetUtil.copyAssetsFilesToSDCard(mContext,"iflytek","/sdcard/iflytek");    //将assets中讯飞离线sdk模型复制到sd卡
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        L.i("onPostExecute复制完毕");
        listener.onComplete();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        L.i("onPreExecute");
    }
}
