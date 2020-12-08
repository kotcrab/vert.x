package io.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;

public class IssueReproducer {
  public static void main (String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new ResponseVerticle());
    vertx.deployVerticle(new MainVerticle());
  }

  static class MainVerticle extends AbstractVerticle {
    @Override
    public void start () throws Exception {
      HttpClient client = vertx.createHttpClient(new HttpClientOptions().setKeepAliveTimeout(3));
      sendRequest(client);
      sendRequest(client);
    }

    void sendRequest (HttpClient client) {
      client.request(HttpMethod.GET, 8890, "localhost", "/").onSuccess(
        req -> req.send().onSuccess(
          resp -> System.out.println("Request response: " + resp.statusCode())
        )
      );
    }
  }

  static class ResponseVerticle extends AbstractVerticle {
    @Override
    public void start () throws Exception {
      vertx.createHttpServer()
        .requestHandler(req -> req.response().end("OK"))
        .listen(8890);
    }
  }

}

