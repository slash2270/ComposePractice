package com.example.composepractice.data

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

data class Item(val name: String, val resId: Int) { var enable: Boolean by mutableStateOf(false) }
data class AnimationSize(val color: Long, val size: Float)
data class Person(val name: String, val age: Int)
data class Message(val author: String, val body: String)
data class Elevations(val card: Dp = 0.dp, val default: Dp = 0.dp)
data class ButtonState(var text: String, var textColor: Color, var buttonColor: Color)
data class MemberItemData(var imageID: Int, var text:String)
data class MemberItemIdData(var id: Int, var imageID: Int, var text:String)
@Serializable
data class DataSaverBean(var id: Int, val label: String)

object SampleData {
    val conversationSample = listOf(
        Message(
            "Colleague",
            "Test...Test...Test..."
        ),
        Message(
            "Colleague",
            "List of Android versions:\n" +
                    "Android KitKat (API 19)\n" +
                    "Android Lollipop (API 21)\n" +
                    "Android Marshmallow (API 23)\n" +
                    "Android Nougat (API 24)\n" +
                    "Android Oreo (API 26)\n" +
                    "Android Pie (API 28)\n" +
                    "Android 10 (API 29)\n" +
                    "Android 11 (API 30)\n" +
                    "Android 12 (API 31)\n"
        ),
        Message(
            "Colleague",
            "I think Kotlin is my favorite programming language.\n" +
                    "It's so much fun!"
        ),
        Message(
            "Colleague",
            "Searching for alternatives to XML layouts..."
        ),
        Message(
            "Colleague",
            "Hey, take a look at Jetpack Compose, it's great!\n" +
                    "It's the Android's modern toolkit for building native UI." +
                    "It simplifies and accelerates UI development on Android." +
                    "Less code, powerful tools, and intuitive Kotlin APIs :)"
        ),
        Message(
            "Colleague",
            "It's available from API 21+ :)"
        ),
        Message(
            "Colleague",
            "Writing Kotlin for UI seems so natural, Compose where have you been all my life?"
        ),
        Message(
            "Colleague",
            "Android Studio next version's name is Arctic Fox"
        ),
        Message(
            "Colleague",
            "Android Studio Arctic Fox tooling for Compose is top notch ^_^"
        ),
        Message(
            "Colleague",
            "I didn't know you can now run the emulator directly from Android Studio"
        ),
        Message(
            "Colleague",
            "Compose Previews are great to check quickly how a composable layout looks like"
        ),
        Message(
            "Colleague",
            "Previews are also interactive after enabling the experimental setting"
        ),
        Message(
            "Colleague",
            "Have you tried writing build.gradle with KTS?"
        ),
    )
}

class DataSaverParcelable(val name: String?, val age: Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(age)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString() = "I'm $name, my age is $age"


    companion object CREATOR : Parcelable.Creator<DataSaverParcelable> {
        override fun createFromParcel(parcel: Parcel): DataSaverParcelable {
            return DataSaverParcelable(parcel)
        }

        override fun newArray(size: Int): Array<DataSaverParcelable?> {
            return arrayOfNulls(size)
        }
    }

    fun copy(name: String? = this.name, age: Int = this.age) = DataSaverParcelable(name, age)
}