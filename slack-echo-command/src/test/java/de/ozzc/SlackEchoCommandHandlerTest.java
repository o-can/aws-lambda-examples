package de.ozzc;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 *
 * @author Ozkan Can
 */
public class SlackEchoCommandHandlerTest {

    private Context context;

    // private String keyId = "arn:...";

    @Before
    public void setUp() throws Exception {
        LambdaLogger lambdaLogger = mock(LambdaLogger.class);
        context = mock(Context.class);
        when(context.getLogger()).thenReturn(System.out::println);
    }

    @Test
    public void testHandleRequest() throws Exception {
        /*
        AWSKMSClient awskmsClient = new AWSKMSClient();
        awskmsClient.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
        String token = "Dummy";
        ByteBuffer plainText = ByteBuffer.wrap(token.getBytes());
        EncryptRequest encryptRequest = new EncryptRequest().withKeyId(keyId).withPlaintext(plainText);
        EncryptResult encryptResult = awskmsClient.encrypt(encryptRequest);
        String kmsEncryptedToken = Base64.getEncoder().encodeToString(encryptResult.getCiphertextBlob().array());
        */

        SlackEchoCommandHandler slackEchoCommandHandler = new SlackEchoCommandHandler();
        slackEchoCommandHandler.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
        SlackRequest slackRequest = new SlackRequest();
        slackRequest.setBody("Hello");
        slackEchoCommandHandler.handleRequest(slackRequest, context);
    }
}