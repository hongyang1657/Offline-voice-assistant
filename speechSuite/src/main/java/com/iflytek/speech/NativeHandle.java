package com.iflytek.speech;

public class NativeHandle {
	public int err_ret;
	public long native_point;

	public NativeHandle() {
		err_ret = 0;
		native_point = 0;
	}
	
	public int getErr_ret() {
		return err_ret;
	}

	public long getNative_point() {
		return native_point;
	}

	public void setErr_ret(int err_ret) {
		this.err_ret = err_ret;
	}

	public void setNative_point(long native_point) {
		this.native_point = native_point;
	}

	public void reSet() {
		err_ret = 0;
		native_point = 0;
	}
}
