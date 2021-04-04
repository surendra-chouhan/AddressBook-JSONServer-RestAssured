package com.addressbook;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AddressBookDataJSONTest {

    @Before
    public void set() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4000;
    }

    public AddressBookData[] getContactList(){
        Response response = RestAssured.get("/contacts");
        System.out.println("Contacts in JSON are : " + response.asString());
        AddressBookData[] restAssuredBookData = new Gson().fromJson(response.asString(),AddressBookData[].class);
        return restAssuredBookData;
    }

    public Response addContactstoJSONServer(AddressBookData restAssuredBookData){
        String contact = new Gson().toJson(restAssuredBookData);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        requestSpecification.body(contact);
        return requestSpecification.post("/contacts");
    }

    @Test
    public void givenContactsShouldReturnContactList() {
        AddressBookData[] restAssuredBookData = getContactList();
        System.out.println(restAssuredBookData);
        Assert.assertEquals(5, restAssuredBookData.length);
    }
}
