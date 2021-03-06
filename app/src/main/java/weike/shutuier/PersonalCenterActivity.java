package weike.shutuier;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import weike.fragment.PersonalFragment;

/**
 * Created by Rth on 2015/3/23.
 */

public class PersonalCenterActivity extends ActionBarActivity implements PersonalFragment.UpdateToolbar{

    @InjectView(R.id.toolbar_personal)
    Toolbar toolbar;

    private final String TAG = "PersonalCenterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        initView();
    }

    private void initView() {
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("个人中心");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, PersonalFragment.getInstance(this,PersonalCenterActivity.this))
                .commit();
    }

    @Override
    public void changeTitle(int mode) {
        switch (mode) {
            case 0:
                toolbar.setTitle("我的出售");
                break;
            case 1:
                toolbar.setTitle("我的求购");
                break;
            case 2:
                toolbar.setTitle("我的赠送");
                break;
            case 3:
                toolbar.setTitle("我的求赠");
                break;
            case 4:
                toolbar.setTitle("基本信息");
                break;
            case 5:
                toolbar.setTitle("个人中心");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }else {
            return false;
        }
    }
}
