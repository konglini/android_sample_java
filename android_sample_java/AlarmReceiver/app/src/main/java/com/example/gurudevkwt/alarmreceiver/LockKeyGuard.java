package com.example.gurudevkwt.alarmreceiver;

import android.app.KeyguardManager;
import android.content.Context;

/**
 * Created by User on 2016-08-25.
 */
public class LockKeyGuard {

    Context mContext;

    public LockKeyGuard(Context context){
        mContext = context;
    }

    public void disable(){
        KeyguardManager manager = (KeyguardManager)mContext.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kgl = manager.newKeyguardLock(Context.KEYGUARD_SERVICE);
        kgl.disableKeyguard();
    }

    public void enable(){
        KeyguardManager manager = (KeyguardManager)mContext.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kgl = manager.newKeyguardLock(Context.KEYGUARD_SERVICE);
        kgl.reenableKeyguard();
    }

}
