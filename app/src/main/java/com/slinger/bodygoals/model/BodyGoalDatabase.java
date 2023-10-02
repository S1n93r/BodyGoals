package com.slinger.bodygoals.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class BodyGoalDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    
}