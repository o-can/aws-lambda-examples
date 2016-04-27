package de.ozzc.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HelloWorldHandlerTest {

    private Context context;

    @Before
    public void setUp() throws Exception {
        LambdaLogger lambdaLogger = mock(LambdaLogger.class);
        context = mock(Context.class);
        when(context.getLogger()).thenReturn(lambdaLogger);
    }

    @Test
    public void testHandleRequest() throws Exception {
        HelloWorldHandler helloWorldHandlerHandler = new HelloWorldHandler();
        Assert.assertEquals("-1", helloWorldHandlerHandler.handleRequest(-1, context));
    }
}