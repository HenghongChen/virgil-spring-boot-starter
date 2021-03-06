package com.indeed.virgil.spring.boot.starter.endpoints;

import com.indeed.virgil.spring.boot.starter.models.EndpointResponse;
import com.indeed.virgil.spring.boot.starter.models.ImmutableEndpointResponse;
import com.indeed.virgil.spring.boot.starter.services.MessageOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static com.indeed.virgil.spring.boot.starter.util.EndpointConstants.ENDPOINT_DEFAULT_PATH_MAPPING;
import static com.indeed.virgil.spring.boot.starter.util.EndpointConstants.PUBLISH_MESSAGE_ENDPOINT_ID;

@Component
@Endpoint(id = PUBLISH_MESSAGE_ENDPOINT_ID)
class PublishMessageEndpoint implements IVirgilEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(PublishMessageEndpoint.class);

    private final MessageOperator messageOperator;

    @Autowired
    public PublishMessageEndpoint(final MessageOperator messageOperator) {
        this.messageOperator = messageOperator;
    }

    @WriteOperation
    public EndpointResponse<Serializable> index(final String fingerprint) {

        // ack message from the source queue; then publish message into the target/sink queue
        // edge case situation.. if process crashed in between, or ack success but publish failed, will result to missing message
        // in case we republish onto the same queue, and we publish first then ack,
        // we may ended up with acking both messages on that queue
        final boolean isSuccess = (messageOperator.ackCertainMessage(fingerprint) && messageOperator.publishCertainMessage(fingerprint));

        return ImmutableEndpointResponse.builder()
            .setData(isSuccess ? "Success!" : "Failure")
            .build();
    }

    public static String getEndpointId() {
        return PUBLISH_MESSAGE_ENDPOINT_ID;
    }

    public static String getEndpointPath() {
        return ENDPOINT_DEFAULT_PATH_MAPPING + getEndpointId();
    }
}
