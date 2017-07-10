package PubNub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.simone.DataModel.OnlineMatch;
import app.simone.GameActivity;
import app.simone.R;
import com.facebook.Profile;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Giacomo on 03/07/2017.
 */

public class CustomAdapter extends ArrayAdapter<OnlineMatch> implements View.OnClickListener {

    private ArrayList<OnlineMatch> data;
    Context mContext;

    public CustomAdapter(ArrayList<OnlineMatch> data, Context context) {
        super(context, R.layout.row_item, data);
        this.data=data;
        this.mContext=context;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView textPlayer1;
        TextView textPlayer2;
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
                intent.putExtra("multiplayerMode","multiplayerMode");
                intent.putExtra("id",Profile.getCurrentProfile().getId().toString());
                intent.putExtra("firstname",Profile.getCurrentProfile().getFirstName().toString());
                intent.putExtra("surname",Profile.getCurrentProfile().getLastName().toString());

                //dati di chi invia la richiesta
               /* OnlinePlayer op = new OnlinePlayer(dataModel.getIdP1(),dataModel.getNameP1(),"");
                Toast.makeText(getContext(),op.getId()+" "+op.getName()+" "+op.getSurname(),Toast.LENGTH_SHORT).show();

                intent.putExtra("idTo",op.getId());
                intent.putExtra("nameTo",op.getName());*/

                OnlinePlayer toPlayer = new OnlinePlayer(Profile.getCurrentProfile().getId().toString(),Profile.getCurrentProfile().getFirstName().toString(),Profile.getCurrentProfile().getLastName().toString());
                OnlinePlayer player = new OnlinePlayer(dataModel.getIdP1(),dataModel.getNameP1(),"");
                player.setScore(dataModel.getScoreP1());
                toPlayer.setScore(dataModel.getScoreP2());
                intent.putExtra("player", player);
                intent.putExtra("toPlayer", toPlayer);


                mContext.startActivity(intent);
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        OnlineMatch dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.textPlayer1 = (TextView) convertView.findViewById(R.id.p1);
            viewHolder.textPlayer2 = (TextView) convertView.findViewById(R.id.p2);
            viewHolder.playButton = (Button) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;


        viewHolder.textPlayer1.setText(dataModel.getNameP1()+" - Score: "+dataModel.getScoreP1());
        viewHolder.textPlayer2.setText(dataModel.getNameP2()+" - Score: "+dataModel.getScoreP2());
        viewHolder.playButton.setOnClickListener(this);
        viewHolder.playButton.setTag(position);
        return convertView;
    }
}
