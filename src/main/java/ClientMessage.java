import org.json.simple.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientMessage extends AbstractMessage {

    List<ServerMessage> serverMessages = new ArrayList<>();

    public ClientMessage(List<String> lines) {
        super(lines);
    }

    public void parse() throws ParseException {
        List<String> serverLines = null;
        int i = 0;
        while (i != lines.size()) {
            if (Main.toDate(lines.get(i), Main.dateClient) != null) {
                setDate(Main.toDate(lines.get(i), Main.dateClient));
                setNameAndTimer(lines.get(i), toMatcher(lines.get(i), "([F][I][N][E])"));
                setNameAndTimer(lines.get(i), toMatcher(lines.get(i), "([I][N][F][O])"));
                setNameAndTimer(lines.get(i), toMatcher(lines.get(i), "([F][I][N][E][R])"));
                setNameAndTimer(lines.get(i), toMatcher(lines.get(i), "([F][I][N][E][S][T])"));
                setNameAndTimer(lines.get(i), toMatcher(lines.get(i), "([C][O][N][F][I][G])"));
                setNameAndTimer(lines.get(i), toMatcher(lines.get(i), "([S][E][V][E][R][E])"));
                setNameAndTimer(lines.get(i), toMatcher(lines.get(i), "([W][A][R][N][I][N][G])"));
            }

            if (Main.toDate(lines.get(i), Main.dateServer) != null) {
                if (serverLines != null) {
                    ServerMessage serverMessage = new ServerMessage(serverLines);
                    serverMessage.parse();
                    serverMessages.add(serverMessage);
                }
                serverLines = new ArrayList<>();
            }

            if  (serverLines != null) {
                serverLines.add(lines.get(i));
            }
            i++;
        }
        if (serverLines != null){
            ServerMessage serverMessage = new ServerMessage(serverLines);
            serverMessage.parse();
            serverMessages.add(serverMessage);
        }
    }

    public void setNameAndTimer(String line, Matcher level){
        Matcher matcherMs = toMatcher(line, "(\\s)([m][s])");
        if (level.find()) {
            if (matcherMs.find()) {
                String s = "";
                int index = matcherMs.start()-1;
                while (line.charAt(index) != ':'){
                    s = s + line.charAt(index);
                    index--;
                }
                String reverse = new StringBuffer(s).reverse().toString();
                setTimer(Long.valueOf(reverse.replaceAll("\\s","")));
                setName(line.substring(level.start(),matcherMs.start()));
            }
            else
                setName(line.substring(level.start(),line.length()));
        }
    }

    @Override
    public Matcher toMatcher(String line, String str) {
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(line);
        return matcher;
    }

    @Override
    public JSONObject toJson(Date startDate) {
        JSONObject jsonObject = new JSONObject();
        long time = (getDate().getTime() - startDate.getTime())*1000;
        jsonObject.put("ts", time);
        jsonObject.put("pid", 1);
        jsonObject.put("tid",1);
        if (getTimer() != 0){
            jsonObject.put("ph", "X");
            jsonObject.put("dur", getTimer()*1000);
        }
        else jsonObject.put("ph", "i");
        jsonObject.put("name", getName());
        return jsonObject;
    }

    public List<ServerMessage> getServerMessages() {
        return serverMessages;
    }

    @Override
    public String toString() {
        return  "\nclientDate: " + getDate() + " name: "+ getName()  + " timer: "+ getTimer() ;
    }

}
