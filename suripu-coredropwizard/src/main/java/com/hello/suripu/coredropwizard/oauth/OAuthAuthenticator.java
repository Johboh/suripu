package com.hello.suripu.coredropwizard.oauth;


import com.hello.suripu.coredropwizard.oauth.stores.PersistentAccessTokenStore;
import com.hello.suripu.core.oauth.MissingRequiredScopeException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

public class OAuthAuthenticator implements Authenticator<String, AccessToken> {

    private PersistentAccessTokenStore tokenStore;
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthAuthenticator.class);

    public OAuthAuthenticator(PersistentAccessTokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public Optional<AccessToken> authenticate(String submittedToken) throws AuthenticationException {

        try {
            final com.google.common.base.Optional<AccessToken> token = tokenStore.getAccessTokenByToken(submittedToken, DateTime.now());
            if(!token.isPresent()) {
                LOGGER.warn("warning=token_not_present token={}", submittedToken);
                return Optional.empty();
            }
            return Optional.of(token.get());
        } catch (MissingRequiredScopeException e) {
            throw new MissingRequiredScopeAuthenticationException(e);
        }
    }
}
