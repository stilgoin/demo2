package com.example.demo;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpForward.forward;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpForward;
import org.mockserver.verify.VerificationTimes;

import shaded_package.org.apache.http.HttpResponse;
import shaded_package.org.apache.http.client.HttpClient;
import shaded_package.org.apache.http.client.config.RequestConfig;
import shaded_package.org.apache.http.client.methods.HttpGet;
import shaded_package.org.apache.http.client.methods.HttpPost;
import shaded_package.org.apache.http.config.ConnectionConfig;
import shaded_package.org.apache.http.entity.StringEntity;
import shaded_package.org.apache.http.impl.client.HttpClientBuilder;

//@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
		
	}
	
    private ClientAndServer mockServer;

    @BeforeEach
    public void startServer() {
        mockServer = startClientAndServer(1080);
    }
    
    @Test
    public void whenGetRequest_ThenForward(){
        createExpectationForForward();
        hitTheServerWithGetRequest("index.html");
        verifyGetRequest();

    }
    
    private void createExpectationForForward(){
        new MockServerClient("127.0.0.1", 1080)
            .when(
                request()
                   .withMethod("GET")
                   .withPath("/index.html"),
                   exactly(1)
                )
            .respond(response()
            		.withBody("UGHHULA456").withDelay(TimeUnit.SECONDS, 3));
                /*.forward(
                    forward()
                        .withHost("www.mock-server.com")
                        .withPort(80)
                        .withScheme(HttpForward.Scheme.HTTP)
                )*/;
    }


    public void verifyGetRequest() {
    	new MockServerClient("localhost", 1080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/index.html"),
                VerificationTimes.exactly(1)
        );
    }
    
    private HttpResponse hitTheServerWithGetRequest(String page) {
        String url = "http://127.0.0.1:1080/"+page;
        RequestConfig config = RequestConfig.custom()
        		//.setConnectionRequestTimeout(500)
        		//.setSocketTimeout(500)
        		//.setConnectionRequestTimeout(500)
        		.build();
        
        HttpClient client = HttpClientBuilder
        		.create()
        		.setDefaultRequestConfig(config)
        		.setConnectionTimeToLive(500, TimeUnit.MILLISECONDS).build();
        HttpResponse response=null;
        HttpGet get = new HttpGet(url);
        try {
            response=client.execute(get);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
    
    private HttpResponse hitTheServerWithPostRequest() {
        String url = "http://127.0.0.1:1080/validate";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/json");
        HttpResponse response=null;

        try {
            StringEntity stringEntity = new StringEntity("{username: 'foo', password: 'bar'}");
            post.getRequestLine();
            post.setEntity(stringEntity);
            response=client.execute(post);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
    
    @AfterEach
    public void stopServer() { 
        mockServer.stop();
    }

}
