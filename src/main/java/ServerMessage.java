import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ServerMessage extends AbstractMessage {

    private String serverMsg;

    public String getServerMsg() {
        return serverMsg;
    }

    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }

    public ServerMessage(List<String> lines){
        super(lines);
    }

    @Override
    void parse() throws ParseException {
        int index = 0;
        String message = "";
        while (index < lines.size()) {
            String line = lines.get(index);
            if (Main.toDate(line, Main.dateServer)!= null) {
                setDate(Main.toDate(line,Main.dateServer));
                String nameStr = "";
                int lenghtDate = Main.dateServer.length() + 6;
                while (line.charAt(lenghtDate) != ':') {
                    nameStr = nameStr + line.charAt(lenghtDate);
                    lenghtDate++;
               }
               setName(nameStr);
               Matcher matcherMs = toMatcher(line, "((\\s)[m][s])");
               if (matcherMs.find()) {
                   String timerStr = "";
                   int i = matcherMs.start() - 1;
                   while (line.charAt(i) != ':') {
                       timerStr = timerStr + line.charAt(i);
                       i--;
                   }
                   String reverse = new StringBuffer(timerStr).reverse().toString();
                   setTimer(Long.valueOf(reverse.replaceAll("\\s", "")));
               }
           }
           else
               {
                   message = message + line;
               }
               index++;
        }
        setServerMsg(message);
        JSONArray jsonArray =  new JSONArray();
        jsonArray.add(getServerMsg());
    }

    @Override
    public Matcher toMatcher(String line, String str) {
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(line);
        return matcher;
    }

    @Override
    public JSONObject toJson(Date startDate) {
        JSONArray jsonArray =  new JSONArray();
        jsonArray.add(getServerMsg());
        JSONObject jsonObject = new JSONObject();
        long time = (getDate().getTime()-startDate.getTime())*1000;
        jsonObject.put("ts", time);
        jsonObject.put("pid", 2);
        jsonObject.put("tid",1);
        jsonObject.put("ph", "X");
        jsonObject.put("dur", getTimer()*1000);
        jsonObject.put("name", getName());
        jsonObject.put("args",jsonArray);
        return jsonObject;
    }

    @Override
    public String toString() {
        return  "\nserverDate: "+ getDate() + " name: "+ getName() + " timer: "+ getTimer() + " \nmessage: "+getServerMsg();
    }

}
