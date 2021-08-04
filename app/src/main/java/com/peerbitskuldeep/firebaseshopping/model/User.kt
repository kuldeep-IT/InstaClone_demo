package com.peerbitskuldeep.firebaseshopping.model

class User {

    private var userName: String = ""
    private var name: String = ""
    private var bio: String = ""
    private var image: String = ""
    private var uid: String = ""

    constructor()

    constructor(userName: String, name: String, bio: String, image: String, uid: String) {
        this.userName = userName
        this.name = name
        this.bio = bio
        this.image = image
        this.uid = uid
    }

    fun getUserName(): String
    {
        return userName
    }

    fun setUserName(userName: String)
    {
        this.userName = userName
    }


    fun getName(): String
    {
        return name
    }

    fun setName(name: String)
    {
        this.name = name
    }


    fun getBio(): String
    {
        return bio
    }

    fun setBio(bio: String)
    {
        this.bio = bio
    }


    fun getImage(): String
    {
        return image
    }

    fun setImage(image: String)
    {
        this.image = image
    }


    fun getUid(): String
    {
        return uid
    }

    fun setUid(uid: String)
    {
        this.uid = uid
    }
}