package com.integrity.feshar.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.integrity.feshar.dao.TvShowDao;
import com.integrity.feshar.models.TvShow;

@Database(entities = TvShow.class, version = 1, exportSchema = false)
public abstract class TvShowDatabase extends RoomDatabase {

    private static TvShowDatabase instance;

    public static synchronized TvShowDatabase getInstance(Context mContext){
        if (instance == null) {
            instance = Room.databaseBuilder(
                    mContext,
                    TvShowDatabase.class,
                    "tv_show_db"
            ).build();
        }

        return instance;
    }

    public abstract TvShowDao tvShowDao();

}
