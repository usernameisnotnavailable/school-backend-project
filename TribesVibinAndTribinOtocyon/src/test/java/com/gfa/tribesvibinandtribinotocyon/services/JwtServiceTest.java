package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.configs.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private Authentication authenticationMock;

    @BeforeEach
    public void setUp() {
        Mockito.reset(authenticationMock);
    }

    @Test
    public void shouldGenerateToken() {
        String username = "testUser";

        when(authenticationMock.getName()).thenReturn(username);

        when(authenticationMock.getAuthorities()).thenAnswer(invocation -> getGrantedAuthorities());

        ReflectionTestUtils.setField(jwtService, "TRIBES_KEY", "kU3go7GtxQmGDlqmaCELl0ir2dgPfMUMlOyWAex2LdzQRrY4+RsSPNBK/iucyYD4");

        String generatedToken = jwtService.generateToken(authenticationMock);

        assertEquals("testUser", jwtService.extractUsername(generatedToken));
        assertFalse(generatedToken.isEmpty());
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> "USER");
    }
}
