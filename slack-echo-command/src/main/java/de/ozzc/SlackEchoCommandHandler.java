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
    private Region region = Region.getRegion(Regions.EU_CENTRAL_1);
    private String kmsEncryptedToken = "<kmsEncryptedToken>";

    @Override
    public String handleRequest(SlackRequest event, Context context) {
        if (token != null) {
            // "Container reuse, simply process the event with the key in memory"
            processEvent(event, context);
        } else if (kmsEncryptedToken != null && !kmsEncryptedToken.equals("<kmsEncryptedToken>")) {
            AWSKMSClient awskmsClient = new AWSKMSClient();
            if (region != null) {
                awskmsClient.setRegion(region);
            }
            ByteBuffer cipherText = ByteBuffer.wrap(Base64.getDecoder().decode(kmsEncryptedToken));
            DecryptRequest decryptRequest = new DecryptRequest().withCiphertextBlob(cipherText);
            DecryptResult decryptResult = awskmsClient.decrypt(decryptRequest);
            token = new String(decryptResult.getPlaintext().array());
            return processEvent(event, context);
        } else {
            throw new IllegalStateException("Token has not been set.");
        }
        return event.getBody();
    }

    public void setKmsEncryptedToken(String kmsEncryptedToken) {
        this.kmsEncryptedToken = kmsEncryptedToken;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String processEvent(SlackRequest slackRequest, Context context) {
        try {
            String decodedBody = URLDecoder.decode(slackRequest.getBody(), "UTF-8");
            Map<String, String> slackMessage = new HashMap<>();
            String[] tokens = decodedBody.split("&");
            for (String token : tokens) {
                String[] keyValuePair = token.split("=");
                if (keyValuePair.length == 2) {
                    slackMessage.put(keyValuePair[0], keyValuePair[1]);
                    context.getLogger().log("Key : " + keyValuePair[0] + ", Value : " + keyValuePair[1]);
                }
            }
            context.getLogger().log(slackRequest.getBody());
            return "success";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "failure";
    }
}
