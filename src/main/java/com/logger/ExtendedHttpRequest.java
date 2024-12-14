

import java.util.List;
import java.util.ArrayList;

import burp.api.montoya.http.message.HttpHeader;
import burp.api.montoya.http.handler.HttpRequestToBeSent;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.HttpService;

import org.bson.Document;

public class ExtendedHttpRequest{

    public ExtendedHttpRequest(){}

    public int messageId;
    public String method;
    public String path;
    public String httpVersion;
    public List<HttpHeader> headers;
    public String contentType;
    public String fileExtension;
    public String host;
    public int hostPort;
    public String hostIpAddress;
    public Boolean https;
    public String body;
    public String rawHttpRequest;

    public String listenerInterface;

    public String toolSource;

    // [IMPLEMENT] For a particular tool if you will save or not the requests
    private String[] blacklistTools = {"INTRUDER"};

    // [IMPLMENT] fileExtension and hostIpAddress are missing
    // This Class has no cookies, so parse the header
    public void setHttpRequestProperties(HttpRequestToBeSent httpRequestToBeSent){
        this.messageId = httpRequestToBeSent.messageId();
        this.method = httpRequestToBeSent.method();
        this.path = httpRequestToBeSent.path();
        this.httpVersion = httpRequestToBeSent.httpVersion();
        this.headers = httpRequestToBeSent.headers();
        this.contentType = httpRequestToBeSent.contentType().toString();
        //this.fileExtension = httpRequestToBeSent.fileExtension(); // THIS IS WEIRD is not specified for httpRequestToBeSent but yes for HttpRequest, which it is supposed to inherit from, maybe it doesn't
        this.host = httpRequestToBeSent.httpService().host();
        this.hostPort = httpRequestToBeSent.httpService().port();
        //this.hostIpAddress = httpRequestToBeSent.httpService().ipAddress();
        this.https = httpRequestToBeSent.httpService().secure();
        this.toolSource = httpRequestToBeSent.toolSource().toolType().toString();
        this.body = httpRequestToBeSent.bodyToString();
        this.rawHttpRequest = httpRequestToBeSent.toString();
    }

    public Document createDbSchema(){
        return  new Document()
                    .append("messageId", this.messageId)
                    .append("method", this.method)
                    .append("path", this.path)
                    .append("httpVersion", this.httpVersion)
                    .append("headers", this.httpHeaderCreateDbSchemaList(this.headers))
                    .append("contentType", this.contentType)
                    //.append("fileExtension", this.fileExtension)
                    .append("host", this.host)
                    .append("hostPort", this.hostPort)
                    //.append("hostIpAddress", this.hostIpAddress)
                    .append("https", this.https)
                    .append("toolSource", this.toolSource)
                    .append("listenerInterface", this.listenerInterface)
                    .append("body", this.body );
                    //.append("rawHttpRequest", this.rawHttpRequest);
    }

    private List<Document> httpHeaderCreateDbSchemaList(List<HttpHeader> httpHeaderList){
        List<Document> headerSchemaList = new ArrayList<>();
        for (HttpHeader httpHeader : httpHeaderList) {
            headerSchemaList.add(new Document()
                    .append("name", httpHeader.name())
                    .append("value", httpHeader.value())
        );};
        return headerSchemaList;
    }

    public void setListnerInterface(String listenerInterface){
        this.listenerInterface = listenerInterface;
    }

}