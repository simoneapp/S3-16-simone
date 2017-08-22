package app.simone.multiplayer.view.invites;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Profile;

import java.util.ArrayList;

import app.simone.R;
import app.simone.multiplayer.model.FacebookUser;
import app.simone.multiplayer.model.OnlineMatch;
import app.simone.singleplayer.view.GameActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Giacomo on 03/07/2017.
 */

public class PendingRequestsAdapter extends ArrayAdapter<OnlineMatch> implements View.OnClickListener {

    private ArrayList<OnlineMatch> data;
    private Context mContext;
    private OnlineMatch dataModel;

    public PendingRequestsAdapter(ArrayList<OnlineMatch> data, Context context) {
        super(context, R.layout.row_item, data);
        this.data=data;
        this.mContext=context;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView textPlayer1;
        TextView textPlayer2;
        TextView scoreP1;
        TextView scoreP2;
        Button playButton;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        OnlineMatch dataModel=(OnlineMatch)object;


        switch (v.getId())
        {

            case R.id.item_info:

                Intent intent = new Intent(mContext,GameActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("multiplayerMode", "multiplayerMode");
                intent.putExtra("key",dataModel.getKey());
                intent.putExtra("whichPlayer","secondplayer");

                mContext.startActivity(intent);
                break;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        this.dataModel = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.textPlayer1 = (TextView) convertView.findViewById(R.id.p1);
            viewHolder.textPlayer2 = (TextView) convertView.findViewById(R.id.p2);
            viewHolder.scoreP1 = (TextView) convertView.findViewById(R.id.scoreP1);
            viewHolder.scoreP2 = (TextView) convertView.findViewById(R.id.scoreP2);
            viewHolder.playButton = (Button) convertView.findViewById(R.id.item_info);

        updateCellText(viewHolder,position);

        boolean myBool = disablePlayButton(dataModel, viewHolder);

       if(myBool){
            viewHolder.playButton.setEnabled(false);
        }

        return convertView;
    }

    private void updateCellText(ViewHolder viewHolder,int position){

        FacebookUser first = dataModel.getFirstplayer();
        FacebookUser second = dataModel.getSecondplayer();
        viewHolder.textPlayer1.setText(first.getName());
        viewHolder.textPlayer2.setText(second.getName());
        viewHolder.scoreP1.setText(first.getScore());
        viewHolder.scoreP2.setText(second.getScore());
        viewHolder.playButton.setOnClickListener(this);
        viewHolder.playButton.setTag(position);

    }

    private boolean disablePlayButton(OnlineMatch dataModel,ViewHolder viewHolder) {


        String playerID = Profile.getCurrentProfile().getId();

       if(dataModel.getFirstplayer().getId().equals(playerID)){
           if(viewHolder.scoreP1.getText().equals(""))
               return false;
       }
       if(dataModel.getSecondplayer().getId().equals(playerID)) {
           if (viewHolder.scoreP2.getText().equals(""))
               return false;
       }
       return true;
    }
}
