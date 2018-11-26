package com.example.kmoue.breakingnews.sync;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;
import android.content.Context;
import android.os.AsyncTask;

public class BreakingNewsFirebaseJobService  extends JobService {

    private AsyncTask<Void, Void, Void> mFetchNewsTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mFetchNewsTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                BreakingNewsSyncTask.syncNews(context);
                jobFinished(jobParameters, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                 jobFinished(jobParameters,false);
            }
        };
        mFetchNewsTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(mFetchNewsTask != null){
            mFetchNewsTask.cancel(true);
        }
        return true;
    }
}
