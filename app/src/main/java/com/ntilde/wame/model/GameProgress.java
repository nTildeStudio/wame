package com.ntilde.wame.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.games.snapshot.Snapshots;
import com.ntilde.wame.helpers.Serializer;

import java.io.IOException;
import java.io.Serializable;

public class GameProgress implements Serializable{


    private int maxLevel;


    transient private static final String PREFERENCES="WAME_PREFS";
    transient private static final String PREF_MAX_LEVEL="MAX_LEVEL";
    transient private SharedPreferences prefs;

    transient private Context context;

    transient private Snapshot snapshot;

    public GameProgress(Context ctx){
        context=ctx;
        prefs=ctx.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        maxLevel=prefs.getInt(PREF_MAX_LEVEL,0);
    }

    public void save(GoogleApiClient mGoogleApiClient){
        if(!mGoogleApiClient.isConnected()){
            return;
        }
        try {
            snapshot.getSnapshotContents().writeBytes(Serializer.serialize(this));
        } catch (IOException e) { }

        SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                .build();

        Games.Snapshots.commitAndClose(mGoogleApiClient, snapshot, metadataChange);
    }

    public int getMaxLevel(){
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        prefs.edit().putInt(PREF_MAX_LEVEL,maxLevel).commit();
    }


    private byte[] mSaveGameData;

    private String mCurrentSaveName="wamesave";

    public void loadFromSnapshot(final GoogleApiClient mGoogleApiClient) {

        AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                Snapshots.OpenSnapshotResult result = Games.Snapshots.open(mGoogleApiClient, mCurrentSaveName, true).await();

                if (result.getStatus().isSuccess()) {
                    snapshot = result.getSnapshot();
                    try {
                        Object obj=Serializer.deserialize(snapshot.getSnapshotContents().readFully());
//                        ((TextView)((HomeActivity) context).findViewById(R.id.home_play_text)).setText("lvl:"+((GameProgress)obj).getMaxLevel());
                        maxLevel=((GameProgress)obj).getMaxLevel();
                    } catch (IOException e) {
//                        ((TextView)((HomeActivity) context).findViewById(R.id.home_play_text)).setText("Err1");
                    } catch (ClassNotFoundException e) {
//                        ((TextView)((HomeActivity) context).findViewById(R.id.home_play_text)).setText("Err2");
                    } catch (Exception e) {
//                        ((TextView)((HomeActivity) context).findViewById(R.id.home_play_text)).setText("Err3");
                    }
                    try {
                        mSaveGameData = snapshot.getSnapshotContents().readFully();
                    } catch (IOException e) {
                    }
                }

                return result.getStatus().getStatusCode();
            }

            @Override
            protected void onPostExecute(Integer status) {
            }
        };

        task.execute();
    }

}
