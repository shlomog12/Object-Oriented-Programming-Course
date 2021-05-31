package api;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This class is a deserializer of directed_weighted_graph
 * This class can deserialize a graph given in a Json format of nodes and edges
 * @Authors Shlomo Glick and Gilad Shotland
 */
public class dwJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {
    @Override
    public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        directed_weighted_graph graph = new DWGraph_DS();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray nodesArray = jsonObject.get("Nodes").getAsJsonArray();
        for (JsonElement jasonNode : nodesArray) { //create every node manually and add it to the graph
            String idString = jasonNode.getAsJsonObject().get("id").getAsString();
            node_data theNode = new nodeDate(Integer.parseInt(idString));
            theNode.setLocation(xyz(jasonNode.getAsJsonObject().get("pos").getAsString()));
            graph.addNode(theNode);
        }
        JsonArray edgesArray = jsonObject.get("Edges").getAsJsonArray();
        for(JsonElement jasonEdge : edgesArray){ //create every edge manually and add it to the graph
            int src = Integer.parseInt(jasonEdge.getAsJsonObject().get("src").getAsString());
            int dest = Integer.parseInt(jasonEdge.getAsJsonObject().get("dest").getAsString());
            double weight = Double.parseDouble(jasonEdge.getAsJsonObject().get("w").getAsString());
            graph.connect(src,dest,weight);
        }
        return graph;

    }
    //private method for creating locations from a Json formatted String
    private geoLocation xyz(String location) {
        String x = "";
        String y = "";
        String z = "";
        int i= 0 ;
        for (; i < location.length(); i++) {
            if (location.charAt(i) == ',') {
                break;
            }
            x += location.charAt(i);
        }
        i++;
        for (;i<location.length() ; i++){
            if(location.charAt(i) == ','){
                break;
            }
            y += location.charAt(i);
        }
        i++;
        for(;i<location.length() ; i++){
            if(location.charAt(i) == 'i'){
                break;
            }
            z += location.charAt(i);
        }
        return new geoLocation(Double.parseDouble(x),Double.parseDouble(y),Double.parseDouble(z));
    }
}
