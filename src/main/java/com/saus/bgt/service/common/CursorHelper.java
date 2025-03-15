package com.saus.bgt.service.common;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class CursorHelper {
    public static final String AFTER = "after";
    public static final String BEFORE = "before";
    public static final String FIRST = "first";
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    public CursorHelper() {
    }

    public ConnectionCursor cursorFrom(Object value) {
        return value != null && !value.toString().isEmpty() ? new DefaultConnectionCursor(Base64.getEncoder().encodeToString(value.toString().getBytes(StandardCharsets.UTF_8))) : null;
    }

    public Integer fromBase64String(String cursorString) {
        if (cursorString != null && !cursorString.isEmpty()) {
            byte[] cursorBytes = Base64.getDecoder().decode(cursorString);
            String decoded = new String(cursorBytes, StandardCharsets.UTF_8);
            int cursorInt = Integer.parseInt(decoded);
            return cursorInt;
        } else {
            return null;
        }
    }

    public int fromCursor(ConnectionCursor cursor) {
        return this.fromBase64String(cursor.getValue());
    }
}