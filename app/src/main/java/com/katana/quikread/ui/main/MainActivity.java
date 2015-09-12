package com.katana.quikread.ui.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.katana.quikread.ActivityScope;
import com.katana.quikread.R;
import com.katana.quikread.common.BaseActivity;
import com.katana.quikread.components.AppComponent;
import com.katana.quikread.rest.OnRequestFinishedListener;
import com.katana.quikread.rest.RestDataSource;
import com.katana.quikread.rest.models.BooksByLocationResponse;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Component;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity implements OnRequestFinishedListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    @ActivityScope
    @Component(
            dependencies = AppComponent.class
    )
    public interface MainComponent {
        void inject(MainActivity activity);
    }

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Inject
    RestDataSource restDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        restDataSource.setOnRequestFinishedListener(this);

        restDataSource.fetchBooksByLocation("Delhi"); //TODO: remove hardcoded location
    }

    @Override
    protected void setupComponent(AppComponent component) {

        DaggerMainActivity_MainComponent.builder()
                .appComponent(component)
                .build()
                .inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(Object response, Response retrofitResponse) {

        Log.v(TAG, response.toString());

        if(response instanceof BooksByLocationResponse){
            Log.i(TAG, String.valueOf(((BooksByLocationResponse)response).getTotal()));
        }
    }

    @Override
    public void onError(RetrofitError error) {

        Log.e(TAG, error.getMessage());
        Log.e(TAG, error.getLocalizedMessage());
    }

}