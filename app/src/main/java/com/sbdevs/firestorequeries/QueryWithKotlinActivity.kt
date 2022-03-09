package com.sbdevs.firestorequeries

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QueryWithKotlinActivity : AppCompatActivity() {
    private var firebaseFirestore = Firebase.firestore
    private lateinit var textView: TextView
    private var lastResult: DocumentSnapshot? = null
    private lateinit var loadProductBtn: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_with_kotlin)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //initialising the button and text view
        loadProductBtn = findViewById(R.id.button_load_product)
        textView = findViewById(R.id.textView)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)


        //set click listener on button
        loadProductBtn.setOnClickListener {
            Log.e("Load Button", "CLICKED")
            progressBar.visibility = View.VISIBLE


            loadProduct()
        }
    }
    private fun loadProduct() {

        val query: Query = if (lastResult == null) {
            firebaseFirestore.collection("PRODUCTS")
                .orderBy("price", Query.Direction.ASCENDING)
                .limit(10)
        } else {
            firebaseFirestore.collection("PRODUCTS")
                .orderBy("price", Query.Direction.ASCENDING)
                .startAfter(lastResult)
                .limit(10)
        }
        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                var data = ""
                val length = queryDocumentSnapshots.documents.size

                for (documentSnapshot in queryDocumentSnapshots) {
                    val productId = documentSnapshot.id
                    val productName = documentSnapshot.getString("title").toString()
                    val price = documentSnapshot.getLong("price")!!.toLong()

                    data += """
                ID: $productId
                Title: $productName
                price: $price
                Query size: $length
                
                
                """.trimIndent()
                }


                if (queryDocumentSnapshots.size() > 0) {
                    data += "___________\n\n"
                    textView.append(data)
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                    Log.e("Query Size: ", length.toString())
                }
                progressBar.visibility = View.INVISIBLE
            }.addOnFailureListener {
                Log.e("Product load failed:", "${it.message}")
                progressBar.visibility = View.INVISIBLE
            }
    }

}