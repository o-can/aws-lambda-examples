package de.ozzc.aws;

import com.amazonaws.services.lambda.runtime.Context;
import de.ozzc.slack.SlackRequest;
import de.ozzc.slack.SlackSlashCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author Ozkan Can
 */
public class SlackEchoCommandHandlerTest {

    public static final String DUMMY_TOKEN = "Dummy";
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = mock(Context.class);
        when(context.getLogger()).thenReturn(System.out::println);
    }


    @Test
    public void testHandleRequest() throws Exception {
        SlackEchoCommandHandler slackEchoCommandHandler = new SlackEchoCommandHandler();
        slackEchoCommandHandler.setToken(DUMMY_TOKEN);
        SlackRequest slackRequest = new SlackRequest();
        SlackSlashCommand slackSlashCommand = new SlackSlashCommand()
                .withToken(DUMMY_TOKEN)
                .withCommand("/test")
                .withChannelName("test")
                .withText("test")
                .withResponseURL("http://127.0.0.1")
                .withUserName("junit")
                .withTeamId("T0001")
                .withTeamDomain("exampe")
                .withChannelId("C2147483705")
                .withUserId("U2147483697");
        slackRequest.setBody(slackSlashCommand.format());
        String result = slackEchoCommandHandler.handleRequest(slackRequest, context);
        Assert.assertEquals("SUCCESS", result);
    }
}