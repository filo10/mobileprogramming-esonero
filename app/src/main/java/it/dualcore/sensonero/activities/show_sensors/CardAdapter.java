package it.dualcore.sensonero.activities.show_sensors;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.dualcore.sensonero.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.SensorInformationViewHolder>{

    List<SensorInfo> sensorInfo_list;
    public CardAdapter(List<SensorInfo> sensorInfo_list){
        this.sensorInfo_list = sensorInfo_list;
    }

    @NonNull
    @Override
    public SensorInformationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_showsensors, viewGroup, false);
        return new SensorInformationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorInformationViewHolder sensorInformationViewHolder, int i) {
        sensorInformationViewHolder.card_name.setText(sensorInfo_list.get(i).name);
        sensorInformationViewHolder.card_type.setText(sensorInfo_list.get(i).type);
        sensorInformationViewHolder.card_number.setText(sensorInfo_list.get(i).number);
    }

    @Override
    public int getItemCount() {
        return sensorInfo_list.size();
    }

    public static class SensorInformationViewHolder extends RecyclerView.ViewHolder{
        //CardView cardView;
        TextView card_name, card_type, card_number;

        SensorInformationViewHolder(View itemView){
            super(itemView);
            //cardView = (CardView) itemView.findViewById(R.id.cardview);
            card_name = itemView.findViewById(R.id.card_name);
            card_type = itemView.findViewById(R.id.card_type);
            card_number = itemView.findViewById(R.id.card_number);
        }
    }
}
