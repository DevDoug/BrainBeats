package com.brainbeats.service;

/*
 * Created by douglas on 7/21/2016.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.brainbeats.sync.BrainBeatsSyncAdapter;

/**
 * Define a Service that returns an IBinder for the
 * com.brainbeats.sync adapter class, allowing the com.brainbeats.sync adapter framework to call
 * onPerformSync().
 */
public class SyncService extends Service {
    // Storage for an instance of the com.brainbeats.sync adapter
    private static BrainBeatsSyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();

    /*
     * Instantiate the com.brainbeats.sync adapter object.
     */
    @Override
    public void onCreate() {
        /*
         * Create the com.brainbeats.sync adapter as a singleton.
         * Set the com.brainbeats.sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new BrainBeatsSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    /**
     * Return an object that allows the system to invoke
     * the com.brainbeats.sync adapter.
     */
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
