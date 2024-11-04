package com.example.mob3000

import android.util.Log
import com.example.mob3000.FirebaseService.leggTilBruker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.mob3000.Person

object FirebaseService {
    private val firestore = FirebaseFirestore.getInstance()

    fun hentPersoner(onSuccess: (List <Person>) -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        firestore.collection("Personer")
            .whereEqualTo("userId", userId)
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
        val currentUser = FirebaseAuth.getInstance().currentUser
        val personMedUserId = person.copy(userId = currentUser?.uid ?: "")

        firestore.collection("Personer")
            .add(personMedUserId)
            .addOnSuccessListener{
                    documentReferanse ->
                val autoId = documentReferanse.id
                onSuccess(autoId)}
            .addOnFailureListener{exception -> onFailure(exception)}
    }
    fun leggTilBruker(bruker: Bruker, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit ) {
        firestore.collection("Brukere")
            .add(bruker)
            .addOnSuccessListener{documentReference ->
                val autoId = documentReference.id
                onSuccess(autoId)
            }



            .addOnFailureListener{exception -> onFailure(exception)}
    }
}



object AuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registrerBruker(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if(email.isEmpty() || password.length <6) {
            onFailure("Ikke godtatt epost eller passord. Passord må være minst 6 tegn langt.")
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    if(user!= null) {
                        val bruker = Bruker(id = user.uid, email = user.email ?: "")

                        FirebaseService.leggTilBruker(bruker,
                            onSuccess= { onSuccess()},
                            onFailure = {exception ->
                                onFailure("Feilet med å legge til bruker i FireStore: ${exception.localizedMessage}")
                            }
                        )
                    } else {
                        onFailure("User registrering suksess, men brukerdata er null")
                    }
                } else {
                    onFailure(task.exception?.localizedMessage?: "Registrering feilet.")
                }
            }
    }
    fun logginnBruker(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.localizedMessage ?: "Logg inn feilet.")
                }
            }
    }
    fun loggUt(    ) {
        auth.signOut()
    }
}

/*
    fun registrerBruker (email: String, password: String, onSuccess: (FirebaseUser?) -> Unit, onFailure: (Exception) -> Unit){
        auth.createUserWithEmailAndPassword(email.trim(), password.trim())
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    onFailure(task.exception ?: Exception("Registrering feilet"))
                    Log.d("registrerBruker", "Reigstrering feilet: ${task.exception?.localizedMessage}")
                }
            }
    }

    fun logginnBruker(email: String, password: String, onSuccess: (FirebaseUser?) -> Unit, onFailure: (Exception) -> Unit){
        auth.signInWithEmailAndPassword(email.trim(), password.trim())
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    Log.e("logginnBruker", "Logg inn feilet: ${task.exception?.localizedMessage}")
                    onFailure(task.exception ?: Exception("Logg inn feilet."))
                }
            }
    }*/