package util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DynamoDB {

    private static final String ACCESS_KEY = "AKIA3LJJJDS25MMBYGTU";
    private static final String SECRET_KEY = "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";
    private static AmazonDynamoDB client = null;

    public static AmazonDynamoDB get() {
        if (client != null) return client;
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2) //Ohio!
                .build();
        return client;
    }

    public static CreateTableResult createTable(@NotNull String tableName, ProvisionedThroughput provisionedThroughput, AttributeDefinition... attributeDefinitions) {
        AttributeDefinition[] attributeDefinitionsWithKey = new AttributeDefinition[attributeDefinitions.length + 1];
        for (int i = 0; i < attributeDefinitions.length; i++) {
            attributeDefinitionsWithKey[i] = attributeDefinitions[i];
        }
        attributeDefinitionsWithKey[attributeDefinitionsWithKey.length - 1] = new AttributeDefinition("id", ScalarAttributeType.S);
        CreateTableRequest request = new CreateTableRequest()
                .withAttributeDefinitions(attributeDefinitionsWithKey)
                .withKeySchema(new KeySchemaElement("id", KeyType.HASH))
                .withProvisionedThroughput(provisionedThroughput)
                .withTableName(tableName);
        return get().createTable(request);
    }

    public static List<String> listTables() {
        ListTablesRequest listTablesRequest;
        listTablesRequest = new ListTablesRequest().withLimit(10);
        ListTablesResult tableList = get().listTables(listTablesRequest);
        return tableList.getTableNames();
    }

    public static TableDescription describeTable(@NotNull String tableName) {
        return get().describeTable(tableName).getTable();
    }

    public static List<AttributeDefinition> getAttributeDefinitions(@NotNull String tableName) {
        return describeTable(tableName).getAttributeDefinitions();
    }

    public static void updateTable(@NotNull String tableName, ProvisionedThroughput provisionedThroughput) {
        get().updateTable(tableName, provisionedThroughput);
    }

    public static void deleteTable(@NotNull String tableName) {
        get().deleteTable(tableName);
    }

    public static void addItem(@NotNull String tableName, HashMap<String, AttributeValue> itemValues) {
        itemValues.put("id", new AttributeValue(UUID.randomUUID().toString()));
        get().putItem(tableName, itemValues);
    }

    public static Map<String,AttributeValue> getItem(@NotNull String tableName, String id) {
        HashMap<String,AttributeValue> keyToGet = new HashMap<>();

        keyToGet.put("id", new AttributeValue(id));

        GetItemRequest request = new GetItemRequest()
                .withKey(keyToGet)
                .withTableName(tableName);

        return get().getItem(request).getItem();
    }

    public static UpdateItemResult updateItem(@NotNull String tableName, String id, HashMap<String, AttributeValueUpdate> updatedValues) {
        HashMap<String,AttributeValue> itemKey = new HashMap<>();
        itemKey.put("id", new AttributeValue(id));
        return get().updateItem(tableName, itemKey, updatedValues);
    }

    public static DeleteItemResult deleteItem(@NotNull String tableName, String id) {
        HashMap<String, AttributeValue> attributes = new HashMap<>();
        attributes.put("id", new AttributeValue(id));
        return get().deleteItem(tableName, attributes);
    }

}
