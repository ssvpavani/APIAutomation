package Files;

import com.google.gson.Gson;

public class payload {
    public static String locationdetails(int accuracy, String name, String phoneNumber, String address, String language) {
        String[] types = {"shoe park", "shop"};
        Gson gson = new Gson();
        String typesJson = gson.toJson(types);

        return  "{\r\n" +
                "  \"location\": {\r\n" +
                "    \"lat\": -38.383494,\r\n" +  // Assuming this is the latitude value
                "    \"lng\": 33.427362\r\n" +   // Assuming this is the longitude value
                "  },\r\n" +
                "  \"accuracy\": " + accuracy + ",\r\n" +
                "  \"name\": \"" + name + "\",\r\n" +
                "  \"phone_number\": \"" + phoneNumber + "\",\r\n" +
                "  \"address\": \"" + address + "\",\r\n" +
                "  \"types\": " + typesJson + ",\r\n" + // Constructing types as a JSON array
                "  \"website\": \"http://google.com\",\r\n" +
                "  \"language\": \"" + language + "\"\r\n" +
                "}\r\n";
    }
}
