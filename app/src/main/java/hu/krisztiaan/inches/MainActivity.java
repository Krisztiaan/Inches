package hu.krisztiaan.inches;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.Slider;
import com.melnykov.fab.FloatingActionButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName();
    @Bind(R.id.listView)
    ListView mListView;
    @Bind(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    CardItemAdapter mCardItemAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onPause trying backup if 0 items. items: " + mCardItemAdapter.getCount());
        if(mCardItemAdapter.getCount()==0) {
            PersistenceManager.importDB();
            mCardItemAdapter.notifyDataSetChanged();
            Crouton.makeText(this, "Loaded from backup.", Style.INFO).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause trying backup if not 0 items. items: " + mCardItemAdapter.getCount());
        if(mCardItemAdapter.getCount()!=0) PersistenceManager.exportDB();
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.BOTTOM)
                .setContentHolder(new ViewHolder(R.layout.dialog_add))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        View saveButton = findViewById(R.id.buttonSave);
                        View clearButton = findViewById(R.id.buttonClear);
                        view.requestFocus();
                        if (view.equals(saveButton)) {
                            addItem(((TextView) findViewById(R.id.textInput)).getText().toString(),
                                    ((Slider) findViewById(R.id.slider)).getValue());
                            Log.i(TAG, "onClick input: " + ((TextView) findViewById(R.id.textInput)).getText().toString());
                            dialog.dismiss();
                        } else if (view.equals(clearButton)) {
                            Habit.deleteAll(Habit.class);
                            mCardItemAdapter.load(PersistenceManager.getHabits());
                            dialog.dismiss();
                        }
                    }
                })
                .setExpanded(true)
                .create();
        dialog.show();
        findViewById(R.id.textInput).requestFocus();
    }

    protected void addItem(String text, int exp) {
        Habit addedHabit = new Habit();
        addedHabit.text = text;
        addedHabit.xp = exp;
        mCardItemAdapter.addItem(addedHabit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PersistenceManager.init(getApplicationContext());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFloatingActionButton.attachToListView(mListView);
        mCardItemAdapter = new CardItemAdapter(this);
        mListView.setAdapter(mCardItemAdapter);
        mCardItemAdapter.load(PersistenceManager.getHabits());
    }
}
