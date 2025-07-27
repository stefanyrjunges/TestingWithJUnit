import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginTest {

    private Connection connMockito;
    private PreparedStatement pstMockito;
    private ResultSet rsMockito;
    private Login login;

    @BeforeEach
    public void setUp() throws SQLException {
        connMockito = mock(Connection.class);
        pstMockito = mock(PreparedStatement.class);
        rsMockito = mock(ResultSet.class);

        when(connMockito.prepareStatement(any(String.class))).thenReturn(pstMockito);
        when(pstMockito.executeQuery()).thenReturn(rsMockito);

        login = new Login(connMockito);
    }

    @Test
    public void testLoginSuccess() throws SQLException {
        when (rsMockito.next()).thenReturn(true);
        boolean result = login.checkCredentials("username", "rightpassword");
        assertTrue(result);
        verify(pstMockito).setString(1, "username");
        verify(pstMockito).setString(2, "rightpassword");
    }

    @Test
    public void testLoginFail() throws SQLException {
        when (rsMockito.next()).thenReturn(false);
        boolean result = login.checkCredentials("username", "wrongpassword");
        assertFalse(result);
        verify(pstMockito).setString(1, "username");
        verify(pstMockito).setString(2, "wrongpassword");
    }

    @Test
    public void testLoginNoUser() throws SQLException {
        when (rsMockito.next()).thenReturn(false);
        boolean result = login.checkCredentials("inexistentUser", "password");
        assertFalse(result);
        verify(pstMockito).setString(1, "inexistentUser");
        verify(pstMockito).setString(2, "password");
    }

    @Test
    public void testLoginException() throws SQLException {
        when(connMockito.prepareStatement(any(String.class))).thenThrow(new SQLException("Failed to establish connection"));
        assertFalse(login.checkCredentials("testuser", "testpsw"));
    }


}
