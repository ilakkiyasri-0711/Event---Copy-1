package com.arun.event.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.arun.event.db.model.Task;
import com.arun.event.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private static final String TAG = "TaskViewModel";
    private TaskRepository repository;
    private LiveData<List<Task>> listLiveData;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        listLiveData = repository.getAllDailyTask();
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public LiveData<List<Task>> getAllTasks() {
        return listLiveData;
    }
}
