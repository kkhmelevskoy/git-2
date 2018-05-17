import com.google.gson.Gson;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Main {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
   // public static final String fileName = "C:/Users/Home/IdeaProjects/Parse/log.json";
    public static final String dateServer = "yyyy-MM-dd HH:mm:ss,SSS Z";
    public static final String dateClient = "dd.MM.yyyy HH:mm:ss,SSS Z";

    public static void main(String[] args) throws IOException, ParseException {
        UtilityForFile utility = new UtilityForFile();
        utility.openFile();
        File file = (utility.getPath());
        if (file != null) {
            BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(file), "cp1251"));
            String line;
            Date startDate;
            List<ClientMessage> messages = new ArrayList<>();
            List<String> lines = null;
            while ((line = fin.readLine()) != null) {
                if (toDate(line, dateClient) != null) {
                    if (lines != null) {
                        ClientMessage clientMessage = new ClientMessage(lines);
                        clientMessage.parse();
                        messages.add(clientMessage);
                    }
                    lines = new ArrayList<>();
                }
                if (lines != null) {
                    lines.add(line);
                }
            }

            ClientMessage clientMessage = new ClientMessage(lines);
            clientMessage.parse();

            messages.add(clientMessage);
            startDate = messages.get(0).getDate();
            JSONArray traceEvents = new JSONArray();
            for (ClientMessage message : messages) {
                traceEvents.add(message.toJson(startDate));
                List<ServerMessage> serverMessages = message.getServerMessages();
                for (ServerMessage serverMessage : serverMessages) {
                    traceEvents.add(serverMessage.toJson(startDate));
                }
            }

            for (int i = 0; i < traceEvents.size(); i++) {
                System.out.println(traceEvents.get(i));
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("traceEvents", traceEvents);
            utility.saveFile(jsonObject.toString());

        }
        else System.out.println("Файл не выбран");
    }

    public static Date toDate(String line, String dateFormat) throws ParseException {
        int len = dateFormat.length() + 4;
        if (line.length() >= len) {
            String date = line.substring(0, 29);
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            try {
                return format.parse(date);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

}
