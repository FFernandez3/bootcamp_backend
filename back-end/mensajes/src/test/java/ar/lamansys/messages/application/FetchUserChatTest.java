package ar.lamansys.messages.application;

import ar.lamansys.messages.application.ClearData;
import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.exception.UserSessionNotExists;
import ar.lamansys.messages.application.message.FetchUserChat;
import ar.lamansys.messages.application.user.AssertUserExists;
import ar.lamansys.messages.domain.message.ChatMessageBo;
import ar.lamansys.messages.domain.message.MessageStoredBo;
import ar.lamansys.messages.application.message.port.MessageStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FetchUserChatTest {

    @Mock
    private ClearData.GetUserSession getUserSession;
    @Mock
    private MessageStorage messageStorage;
    @Mock
    private AssertUserExists assertUserExists;

    private FetchUserChat fetchUserChat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fetchUserChat = new FetchUserChat(getUserSession, messageStorage, assertUserExists);
    }

    @Test
    void fetchUserChat_ok() throws UserSessionNotExists, UserNotExistsException {
        // Arrange
        String sessionUserId = "user1";
        String contactId = "contact1";
        var storedMessagesBetween = List.of(
                new MessageStoredBo(sessionUserId, contactId, "Hola!"),
                new MessageStoredBo(contactId, sessionUserId,"Hola señor! Que desea?"),
                new MessageStoredBo(sessionUserId, contactId, "Quiero compra YA!")
        );

        when(getUserSession.run())
                .thenReturn(sessionUserId);
        when(messageStorage.findBetween(sessionUserId, contactId))
                .thenReturn(storedMessagesBetween.stream());

        // Act
        List<ChatMessageBo> actualMessages = fetchUserChat.run(contactId);

        // Verify
        assertEquals(3, actualMessages.size(), "The number of chat messages does not match the expected one.");

        var expectedMessages = List.of(
                new ChatMessageBo(true, "Hola!"),
                new ChatMessageBo(false ,"Hola señor! Que desea?"),
                new ChatMessageBo(true, "Quiero compra YA!")
        );
        assertEquals(expectedMessages, actualMessages, "The fetched chat messages do not match the expected ones.");
    }

    @Test
    void fetchUserChatUnexistedUser_throwException() throws UserSessionNotExists, UserNotExistsException {
        // Arrange
        String sessionUserId = "user1";
        String contactId = "contact1";

        when(getUserSession.run())
                .thenReturn(sessionUserId);
        Mockito.doThrow(new UserNotExistsException(contactId))
                .when(assertUserExists)
                .run(contactId);

        // Act y Verify
        assertThrows(UserNotExistsException.class, () -> fetchUserChat.run(contactId));
        verify(messageStorage, Mockito.never()).findBetween(sessionUserId, contactId);
    }

}