package com.avaki.cuBot.validators;


import com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.AbstractCommandHandler;
import com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.CuCommandHandler;
import com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.RegistrationCommandHandler;
import com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.WiFiCommandHandler;
import com.avaki.cuBot.businessLogic.messageHandlers.commandHandlers.adminCommandHandlers.MoveUserCommandHandler;
import com.avaki.cuBot.exceptions.RegistrationException;
import com.avaki.cuBot.models.User;
import com.avaki.cuBot.models.enums.AdminCommandConstants;
import com.avaki.cuBot.models.enums.CommandConstants;
import com.avaki.cuBot.models.enums.Status;
import com.avaki.cuBot.repositories.UserRepository;
import org.junit.Assert;
import org.junit.function.ThrowingRunnable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class RegistrationValidatorTest {

    @Mock
    private UserRepository userRepository;

    private final Message message = new Message();

    private final User user = new User();

    private static final long USER_ID = 1111L;
    private static final long ADMIN_ID = 2222L;
    private final List<AbstractCommandHandler> commandHandlerList = Arrays.asList(new CuCommandHandler(), new MoveUserCommandHandler(), new WiFiCommandHandler(), new RegistrationCommandHandler());

    @InjectMocks
    private RegistrationValidator registrationValidator = new RegistrationValidator();

    @BeforeEach
    public void beforeTest() {
        registrationValidator.setAdminId(ADMIN_ID);
    }

    @ParameterizedTest
    @MethodSource("provideStartAndRegisterCommandAndAnyUserIds")
    public void validateTest_StartOrRegisterCommand_userIsNull_anyUserId_shouldNotThrowException(CommandConstants command, long userId) {
        //Given
        message.setChat(new Chat(userId, "Type"));
        message.setText(command.getValue());

        //When
        registrationValidator.validate(message, commandHandlerList);

        //Then
        //The exception was not thrown
    }

    public static Stream<Arguments> provideStartAndRegisterCommandAndAnyUserIds() {
        List<CommandConstants> commands = Arrays.asList(CommandConstants.START, CommandConstants.REGISTER);
        List<Long> userIds = Arrays.asList(USER_ID, ADMIN_ID);

        return commands.stream()
                .flatMap(command -> userIds.stream().map(userId -> Arguments.of(command, userId)));
    }

    @ParameterizedTest
    @MethodSource("provideStartAndRegisterCommandAndAnyStatusAndAnyUserIds")
    public void validateTest_startOrRegisterCommand_anyUserStatus_anyUserId_shouldNotThrowException(CommandConstants command, Status status, long userId) {
        //Given
        message.setChat(new Chat(userId, "Type"));
        message.setText(command.getValue());
        user.setStatus(status);

        //When
        registrationValidator.validate(message, commandHandlerList);

        //Then
        //The exception was not thrown
    }

    private static Stream<Arguments> provideStartAndRegisterCommandAndAnyStatusAndAnyUserIds() {
        List<CommandConstants> commands = Arrays.asList(CommandConstants.START, CommandConstants.REGISTER);

        List<Status> statuses = Arrays.stream(Status.values()).collect(Collectors.toList());

        List<Long> userIDs = Arrays.asList(USER_ID, ADMIN_ID);

        //Получаем все комбинации трех наборов параметров
        return commands.stream()
                .flatMap(command -> statuses.stream()
                        .flatMap(status -> userIDs.stream()
                                .map(userID -> Arguments.of(command, status, userID))));
    }

    @ParameterizedTest
    @MethodSource("provideActiveStatusAndAnyCommandsAndAnyUserId")
    public void validateTest_ActiveStatus_commandsForActiveUsers_anyUserId_shouldNotThrowException(Status status, CommandConstants command, long userId) {
        // Given
        message.setChat(new Chat(userId, "Type"));
        message.setText(command.getValue());
        user.setStatus(status);

        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(Mockito.anyLong());

        // When
        registrationValidator.validate(message, commandHandlerList);

        // Then
        // The exception was not thrown
    }

    private static Stream<Arguments> provideActiveStatusAndAnyCommandsAndAnyUserId() {
        List<Status> statuses = Collections.singletonList(Status.ACTIVE);

        List<CommandConstants> enumCommands = Arrays.stream(CommandConstants.values())
                .filter(command -> command != CommandConstants.START && command != CommandConstants.REGISTER)
                .collect(Collectors.toList());

        List<Long> userIDs = Arrays.asList(USER_ID, ADMIN_ID);

        return statuses.stream()
                .flatMap(status -> enumCommands.stream()
                        .flatMap(command -> userIDs.stream()
                                .map(userId -> Arguments.of(status, command, userId))));
    }

    @ParameterizedTest
    @MethodSource("provideMovedStatusAndCommandsForRegisteredUsersAndAnyUserId")
    public void validateTest_MovedStatus_commandsForActiveUsers_anyUserId_shouldThrowException(CommandConstants command, long userId) {
        // Given
        message.setChat(new Chat(userId, "Type"));
        message.setText(command.getValue());
        user.setStatus(Status.MOVED);

        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(Mockito.anyLong());

        // When
        ThrowingRunnable task = () -> registrationValidator.validate(message, commandHandlerList);

        // Then
        Assert.assertThrows(RegistrationException.class, task);
    }

    private static Stream<Arguments> provideMovedStatusAndCommandsForRegisteredUsersAndAnyUserId() {
        List<CommandConstants> commands = Arrays.stream(CommandConstants.values())
                .filter(command -> command != CommandConstants.START && command != CommandConstants.REGISTER)
                .collect(Collectors.toList());

        List<Long> userIDs = Arrays.asList(USER_ID, ADMIN_ID);

        return commands.stream()
                        .flatMap(command -> userIDs.stream().map(userId -> Arguments.of(command, userId)));
    }

    @ParameterizedTest
    @MethodSource("provideMovedAdminCommands")
    public void validateTest_MovedStatus_adminCommands_notAdminId_shouldThrowException(AdminCommandConstants adminCommand) {
        // Given
        message.setChat(new Chat(USER_ID, "Type"));
        message.setText(adminCommand.getValue());
        user.setStatus(Status.MOVED);

        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(Mockito.anyLong());

        // When
        ThrowingRunnable task = () -> registrationValidator.validate(message, commandHandlerList);

        // Then
        Assert.assertThrows(RegistrationException.class, task);
    }

    private static Stream<Arguments> provideMovedAdminCommands() {
        List<AdminCommandConstants> adminCommands = Arrays.asList(AdminCommandConstants.values());
        return adminCommands.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("provideAdminCommandsAndAnyStatuses")
    public void validateTest_AdminCommands_adminId_shouldNotThrowException(AdminCommandConstants adminCommand, Status status) {
        //Given
        message.setChat(new Chat(ADMIN_ID, "Type"));
        message.setText(adminCommand.getValue());
        user.setStatus(status);

        //When
        registrationValidator.validate(message, commandHandlerList);

        //Then
    }

    private static Stream<Arguments> provideAdminCommandsAndAnyStatuses() {
        List<AdminCommandConstants> adminCommands = Arrays.stream(AdminCommandConstants.values()).collect(Collectors.toList());
        List<Status> statuses = Arrays.asList(Status.values());

        return adminCommands.stream()
                .flatMap(adminCommand -> statuses.stream().map(status -> Arguments.of(adminCommand, status)));
    }
}