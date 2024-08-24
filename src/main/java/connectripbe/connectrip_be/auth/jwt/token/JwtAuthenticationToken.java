package connectripbe.connectrip_be.auth.jwt.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final long memberId;

    public JwtAuthenticationToken(long memberId) {
        super(null);
        this.memberId = memberId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return memberId;
    }
}
