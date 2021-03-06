package weike.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import weike.adapter.GridAdapter;
import weike.shutuier.MainActivity;
import weike.shutuier.R;
import weike.util.Constants;
import weike.util.HttpManager;
import weike.util.HttpTask;
import weike.util.Utils;

/**
 * Created by Rth on 2015/3/24.
 */
public class MySellFragment extends Fragment {

    @InjectView(R.id.gridView)
    GridView gridView;
    @InjectView(R.id.rl_loading_personal)
    RelativeLayout rlLoading;
    @InjectView(R.id.tv_loading)
    TextView tvLoading;
    @InjectView(R.id.progressBar_loading)
    ProgressBar pbLoading;

    private static PersonalFragment.UpdateToolbar updateToolbar = null;
    private Handler han = null;
    private ArrayList<Map<String,String>> data = new ArrayList<>();
    private GridAdapter  adapter = null;
    private final String TAG = "MySellFragment";
    private final int REQUEST_CHANGE = 42;
    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.grid_books,container,false);
        initView(v);
        initData();
        return v;
    }

    private void initData() {
        //检查网络
        if(MainActivity.netConnect) {
            if(han == null) {
                initHandler();
            }
            SharedPreferences sp = context.getSharedPreferences(Constants.SP_USER,0);
            String uid = sp.getString(Constants.UID,"");
            try {
                rlLoading.setVisibility(View.VISIBLE);
                HttpTask task = new HttpTask(context, Utils.getMyCommitUrl(uid, Constants.TYPE_MYSELL),han,Constants.TYPE_MYSELL,null);
                HttpManager.startTask(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(context,"网络不可用",Toast.LENGTH_SHORT).show();
        }
    }

    private void initHandler() {
        han = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                rlLoading.setVisibility(View.INVISIBLE);
                if(msg.what == 0) {
                    ArrayList<Map<String,String>> list =  (ArrayList<Map<String,String>>)msg.obj;
                    data = (ArrayList<Map<String,String>>)list.clone();
                    list = null;
                    if(adapter == null) {
                        adapter = new GridAdapter(context,data);
                        gridView.setAdapter(adapter);
                    }
                    adapter.notifyDataSetChanged();
                    if(adapter.getCount() == 0) {
                        rlLoading.setVisibility(View.VISIBLE);
                        pbLoading.setVisibility(View.GONE);
                        tvLoading.setText("您还没有已出售的书籍");
                    }
                }
            }
        };
    }

    private void initView(View v) {
        ButterKnife.inject(this,v);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String preState = data.get(position).get("Status");
                HandleBookDialogFragment fragment = HandleBookDialogFragment.getInstance(data.get(position).get("ID"),position,preState);
                fragment.setTargetFragment(MySellFragment.this,REQUEST_CHANGE);
                fragment.show(getChildFragmentManager(), "action");
            }
        });
    }

    public static MySellFragment getInstance(PersonalFragment.UpdateToolbar listener,Context con) {
        context = con;
        updateToolbar = listener;
        return new MySellFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updateToolbar.changeTitle(5);
        context = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStackImmediate();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CHANGE && resultCode == Activity.RESULT_OK && data != null){
            int pos = data.getIntExtra(Constants.HANDLE_BOOK_POSITION,-1);
            int action = data.getIntExtra(Constants.HANDLE_BOOK_ACTION,-1);
            if(pos != -1 && action != -1) {
                updateData(pos,action);
            }
        }
    }

    private void updateData(int pos, int action) {
        if(action == 0) {
            data.get(pos).put("Status","1");
            adapter.notifyDataSetChanged();
        }else if(action == 1) {
            data.remove(pos);
            adapter.notifyDataSetChanged();
        }
    }
}
