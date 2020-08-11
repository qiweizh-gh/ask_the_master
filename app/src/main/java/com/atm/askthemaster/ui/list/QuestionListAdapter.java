package com.atm.askthemaster.ui.list;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atm.askthemaster.R;
import com.atm.askthemaster.model.Category;
import com.atm.askthemaster.model.Question;
import com.atm.askthemaster.util.MatchHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.QuestionListViewHolder>{
    private List<Question> questions = new ArrayList<>();
    private FirebaseAuth mAuth;


    public void setQuestions(List<Question> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public QuestionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list_item,parent,false);
        return new QuestionListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull QuestionListViewHolder holder, int position) {

        Question question = questions.get(position);
        Category category = question.getCategories().get(0);

        holder.question_title.setText(question.getTitle());
        holder.question_content.setText(question.getContent());
        holder.estimated_time.setText(question.getDuration() + " min");
        holder.question_created_time.setText(String.valueOf(question.getCreateTime()));
        holder.category.setText(category.getName());
        holder.myMasterAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchHelper matchHelper = new MatchHelper();
                matchHelper.notifyAsker(question.getQuestionKey(), mAuth.getInstance().getCurrentUser().getUid(), v.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        //return 0;
        return questions.size();
    }



    public static class QuestionListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView question_title;
        TextView question_content;
        TextView question_created_time;
        TextView estimated_time;
        TextView category;
        Button myMasterAcceptBtn;

        public QuestionListViewHolder(View itemView) {
            super(itemView);
            question_title = itemView.findViewById(R.id.question_title);
            question_content = itemView.findViewById(R.id.question_content);
            question_created_time = itemView.findViewById(R.id.question_created_time);
            estimated_time = itemView.findViewById(R.id.estimated_time);
            category = itemView.findViewById(R.id.question_category);
            myMasterAcceptBtn = itemView.findViewById(R.id.question_accept_button);
        }

        @Override
        public void onClick(View v) {

        }
    }
}