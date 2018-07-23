package com.iflytek.speech;

public interface ITtsListener {
	void onDataReady();

	void onProgress(int nTextIndex, int nTextLen);
}
