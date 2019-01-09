package com.kevin.waitless;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

@Entity(tableName = "USER")
public class Client {

    @PrimaryKey(autoGenerate = true)
    private long user_id;
    @ColumnInfo(name = "email")
    private static SystemID email;
    @ColumnInfo(name = "firstname")
    private static String firstname;
    @ColumnInfo(name = "surname")
    private static String surname;

    public Client() {}


    public SystemID getEmail() {
        return email;
    }

    public void setEmail(SystemID email) {
        Client.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        Client.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        Client.surname = surname;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}

@Dao
interface ClientDao{
    @Insert
    public void insertClient(Client client);
    @Update
    public void updateClient(Client client);
    @Delete
    public void deleteClient(Client client);
    @Query("SELECT * FROM USER")
    public Client getClient();
}