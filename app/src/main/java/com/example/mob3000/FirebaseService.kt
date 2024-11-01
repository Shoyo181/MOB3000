package com.example.mob3000

import com.google.firebase.firestore.FirebaseFirestore
import com.example.mob3000.Person

object FirebaseService {
    private val firestore = FirebaseFirestore.getInstance()

    fun hentPersoner(onSuccess: (List <Person>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("Personer")
            .addSnapshotListener{ snapshot, error ->
                if(error != null) {
                    onFailure(error)
                    return@addSnapshotListener
                }
                if(snapshot != null && !snapshot.isEmpty) {
                    val personList = snapshot.toObjects(Person::class.java)
                    onSuccess(personList)
                } else {
                    onSuccess(emptyList())
                }
            }
    }
    fun leggTilPerson (person: Person, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("Personer")
            .add(person)
            .addOnSuccessListener{
                    documentReferanse ->
                val autoId = documentReferanse.id
                onSuccess(autoId)}
            .addOnFailureListener{exception -> onFailure(exception)}
    }
}
