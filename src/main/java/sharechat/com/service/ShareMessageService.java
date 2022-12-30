package sharechat.com.service;

import sharechat.com.repository.MessageRepository;
import sharechat.com.repository.ShareMessageRepository;

public class ShareMessageService {
    private final MessageRepository messageRepository;
    private final ShareMessageRepository shareMessageRepository;

    public ShareMessageService(MessageRepository messageRepository,ShareMessageRepository shareMessageRepository){
        this.messageRepository=messageRepository;
        this.shareMessageRepository=shareMessageRepository;
    }


}
