package com.chaolu.imsdk;

import android.app.Application;
import android.content.Context;

import com.chaolu.imsdk.manager.ImSdkManager;

public class ImSdkLibrary {
    private static ImSdkLibrary sInstance;
    private Context mContext;
    private Class mMainActivityClass;

    private ImSdkLibrary() {
    }

    public static ImSdkLibrary getInstance() {
        if (sInstance == null) {
            synchronized (ImSdkLibrary.class) {
                if (sInstance == null)
                    sInstance = new ImSdkLibrary();
            }
        }
        return sInstance;
    }

    public Context getContext() {
        return mContext;
    }

    public Class getMainActivityClass() {
        return mMainActivityClass;
    }

    public synchronized void init(Application application, String appId, Class mMainActivityClass) {
        if (mContext == null) {
            mContext = application;
            this.mMainActivityClass = mMainActivityClass;
            ImSdkManager.getInstance().init(application, appId);
        }
    }
}
