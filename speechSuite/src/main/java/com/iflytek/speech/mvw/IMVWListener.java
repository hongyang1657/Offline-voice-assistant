package com.iflytek.speech.mvw;

public interface IMVWListener{
	public void onMVWWakeup(int nMvwScene, int nMvwId, int nMvwScore, String lParam);
    public void onMVWMsgProc_(long uMsg, long wParam, String lParam);
}