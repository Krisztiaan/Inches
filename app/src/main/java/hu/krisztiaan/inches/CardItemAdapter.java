package hu.krisztiaan.inches;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarDeterminate;
import com.gc.materialdesign.widgets.SnackBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krisz on 2015.07.21..
 */
public class CardItemAdapter extends BaseAdapter {
    List<Habit> items = new ArrayList<>(10);
    LayoutInflater mLayoutInflater;
    Activity mContext;

    public CardItemAdapter(Activity activity) {
        mContext = activity;
        mLayoutInflater = activity.getLayoutInflater();
    }

    public void addItem(Habit habit) {
        habit.save();
        items.add(habit);
        notifyDataSetChanged();
    }

    public void load(List<Habit> habits) {
        items = habits;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Habit getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Habit currentHabit = getItem(position);

        convertView = mLayoutInflater.inflate(R.layout.card_item, parent, false);
        final CardView cw = (CardView) convertView.findViewById(R.id.cardView);

        TextView txtName = (TextView) convertView.findViewById(R.id.cardText);
        txtName.setText(currentHabit.text);
        TextView txtLevel = (TextView) convertView.findViewById(R.id.cardLevel);
        txtLevel.setText(mContext.getString(R.string.level) + Integer.toString(XpParser.getLevel(currentHabit.xp)));

        TextView txtXp = (TextView) convertView.findViewById(R.id.cardXp);
        txtXp.setText(Integer.toString(currentHabit.xp) + mContext.getString(R.string.experience_short));

        ProgressBarDeterminate progressBar = (ProgressBarDeterminate) convertView.findViewById(R.id.cardProgressBar);
        progressBar.setMax(XpParser.XP_PER_LEVEL);
        progressBar.setProgress(XpParser.getLevelProgress(currentHabit.xp));

        if (currentHabit.isChecked() && currentHabit.xp != 0) {
            if (currentHabit.isLeveledUp()) {
                cw.setCardBackgroundColor(mContext.getResources().getColor(R.color.level_up));
            } else {
                cw.setCardBackgroundColor(mContext.getResources().getColor(R.color.done));
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentHabit.progress();
                if (currentHabit.isChecked()) {
                    if (currentHabit.isLeveledUp()) {
                        cw.setCardBackgroundColor(mContext.getResources().getColor(R.color.level_up));
                    } else {
                        cw.setCardBackgroundColor(mContext.getResources().getColor(R.color.done));
                    }
                }
                notifyDataSetChanged();
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SnackBar snackbar = new SnackBar(mContext, mContext.getString(R.string.delete_begin)
                        + currentHabit.text
                        + mContext.getString(R.string.delete_end),
                        mContext.getString(R.string.yes),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                items.remove(position);
                                currentHabit.delete();
                                notifyDataSetChanged();
                            }
                        });
                snackbar.show();
                return true;
            }
        });
        return convertView;
    }
}
