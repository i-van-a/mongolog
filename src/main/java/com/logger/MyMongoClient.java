import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.Arrays;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import burp.api.montoya.MontoyaApi;

// This is a connection that is just connected to a single connection, the one specified in its construction, the only method is 
// resetConnection, which retries to connect. It will connect in its construction. 
// It uses the montoya Api interface so it can log in the burp log any error.
// It only has the attribute connectionToCollection, which serves as an interface to the provided collection

// A new connection is created every time
public class MyMongoClient {

    public MongoCollection<Document> collectionInConnection;
    public MongoDatabase database;
    public MontoyaApi api;
    private String uri;
    private String databaseName;
    private String collectionName;

    public MyMongoClient( String[] connectionDetails, MontoyaApi api ){
        this.api = api;
        this.uri = connectionDetails[0];
        this.databaseName = connectionDetails[1];
        this.collectionName = connectionDetails[2];
        resetConnection();
        //debugDatabase(); // [WARNING] Do not use it, because it'll pollute your database, just change the connection name before
    }

    public void resetConnection() {
        api.logging().logToOutput(this.uri+"/"+this.databaseName+". Collection: "+this.collectionName);
        try{
            // [IMPLEMENT] In here you should itterate a while until the connection is succesful
            MongoClient mongoClient = MongoClients.create(this.uri);
            api.logging().logToOutput("Connection to database " + this.uri +  " successful.");
            MongoDatabase database = mongoClient.getDatabase(this.databaseName);
            this.collectionInConnection = database.getCollection(this.collectionName);
        } catch (Exception e) {
            api.logging().logToOutput("Error: " + e.getMessage());
        }
    }
    //debugDatabase(); // [WARNING] Do not use it, because it'll pollute your database, just change the connection name before
    public void debugDatabase(){
        // [IMPLEMENT] In here should be the check to see if the database is connected
        try {
            InsertOneResult result = this.collectionInConnection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("title", "Ski Bloopers")
                    .append("genres", Arrays.asList("Documentary", "Comedy"))); 
            api.logging().logToOutput("Success! Inserted document id: " + result.getInsertedId());
        } catch (MongoException me) {
            api.logging().logToOutput("Unable to insert due to an error: " + me);
        }
    }
}