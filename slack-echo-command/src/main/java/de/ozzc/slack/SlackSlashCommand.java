package de.ozzc.slack;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ozkan Can
 */
public class SlackSlashCommand {

    private String token;
    private String userName;
    private String text;
    private String command;
    private String channelName;
    private String responseURL;
    private String teamId;
    private String channelId;
    private String userId;
    private String teamDomain;


    public SlackSlashCommand withTeamId(String teamId) {
        this.teamId = teamId;
        return this;
    }
    public SlackSlashCommand withChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }
    public SlackSlashCommand withUserId(String userId) {
        this.userId = userId;
        return this;
    }
    public SlackSlashCommand withTeamDomain(String teamDomain) {
        this.teamDomain = teamDomain;
        return this;
    }


    public SlackSlashCommand withToken(String token) {
        this.token = token;
        return this;
    }

    public SlackSlashCommand withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public SlackSlashCommand withText(String text) {
        this.text = text;
        return this;
    }

    public SlackSlashCommand withCommand(String command) {
        this.command = command;
        return this;
    }

    public SlackSlashCommand withChannelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public SlackSlashCommand withResponseURL(String responseURL) {
        this.responseURL = responseURL;
        return this;
    }

    public SlackSlashCommand withUrlEncodedString(String urlEncodedString)
    {
        List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(urlEncodedString, Charset.forName("UTF-8"));
        parse(nameValuePairs);
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getResponseURL() {
        return responseURL;
    }

    public void setResponseURL(String responseURL) {
        this.responseURL = responseURL;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTeamDomain() {
        return teamDomain;
    }

    public void setTeamDomain(String teamDomain) {
        this.teamDomain = teamDomain;
    }

    public void parse(List<NameValuePair> nameValuePairs) {
        if (nameValuePairs != null) {
            for (NameValuePair nameValuePair : nameValuePairs) {
                switch (nameValuePair.getName()) {
                    case "token":
                        this.token = nameValuePair.getValue();
                        break;
                    case "user_name":
                        this.userName = nameValuePair.getValue();
                        break;
                    case "text":
                        this.text = nameValuePair.getValue();
                        break;
                    case "command":
                        this.command = nameValuePair.getValue();
                        break;
                    case "channel_name":
                        this.channelName = nameValuePair.getValue();
                        break;
                    case "response_url":
                        this.responseURL = nameValuePair.getValue();
                        break;
                    case "team_id":
                        this.teamId = nameValuePair.getValue();
                        break;
                    case "team_domain":
                        this.teamDomain = nameValuePair.getValue();
                        break;
                    case "channel_id":
                        this.channelId = nameValuePair.getValue();
                        break;
                    case "user_id":
                        this.userId = nameValuePair.getValue();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public String format() {
        List<BasicNameValuePair> pairs = new ArrayList<>(7);
        pairs.add(new BasicNameValuePair("token", token));
        pairs.add(new BasicNameValuePair("user_name", userName));
        pairs.add(new BasicNameValuePair("text", text));
        pairs.add(new BasicNameValuePair("command", command));
        pairs.add(new BasicNameValuePair("channel_name", channelName));
        pairs.add(new BasicNameValuePair("response_url", responseURL));
        return URLEncodedUtils.format(pairs, Charset.forName("UTF-8"));
    }

}
