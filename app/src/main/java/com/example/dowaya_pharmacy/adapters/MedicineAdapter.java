package com.example.dowaya_pharmacy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dowaya_pharmacy.R;
import com.example.dowaya_pharmacy.StaticClass;
import com.example.dowaya_pharmacy.activities.core.RequestDescriptionActivity;
import com.example.dowaya_pharmacy.models.Medicine;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.example.dowaya_pharmacy.StaticClass.medicineList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private List<Medicine> list, copyList=new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private DocumentReference storeReference;

    public MedicineAdapter(Context context, List<Medicine> list,
                           DocumentReference storeReference) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        copyList.addAll(list);
        this.storeReference = storeReference;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.medicine_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTV.setText(list.get(position).getName());
        holder.checkbox.setChecked(list.get(position).isAvailable());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTV;
        CheckBox checkbox;
        FirebaseFirestore database;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            nameTV = view.findViewById(R.id.nameTV);
            checkbox = view.findViewById(R.id.checkbox);

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        Toast.makeText(view.getContext(),
                                "checked",
                                Toast.LENGTH_SHORT).show();
                        database.collection("medicines-stores")
                                .document(list.get(getAdapterPosition()).getId())
                                .update("stores",
                                        FieldValue.arrayUnion(storeReference))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(view.getContext(),
                                                "medicine store successfully written!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(view.getContext(),
                                                "Error writing medicine store",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        Toast.makeText(view.getContext(),
                                "unchecked",
                                Toast.LENGTH_SHORT).show();
                        database.collection("medicines-stores")
                                .document(list.get(getAdapterPosition()).getId())
                                .update("stores",
                                        FieldValue.arrayRemove(storeReference))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(view.getContext(),
                                                "medicine store successfully removed!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(view.getContext(),
                                                "Error removing medicine store",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
            database = FirebaseFirestore.getInstance();

            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Medicine getItem(int id) {
        return list.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public void filter(String queryText) {
        medicineList.clear();
        if(queryText.isEmpty()) {
            medicineList.addAll(copyList);
        }else{
            for(Medicine medicine: copyList) {
                if(medicine.getName().toLowerCase().contains(queryText.toLowerCase())) {
                    medicineList.add(medicine);
                }
            }
        }
        notifyDataSetChanged();
    }
}
