package com.atm.askthemaster.ui.master;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atm.askthemaster.CallingActivity;
import com.atm.askthemaster.R;
import com.atm.askthemaster.VideoChatActivity;
import com.atm.askthemaster.model.MasterCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SavedAskerAdapter extends RecyclerView.Adapter<SavedAskerAdapter.SavedNewsViewHolder> {

    private List<MasterCard> masterCards = new ArrayList<>();

    private DatabaseReference userRef;
    private String mAskerId;
    private String mMasterId;
    private String mQuestionId;

    private Button makeCall;

    @NonNull
    @Override
    public SavedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_card_item, parent, false);
        return new SavedNewsViewHolder(view);

    }
    public void setMasterCards(List<MasterCard> masterCards) {//sent to matchHelper/
        this.masterCards.clear();
        this.masterCards.addAll(masterCards);
        notifyDataSetChanged();
        Log.d("masterList","setMasterCards");
    }

    @Override
    public void onBindViewHolder(@NonNull SavedNewsViewHolder holder, int position) {
        MasterCard masterCard = masterCards.get(position);
        holder.masterId.setText(masterCard.getMasterId());
        holder.qTitle.setText(masterCard.getqTitle());
        holder.category.setText(masterCard.getCategory());
        mAskerId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        mMasterId = masterCard.getMasterUserId();
        userRef = FirebaseDatabase.getInstance().getReference();
        Query query = userRef
                .child("Users")
                .child(mAskerId)
                .child("askerQuestions")
                .orderByKey()
                .limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mQuestionId = dataSnapshot.getValue().toString();
                mQuestionId = mQuestionId.substring(0,mQuestionId.length()-1);
                String[] temp = mQuestionId.split("=");
                mQuestionId = temp[temp.length-1];
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("talkTest","askerId is" + mAskerId);
                Log.d("talkTest","masterId is" + mMasterId);
                Log.d("talkTest","questionId is" + mQuestionId);

                Intent intent = new Intent(v.getContext(), CallingActivity.class);
                intent.putExtra("curQuestionId", mQuestionId);
                intent.putExtra("visit_user_id",mMasterId);
                intent.putExtra("role","asker");

                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return masterCards.size();
    }


    public static class SavedNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView masterId;
        TextView qTitle;
        TextView category;
        //ImageView icon;//for the headshot.
        private Button callBtn;
        public SavedNewsViewHolder(View itemView) {
            super(itemView);
            masterId = itemView.findViewById(R.id.master_id);
            qTitle = itemView.findViewById(R.id.question_title);
            category = itemView.findViewById(R.id.question_category);
            callBtn = itemView.findViewById(R.id.question_accept_button);

            //icon = itemView.findViewById(R.id.image);
        }
        @Override
        public void onClick(View v) {

        }
    }
}
