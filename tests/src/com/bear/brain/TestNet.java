package com.bear.brain;

import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.HeadlessNet;
import com.badlogic.gdx.net.HttpStatus;
import com.bear.brain.logic.coding.Coder;
import com.etheapp.brainserver.CommandController;
import com.etheapp.brainserver.commands.Unknown;
import com.etheapp.brainserver.logic.GGame;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class TestNet extends HeadlessNet {
    GGame game;

    public TestNet(GGame game) {
        super(new HeadlessApplicationConfiguration());
        this.game = game;
    }

    @Override
    public void sendHttpRequest(HttpRequest httpRequest, HttpResponseListener httpResponseListener) {
        httpResponseListener.handleHttpResponse(new HttpResponse() {
            @Override
            public byte[] getResult() {
                return new byte[0];
            }

            @Override
            public String getResultAsString() {
                try {
                    return CommandController.process(game, Coder.decode(httpRequest.getContent()), null);
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new Unknown().process(null, "", null);
            }

            @Override
            public InputStream getResultAsStream() {
                return null;
            }

            @Override
            public HttpStatus getStatus() {
                return new HttpStatus(HttpStatus.SC_OK);
            }

            @Override
            public String getHeader(String name) {
                return null;
            }

            @Override
            public Map<String, List<String>> getHeaders() {
                return null;
            }
        });
    }
}
