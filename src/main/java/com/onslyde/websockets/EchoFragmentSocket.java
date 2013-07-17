package com.onslyde.websockets;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.extensions.Frame;

/**
 * Echo back the incoming text or binary as 2 frames of (roughly) equal size.
 */
@WebSocket
public class EchoFragmentSocket
{
    @OnWebSocketFrame
    public void onFrame(Session session, Frame frame)
    {
        if (frame.getType().isData())
        {
            return;
        }

        ByteBuffer data = frame.getPayload();

        int half = data.remaining() / 2;

        ByteBuffer buf1 = data.slice();
        ByteBuffer buf2 = data.slice();

        buf1.limit(half);
        buf2.position(half);

        RemoteEndpoint remote = session.getRemote();
        try
        {
            switch (frame.getType())
            {
                case BINARY:
                    remote.sendBytesByFuture(buf1);
                    remote.sendBytesByFuture(buf2);
                    break;
                case TEXT:
                    // NOTE: This impl is not smart enough to split on a UTF8 boundary
                    remote.sendStringByFuture(BufferUtil.toUTF8String(buf1));
                    remote.sendStringByFuture(BufferUtil.toUTF8String(buf2));
                    break;
                default:
                    throw new IOException("Unexpected frame type: " + frame.getType());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
        }
    }
}