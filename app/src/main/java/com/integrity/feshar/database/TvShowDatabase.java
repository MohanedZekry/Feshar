package com.integrity.feshar.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.integrity.feshar.dao.TvShowDao;
import com.integrity.feshar.models.TvShow;

import static com.integrity.feshar.utilities.Constants.DATABASE_NAME;

@Database(entities = TvShow.class, version = 1, exportSchema = false)
public abstract class TvShowDatabase extends RoomDatabase {

    private static TvShowDatabase instance;

    public synchronized static TvShowDatabase getInstance(Context mContext){
        if (instance == null) {
            instance = Room.databaseBuilder(
                    mContext,
                    TvShowDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
    //create Dao
    public abstract TvShowDao tvShowDao();

}
