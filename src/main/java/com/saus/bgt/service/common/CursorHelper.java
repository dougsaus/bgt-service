package com.saus.bgt.service.common;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class CursorHelper {
    public ConnectionCursor cursorFrom(Object value) {
        return value != null && !value.toString().isEmpty() ? new DefaultConnectionCursor(Base64.getEncoder().encodeToString(value.toString().getBytes(StandardCharsets.UTF_8))) : null;
    }

    public Integer fromBase64String(String cursorString) {
        Integer cursor = null;
        if (cursorString != null && !cursorString.isEmpty()) {
            byte[] cursorBytes = Base64.getDecoder().decode(cursorString);
            String decoded = new String(cursorBytes, StandardCharsets.UTF_8);
            cursor = Integer.parseInt(decoded);
        }
        return cursor;
    }

}