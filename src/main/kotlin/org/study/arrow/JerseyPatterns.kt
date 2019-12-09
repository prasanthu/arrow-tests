package org.study.arrow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.Response
import org.glassfish.jersey.client.*
import org.glassfish.jersey.client.JerseyWebTarget
import org.glassfish.jersey.client.JerseyClientBuilder;

class JerseyPatterns {
    suspend fun downloadFileByAsync(path: String): Deferred<String> = withContext(Dispatchers.IO) {
        async {
            val result = downloadHelper(path)
            println("Finished downloadFileByAsync withContext(Dispatchers.IO) $path")
            result
        }
    }

    suspend fun downloadFileByLaunch(path: String) = withContext(Dispatchers.IO) {
        launch {
            downloadHelper(path)
            println("Finished downloadFileByLaunch withContext(Dispatchers.IO) $path")
        }
    }

    suspend fun downloadPlainAsync(path: String) {
        coroutineScope {
            async {
                val result = downloadHelper(path)
                println("Finished downloadPlainAsync new coroutineScope $path")
                result
            }
        }
    }

    companion object {
        fun downloadHelper(path: String): String {
            val client = JerseyClientBuilder.newClient()
            val target = client.target(path)
            val response = target.request().get()
            val queryRespose: String = response.readEntity(String::class.java)
            return queryRespose
        }
    }
}

fun CoroutineScope.downloadScopeAsync(path: String) = async {
    val result = JerseyPatterns.downloadHelper(path)
    println("Finished CoroutineScope.downloadScopeAsync $path")
    result
}

fun main(args : Array<String>)  = runBlocking{
    val patterns = JerseyPatterns()
    val uri = "https://node.readthedocs.io/en/latest/api/https/";
    val uri2  = "https://node.readthedocs.io/en/latest/api/child_process/"
    val uri3 = "https://node.readthedocs.io/en/latest/api/debugger/"

    println("Start downloadScopeAsync $uri3")
    val data5 = downloadScopeAsync(uri3)

    println("Start downloadFileByAsync $uri")
    val response = patterns.downloadFileByAsync(uri)

    println("Start downloadFileByLaunch $uri2")
    val job2 = patterns.downloadFileByLaunch(uri2)

    println("Start downloadFileByLaunch $uri3")
    val job3 = patterns.downloadFileByLaunch(uri3)

    println("Start downloadPlainAsync $uri3")
    val data4 = patterns.downloadPlainAsync(uri3)

    response.await()
    println("Finished downloading by async $uri")
}