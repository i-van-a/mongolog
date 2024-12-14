import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.Arrays;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoException;

import burp.api.montoya.MontoyaApi;

public class HttpInteraction{

    public HttpInteraction(ExtendedHttpRequest request,ExtendedHttpResponse response){
        this.request = request;
        this.response = response;
    }

    public ExtendedHttpRequest request;
    public ExtendedHttpResponse response;

    public void loadToDb(MyMongoClient myMongoClient){
        // [IMPLEMENT] In here should be the check to see if the database is connected
        try {

            Document document = new Document("_id", new ObjectId())
                                .append("response", this.response.createDbSchema())
                                .append("request", this.request.createDbSchema());

            InsertOneResult result = myMongoClient.collectionInConnection.insertOne( document ); 
            //myMongoClient.api.logging().logToOutput("Success! Inserted document id: " + result.getInsertedId());
        } catch (MongoException me) {
            myMongoClient.api.logging().logToOutput("Unable to insert due to an error: " + me);
        }
    }
}