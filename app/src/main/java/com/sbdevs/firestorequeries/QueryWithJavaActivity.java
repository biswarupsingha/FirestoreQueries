package com.sbdevs.firestorequeries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class QueryWithJavaActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private TextView textView;
    private DocumentSnapshot lastResult;
    private Button loadProductBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_with_java);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //initialising the button and text view
        loadProductBtn = findViewById(R.id.button_load_product);
        textView = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);

        //set click listener on button
        loadProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Load Button", "CLICKED");
                progressBar.setVisibility(View.VISIBLE);

                LoadProduct();
            }
        });

    }

    private void LoadProduct() {
        Query query;
        if (lastResult == null) {
            query = firebaseFirestore.collection("PRODUCTS")
                    .orderBy("price", Query.Direction.ASCENDING)
                    .limit(10);
        } else {
            query = firebaseFirestore.collection("PRODUCTS")
                    .orderBy("price", Query.Direction.ASCENDING)
                    .startAfter(lastResult)
                    .limit(10);
        }

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            String data = "";
                            QuerySnapshot queryDocumentSnapshots = task.getResult();

                            int length = queryDocumentSnapshots.getDocuments().size();

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String productId = documentSnapshot.getId();
                                String productName = documentSnapshot.getString("title").toString();
                                String price = documentSnapshot.getLong("price").toString();


                                data += "ID: " + productId
                                        + "\nTitle: " + productName + "\nprice: " + price
                                        + "\nQuery size: " + length + "\n\n";
                            }

                            if (queryDocumentSnapshots.size() > 0) {
                                data += "___________\n\n";
                                textView.append(data);

                                lastResult = queryDocumentSnapshots.getDocuments()
                                        .get(queryDocumentSnapshots.size() - 1);
                                Log.e("Query Size: ", String.valueOf(length));
                            }
                            progressBar.setVisibility(View.INVISIBLE);

                        }else {
                            Log.e("Product load failed:", "${it.message}");
                            progressBar.setVisibility(View.INVISIBLE);
                        }



                    }
                });
    }

}