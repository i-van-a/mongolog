import burp.api.montoya.http.message.responses.analysis.Attribute;
import burp.api.montoya.http.message.Cookie;
import burp.api.montoya.http.message.responses.analysis.AttributeType;
import burp.api.montoya.http.message.HttpHeader;
import java.util.List;
import java.util.ArrayList;
import org.bson.Document;
import java.time.format.DateTimeFormatter;
import burp.api.montoya.http.handler.HttpResponseReceived;


public class ExtendedHttpResponse{

    public ExtendedHttpResponse(){}

    public String httpVersion;
    public String reasonPhrase;
    public int statusCode;
    public List<Attribute> attributes;
    public List<HttpHeader> headers;
    public List<Cookie> cookies;
    public String inferredMimeType;
    public String mimeType;
    public String statedMimeType;
    public String body;
    public String rawHttpResponse;

    // [IMPLEMENT] For a particular mimetype you can either save the next things (6 possible combinations determined by 3 radio buttons)
    private String[] blacklistStoreBodyAsBinary = {"IMAGE_BMP", "IMAGE_GIF", "IMAGE_JPEG", "IMAGE_PNG", "IMAGE_TIFF", "IMAGE_UNKNOWN","SOUND", "VIDEO"};
    private String[] blacklistStoreFullMessageAsBinary = {"SOUND", "VIDEO"};
    private String[] blacklistStoreBodyAsPlainText = {"IMAGE_BMP", "IMAGE_GIF", "IMAGE_JPEG", "IMAGE_PNG", "IMAGE_TIFF", "IMAGE_UNKNOWN","SOUND", "VIDEO"};

    public void setHttpResponseProperties(HttpResponseReceived httpResponseReceived){
        this.httpVersion = httpResponseReceived.httpVersion();
        this.reasonPhrase = httpResponseReceived.reasonPhrase();
        this.statusCode = httpResponseReceived.statusCode();
        AttributeType[] attributeTypes = AttributeType.values();
        this.attributes = httpResponseReceived.attributes(attributeTypes);
        this.headers = httpResponseReceived.headers();
        this.cookies = httpResponseReceived.cookies();
        this.inferredMimeType = httpResponseReceived.inferredMimeType().toString();
        this.mimeType = httpResponseReceived.mimeType().toString();
        this.statedMimeType = httpResponseReceived.statedMimeType().toString();
        this.body = httpResponseReceived.bodyToString();
        this.rawHttpResponse = httpResponseReceived.toString();
    }

    public Document createDbSchema(){
        return  new Document()
                    .append("httpVersion", this.httpVersion)
                    .append("reasonPhrase", this.reasonPhrase)
                    .append("statusCode", this.statusCode)
                    .append("attributes", this.responseAttributesCreateDbSchemaList(this.attributes))
                    .append("headers", this.httpHeaderCreateDbSchemaList(this.headers))
                    .append("cookies", this.setCookiesCreateDbSchemaList(this.cookies))
                    .append("mimeType_ByBurp", this.mimeType)
                    .append("statedMimeType_InHeaders", this.statedMimeType)
                    .append("inferredMimeType_Signature", this.inferredMimeType)
                    .append("body", this.body );
                    //.append("rawHttpResponse", this.rawHttpResponse);
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

    private List<Document> responseAttributesCreateDbSchemaList(List<Attribute> attributeList){
        List<Document> attributeSchemaList = new ArrayList<>();
        for (Attribute attribute : attributeList) {
            attributeSchemaList.add(new Document()
                    .append("name", attribute.type())
                    .append("value", attribute.value())
        );};
        return attributeSchemaList;
    }

    private List<Document> setCookiesCreateDbSchemaList(List<Cookie> cookieList){
        List<Document> cookieSchemaList = new ArrayList<>();
        // The structure of this is different, because the Document has to be defined for cookie.expiration.ifPresent()
        for (Cookie cookie : cookieList) {
            Document cookieDocument = new Document()
                    .append("name", cookie.name())
                    .append("value", cookie.value())
                    .append("path", cookie.path())
                    .append("domain", cookie.domain());
            cookie.expiration().ifPresent( timestamp -> { cookieDocument.append("expiration", timestamp.format(java.time.format.DateTimeFormatter.ISO_INSTANT)); });
            cookieSchemaList.add(cookieDocument);};
        return cookieSchemaList;
    }
}