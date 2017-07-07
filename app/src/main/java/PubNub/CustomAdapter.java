package PubNub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import app.simone.GameActivity;
import app.simone.R;
import com.facebook.Profile;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Giacomo on 03/07/2017.
 */

public class CustomAdapter extends ArrayAdapter<OnlinePlayer> implements View.OnClickListener {

    private ArrayList<OnlinePlayer> data;
    Context mContext;

    public CustomAdapter(ArrayList<OnlinePlayer> data, Context context) {
        super(context, R.layout.row_item, data);
        this.data=data;
        this.mContext=context;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        Button info;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        OnlinePlayer dataModel=(OnlinePlayer)object;

        switch (v.getId())
        {

            case R.id.item_info:

                Intent intent = new Intent(mContext,GameActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",Profile.getCurrentProfile().getId().toString());
                intent.putExtra("firstname",Profile.getCurrentProfile().getFirstName().toString());
                intent.putExtra("surname",Profile.getCurrentProfile().getLastName().toString());

                mContext.startActivity(intent);
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        OnlinePlayer dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.info = (Button) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;


        viewHolder.txtName.setText(dataModel.getId());
        viewHolder.txtType.setText(dataModel.getName());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
