package org.study.arrow

import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.Response
import org.glassfish.jersey.client.*
import org.glassfish.jersey.client.JerseyWebTarget
import org.glassfish.jersey.client.JerseyClientBuilder;

class JerseyPatterns {
}

fun main(args : Array<String>) {
    val client = JerseyClientBuilder.newClient();
    val target = client.target("https://node.readthedocs.io/en/latest/api/https/");
    val response = target.request().get();
    val queryRespose:String = response.readEntity(String::class.java);
    println(queryRespose)
}