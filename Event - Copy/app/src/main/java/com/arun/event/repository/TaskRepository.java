package com.arun.event.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.arun.event.db.model.Dept;
import com.arun.event.db.model.Session;
import com.arun.event.db.model.Task;
import com.arun.event.db.TaskDao;
import com.arun.event.db.TaskDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskRepository {

    static Context context;

    private TaskDao taskDao;
    private MutableLiveData<List<Dept>> listDept = new MutableLiveData<>();
    private MutableLiveData<List<Session>> listSession = new MutableLiveData<>();
    private LiveData<List<Task>> listLiveData;
    private LiveData<List<Task>> listLiveDataSpec;

    public TaskRepository(Application application) {
        context = application;
        TaskDatabase database = TaskDatabase.getInstance(context);
        taskDao = database.getTaskDao();
        listLiveData = taskDao.getTask();
    }

    public List<Dept> getDept() {
        List<Dept> list = new ArrayList<Dept>();
        list.add(new Dept(1, "BCA"));
        list.add(new Dept(2, "BSC"));
        list.add(new Dept(3, "MSC"));
        return list;
    }

    public List<Session> getSession() {
        List<Session> list = new ArrayList<Session>();
        list.add(new Session(1, "Session-1"));
        list.add(new Session(2, "Session-2"));
        return list;
    }

    public LiveData<List<Task>> getAllDailyTask() {
        return listLiveData;
    }

    public LiveData<List<Task>> getAllTaskSpec(Date search) {
        listLiveData = taskDao.getSpecTask(search);
        return listLiveData;
    }

    public LiveData<Integer> getAllTaskSpecCount(Date search) {
        //getTaskAsync taskAsync = new getTaskAsync(taskDao);
        //taskAsync.execute(search);
        return taskDao.getSpecTaskCount(search);
    }

    public LiveData<Integer> getSpecSessionCount(Date dt, int session) {
        return taskDao.getSpecSessionCount(dt, session);
    }

    public LiveData<Integer> hasSession(Date dt) {
        return taskDao.hasSession(dt);
    }

    public Integer getTaskSpecCountValue(Date search) {
        return taskDao.getSpecTaskCountValue(search);
    }

    public void insert(Task task) {
        new insertTaskAsync(taskDao).execute(task);
    }

    public void update(Task task) {
        new updateTaskAsync(taskDao).execute(task);
    }

    public void delete(Task task) {
        new deleteTaskAsync(taskDao).execute(task);
    }

    private static class insertTaskAsync extends AsyncTask<Task, Void, Long> {

        private TaskDao taskDao;

        public insertTaskAsync(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Long doInBackground(Task... tasks) {
            return taskDao.insert(tasks[0]);
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            Toast.makeText(TaskRepository.context, "Task added", Toast.LENGTH_SHORT).show();
        }
    }

    private static class updateTaskAsync extends AsyncTask<Task, Void, Integer> {

        private TaskDao taskDao;

        public updateTaskAsync(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Integer doInBackground(Task... tasks) {
            return taskDao.update(tasks[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Toast.makeText(TaskRepository.context, "Updated" , Toast.LENGTH_SHORT).show();
        }
    }

    private static class deleteTaskAsync extends AsyncTask<Task, Void, Integer> {

        private TaskDao taskDao;

        public deleteTaskAsync(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

      @Override
        protected Integer doInBackground(Task... tasks) {
            return taskDao.delete(tasks[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Toast.makeText(TaskRepository.context, "Deleted " + result, Toast.LENGTH_SHORT).show();
        }
    }

    private static class getTaskAsync extends AsyncTask<Date, Integer, Integer> {

        private TaskDao taskDao;
        private int eventCount = 0;

        public getTaskAsync(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

      @Override
        protected Integer doInBackground(Date... date) {
           // return taskDao.getSpecTaskCount(date[0]);
          return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            eventCount = result;
        }
    }


}
