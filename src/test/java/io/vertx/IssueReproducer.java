package io.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

public class IssueReproducer {
  public static void main (String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new ResponseVerticle());
    vertx.deployVerticle(new MainVerticle());
  }

  static class MainVerticle extends AbstractVerticle {
    @Override
    public void start () throws Exception {
      HttpClient client = vertx.createHttpClient(new HttpClientOptions().setKeepAliveTimeout(5));
      sendRequest(client);
      sendRequest(client);
    }

    void sendRequest (HttpClient client) {
      client.get(8890, "localhost", "/", res -> System.out.println("Response: "+ res.statusCode()))
        .end();
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
