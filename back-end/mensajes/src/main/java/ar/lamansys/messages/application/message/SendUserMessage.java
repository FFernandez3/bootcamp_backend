package ar.lamansys.messages.application.message;

import ar.lamansys.messages.application.ClearData;
import ar.lamansys.messages.application.user.AssertUserExists;
import ar.lamansys.messages.application.exception.UserNotExistsException;
import ar.lamansys.messages.application.exception.UserSessionNotExists;
import ar.lamansys.messages.domain.message.MessageMapper;
import ar.lamansys.messages.domain.message.NewMessageBo;
import ar.lamansys.messages.application.message.port.MessageStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SendUserMessage {
    private final ClearData.GetUserSession getUserSession;
    private final MessageStorage messageStorage;
    private final AssertUserExists assertUserExists;

    public void run(NewMessageBo newMessage) throws UserNotExistsException, UserSessionNotExists {
        assertUserExists.run(newMessage.getTargetId());
        var message = MessageMapper.buildMessage(
                getUserSession.run(),
                newMessage
        );
        messageStorage.save(message);
    }

}
