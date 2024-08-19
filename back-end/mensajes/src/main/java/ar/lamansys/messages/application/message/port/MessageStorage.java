package ar.lamansys.messages.application.message.port;

import java.util.stream.Stream;

import ar.lamansys.messages.domain.message.MessageStoredBo;

public interface MessageStorage {

    void save(
            MessageStoredBo message
    );

    Stream<MessageStoredBo> findBetween(
            String oneContact,
            String otherContact
    );

    Stream<MessageStoredBo> findByContact(
            String userId
    );

    void deleteAll();
}
