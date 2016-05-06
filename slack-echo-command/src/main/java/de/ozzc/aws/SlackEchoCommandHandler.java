package de.ozzc.aws;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import de.ozzc.slack.SlackRequest;
import de.ozzc.slack.SlackSlashCommand;

import java.nio.ByteBuffer;
import java.util.Base64;

/**
 * Java Implementation of the Slack-Echo-Command Blueprint for AWS Lambda.
 *
 * @author Ozkan Can
 */
public class SlackEchoCommandHandler implements RequestHandler<SlackRequest, String> {


    private String token;
    private Region region;
    protected String kmsEncryptedToken = "<kmsEncryptedToken>";

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
        SlackSlashCommand slackSlashCommand = new SlackSlashCommand().withUrlEncodedString(slackRequest.getBody());
        if (slackSlashCommand.getToken() == null) {
            context.getLogger().log("ERROR: Request token was not provided");
            throw new IllegalArgumentException("Invalid request token");
        }
        if (!token.equals(slackSlashCommand.getToken())) {
            context.getLogger().log("ERROR: Request token (" + slackSlashCommand.getToken() + ") does not match expected");
            throw new IllegalArgumentException("Invalid request token");
        }
        context.getLogger().log(
                slackSlashCommand.getUserName()
                        + " invoked " + slackSlashCommand.getCommand()
                        + " in " + slackSlashCommand.getChannelName()
                        + " with the following response_url : " + slackSlashCommand.getResponseURL());
        return "SUCCESS";
    }
}
