package de.ozzc;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Java Implementation of the Slack-Echo-Command Blueprint for AWS Lambda.
 *
 * @author Ozkan Can
 */
public class SlackEchoCommandHandler implements RequestHandler<SlackRequest, String> {


    private String token;
    private Region region;
    private String kmsEncryptedToken = "<kmsEncryptedToken>";

    public void setKmsEncryptedToken(String kmsEncryptedToken) {
        this.kmsEncryptedToken = kmsEncryptedToken;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String handleRequest(SlackRequest event, Context context) {
        if (token != null) {
            // "Container reuse, simply process the event with the key in memory"
            return processEvent(event, context);
        } else if (kmsEncryptedToken != null && !kmsEncryptedToken.equals("<kmsEncryptedToken>")) {
            if (region == null) {
                String regionName = System.getProperty("AWS_DEFAULT_REGION", "eu-central-1");
                region = Region.getRegion(Regions.fromName(regionName));
            }
            AWSKMSClient awskmsClient = new AWSKMSClient();
            awskmsClient.setRegion(region);
            ByteBuffer cipherText = ByteBuffer.wrap(Base64.getDecoder().decode(kmsEncryptedToken));
            DecryptRequest decryptRequest = new DecryptRequest().withCiphertextBlob(cipherText);
            DecryptResult decryptResult = awskmsClient.decrypt(decryptRequest);
            token = new String(decryptResult.getPlaintext().array());
            return processEvent(event, context);
        } else {
            throw new IllegalStateException("Token has not been set.");
        }
    }

    private String processEvent(SlackRequest slackRequest, Context context) {
        String body = slackRequest.getBody();
        Map<String, String> params = parseUrlEncodedData(body, "UTF-8");
        String requestToken = params.get("token");
        if (requestToken == null) {
            context.getLogger().log("ERROR: Request token was not provided");
            throw new IllegalArgumentException("Invalid request token");
        }
        if (!token.equals(requestToken)) {
            context.getLogger().log("ERROR: Request token (" + requestToken + ") does not match expected");
            throw new IllegalArgumentException("Invalid request token");
        }
        String user = params.get("user_name");
        String command = params.get("command");
        String channel = params.get("channel_name");
        String commandText = params.get("text");
        context.getLogger().log(user + " invoked " + command + " in " + channel + " with the following text : " + commandText);
        return "SUCCESS";
    }

    private Map<String, String> parseUrlEncodedData(String data, String charset) {
        if (data != null && !data.isEmpty()) {
            String charsetStr = (charset != null) ? charset : "UTF-8";
            String[] ampersandTokens = data.split("&");
            Map<String, String> kvMap = new HashMap<>(ampersandTokens.length);
            for (String kvTokens : ampersandTokens) {
                String[] kvPair = kvTokens.split("=");
                if (kvPair.length == 2) {
                    if (!kvPair[0].isEmpty()) {
                        try {
                            String key = URLDecoder.decode(kvPair[0], charsetStr);
                            String value = URLDecoder.decode(kvPair[1], charsetStr);
                            kvMap.put(key, value);
                        } catch (UnsupportedEncodingException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                }
            }
            return kvMap;
        }
        return new HashMap<>(0);
    }
}
