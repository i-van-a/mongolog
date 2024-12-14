import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.Cookie;
import burp.api.montoya.http.message.params.*;
import burp.api.montoya.http.message.requests.HttpRequest;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.security.NoSuchAlgorithmException;
import java.lang.RuntimeException;
import java.util.List;
import burp.api.montoya.http.message.HttpHeader;
import java.time.format.DateTimeFormatter;
import burp.api.montoya.http.message.responses.analysis.AttributeType;
import burp.api.montoya.http.message.responses.analysis.Attribute;
import burp.api.montoya.proxy.*;
import burp.api.montoya.proxy.http.*;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;


public class MyExperimentalHandler implements HttpHandler,ProxyRequestHandler{

    private MontoyaApi api;
    private MyMongoClient myMongoClient;

    public MyExperimentalHandler( MyMongoClient myMongoClient, MontoyaApi api ){
        this.api = api;
        this.myMongoClient = myMongoClient;
    }

    // If connection fails, generate another one using the MyMongoClient object as follows
    // if fails, then : myMongoClient.resetConnection()


    // I am using the constructor of HttpInteraction instead of creating them with the object,
    // because idk the order of the requests, after that i might delete them and add them inside
    private ExtendedHttpRequest req = new ExtendedHttpRequest();
    private ExtendedHttpResponse res = new ExtendedHttpResponse();
    private HttpInteraction httpInteraction = new HttpInteraction( req, res );

    // [WARNING][URGENT] this shit is making the proxy intercepter letting everything trough 
    @Override
    public ProxyRequestToBeSentAction handleRequestToBeSent(InterceptedRequest interceptedRequest){
        httpInteraction.request.setListnerInterface(interceptedRequest.listenerInterface());
        return null;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent httpRequestToBeSent){
        httpInteraction.request.setHttpRequestProperties( httpRequestToBeSent );
        return null;
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived httpResponseReceived){
        httpInteraction.response.setHttpResponseProperties( httpResponseReceived );
        httpInteraction.loadToDb(this.myMongoClient);
        return null;
    }

    @Override 
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest){
        return null;
    }

}
