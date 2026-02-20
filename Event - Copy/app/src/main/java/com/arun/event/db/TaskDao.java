package com.arun.event.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.arun.event.db.model.Task;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    public long insert(Task task);

    @Update
    int update(Task task);

    @Query("select * from task")
    public LiveData<List<Task>> getTask();

    @Query("select * from task where date in (:search)")
    public LiveData<List<Task>> getSpecTask(Date search);

    @Query("select count(*) from task where date in (:search)")
    public LiveData<Integer> getSpecTaskCount(Date search);

    @Query("select count(*) from task where date in (:dt) and session in (:session)")
    public LiveData<Integer> getSpecSessionCount(Date dt, int session);

    @Query("select count(*) from task where date in (:dt) and (session in (0) or session in (1))")
    public LiveData<Integer> hasSession(Date dt);

    @Query("select count(*) from task where date in (:search)")
    public Integer getSpecTaskCountValue(Date search);

    @Delete
    int delete(Task task);

}
