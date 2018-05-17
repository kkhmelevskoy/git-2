import org.json.simple.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

public abstract class AbstractMessage {

    final List<String> lines;

    public AbstractMessage(List<String> lines) {
        this.lines = lines;
    }

    public List<String> getLines() {
        return lines;
    }

    private Date date;

    private String name;

    private long timer;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTimer() {
        return timer;
    }

    public void setTimer(Long timer) {
        this.timer = timer;
    }

    abstract void parse() throws ParseException;

    abstract public JSONObject toJson(Date startDate);

    abstract public Matcher toMatcher(String line, String str);

}
