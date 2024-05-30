package com.ts.connectingdot.helper.temp

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ts.connectingdot.data.remote.FirestoreCollection.userColl
import com.ts.connectingdot.domain.model.Gender
import com.ts.connectingdot.domain.model.User
import kotlinx.coroutines.tasks.await

object Scripts {

    val userList = listOf<User>(
        User(id = null, name = "Alice", profileImageUrl = null, email = "alice@example.com", bio = "Software Engineer", gender = Gender.Female, dob = "1990-05-10", fcmToken = null),
        User(id = null, name = "Bob", profileImageUrl = null, email = "bob@example.com", bio = "Product Manager", gender = Gender.Male, dob = "1985-08-15", fcmToken = null),
        User(id = null, name = "Charlie", profileImageUrl = null, email = "charlie@example.com", bio = "Graphic Designer", gender = Gender.Male, dob = "1992-03-22", fcmToken = null),
        User(id = null, name = "Diana", profileImageUrl = null, email = "diana@example.com", bio = "Data Scientist", gender = Gender.Female, dob = "1988-11-28", fcmToken = null),
        User(id = null, name = "Eva", profileImageUrl = null, email = "eva@example.com", bio = "Marketing Specialist", gender = Gender.Female, dob = "1995-07-03", fcmToken = null),
        User(id = null, name = "Frank", profileImageUrl = null, email = "frank@example.com", bio = "Software Developer", gender = Gender.Male, dob = "1993-01-18", fcmToken = null),
        User(id = null, name = "Grace", profileImageUrl = null, email = "grace@example.com", bio = "UI/UX Designer", gender = Gender.Female, dob = "1987-09-05", fcmToken = null),
        User(id = null, name = "Henry", profileImageUrl = null, email = "henry@example.com", bio = "Business Analyst", gender = Gender.Male, dob = "1991-06-12", fcmToken = null),
        User(id = null, name = "Ivy", profileImageUrl = null, email = "ivy@example.com", bio = "Content Writer", gender = Gender.Female, dob = "1986-04-25", fcmToken = null),
        User(id = null, name = "Jack", profileImageUrl = null, email = "jack@example.com", bio = "Sales Manager", gender = Gender.Male, dob = "1989-12-30", fcmToken = null)
    )
    suspend fun saveDummyUsers() {

        val collRef = Firebase.firestore.userColl()
        val batch = collRef.firestore.batch()


        userList.forEach {user ->
            batch.set(
                collRef.document(collRef.document().id),
                user
            )
        }

        batch.commit().await()

    }

}