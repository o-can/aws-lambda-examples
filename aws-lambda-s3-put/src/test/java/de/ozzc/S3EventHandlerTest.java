package de.ozzc;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class S3EventHandlerTest {

    private S3Event s3Event;
    private Context context;


    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        S3EventNotification s3EventNotification = mapper.readValue(S3EventHandlerTest.class.getResource("/s3-event.put.json"), S3EventNotification.class);
        s3Event = new S3Event(s3EventNotification.getRecords());
        LambdaLogger lambdaLogger = mock(LambdaLogger.class);
        context = mock(Context.class);
        when(context.getLogger()).thenReturn(lambdaLogger);
    }

    @Test
    public void testHandleRequest() throws Exception {
        S3EventHandler s3EventHandler = new S3EventHandler();
        s3EventHandler.handleRequest(s3Event, context);
    }
}