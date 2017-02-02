package com.auth0.example;


import com.auth0.client.auth.AuthAPI;
import com.auth0.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

public class LoginServlet extends HttpServlet {

    private static final String REDIRECT_URI = "http://localhost:3099/callback";

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res)
            throws ServletException, IOException {
        final String clientId = getServletContext().getInitParameter("auth0.client_id");
        final String clientDomain = getServletContext().getInitParameter("auth0.domain");
        final String clientSecret = getServletContext().getInitParameter("auth0.client_secret");

        String state = createState();
        SessionUtils.setState(req, state);

        final AuthAPI authAPIClient = new AuthAPI(clientDomain, clientId, clientSecret);
        String authorizeUrl = authAPIClient
                .authorizeUrl(REDIRECT_URI)
                .withState(state)
                .build();
        res.sendRedirect(authorizeUrl);
    }


    /**
     * Create a randomly generated State value (for example: D27906B34E8B08554F43E0CDC4904BB2)
     *
     * @return the state value
     */
    private String createState() {
        final Random randomSource = new Random();
        byte random[] = new byte[16];
        StringBuilder buffer = new StringBuilder();
        randomSource.nextBytes(random);
        for (byte r : random) {
            byte b1 = (byte) ((r & 0xf0) >> 4);
            byte b2 = (byte) (r & 0x0f);
            if (b1 < 10)
                buffer.append((char) ('0' + b1));
            else
                buffer.append((char) ('A' + (b1 - 10)));
            if (b2 < 10)
                buffer.append((char) ('0' + b2));
            else
                buffer.append((char) ('A' + (b2 - 10)));
        }
        return buffer.toString();
    }
}